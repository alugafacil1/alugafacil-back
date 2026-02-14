package br.edu.ufape.alugafacil.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;

import br.edu.ufape.alugafacil.dtos.property.PropertyFilterRequest;
import br.edu.ufape.alugafacil.dtos.property.PropertyRequest;
import br.edu.ufape.alugafacil.dtos.property.PropertyResponse;
import br.edu.ufape.alugafacil.enums.PaymentStatus;
import br.edu.ufape.alugafacil.enums.PropertyStatus;

import br.edu.ufape.alugafacil.mappers.PropertyMapper;
import br.edu.ufape.alugafacil.models.Plan;
import br.edu.ufape.alugafacil.models.Property;
import br.edu.ufape.alugafacil.models.QProperty;
import br.edu.ufape.alugafacil.models.User;
import br.edu.ufape.alugafacil.models.UserSearchPreference;
import br.edu.ufape.alugafacil.repositories.PropertyRepository;
import br.edu.ufape.alugafacil.repositories.SubscriptionRepository;
import br.edu.ufape.alugafacil.repositories.UserRepository;
import br.edu.ufape.alugafacil.repositories.UserSearchPreferenceRepository;
import br.edu.ufape.alugafacil.services.interfaces.IFileStorageService;
import br.edu.ufape.alugafacil.services.interfaces.INotificationService;
import br.edu.ufape.alugafacil.services.interfaces.IPropertyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PropertyService implements IPropertyService {
    
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final PropertyMapper propertyMapper;
    private final IFileStorageService fileStorageService;
    
    // Novas injeções
    private final UserSearchPreferenceRepository preferenceRepository;
    private final INotificationService notificationService;
    private final SubscriptionRepository subscriptionRepository;
    
    // Método auxiliar para pegar plano
    private Plan getUserActivePlan(User user) {
        return subscriptionRepository.findFirstByUserUserIdAndStatus(user.getUserId(), PaymentStatus.ACTIVE)
                .map(subscription -> subscription.getPlan())
                .orElseGet(() -> {
                    // Fallback: Plano Free Padrão (Idealmente viria do banco)
                    Plan freePlan = new Plan();
                    freePlan.setPropertiesCount(1);
                    freePlan.setImagesCount(5);
                    freePlan.setHasVideo(false);
                    freePlan.setIsPriority(false);
                    return freePlan;
                });
    }
    
    @Override
    @Transactional
    public PropertyResponse createProperty(PropertyRequest request) {
        // 1. Busca Dono
        User owner = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + request.userId()));
        
        // 2. Valida Plano
        Plan plan = getUserActivePlan(owner);
        long currentProperties = propertyRepository.countPropertiesByUser(owner.getUserId(), PropertyStatus.ACTIVE);
        
        if (currentProperties >= plan.getPropertiesCount()) {
            throw new RuntimeException("Upgrade necessário! Seu plano permite apenas " + plan.getPropertiesCount() + " imóveis.");
        }
        
        if (request.videoUrl() != null && !request.videoUrl().isEmpty() && !plan.getHasVideo()) {
            throw new RuntimeException("Seu plano atual não permite adicionar vídeos ao anúncio.");
        }
        
        // 3. Monta Entidade
        Property property = propertyMapper.toEntity(request);
        property.setUser(owner);
        property.setIsPriority(plan.getIsPriority()); 
        property.setStatus(PropertyStatus.ACTIVE);

        // 4. Salva
        Property savedProperty = propertyRepository.save(property);
        
        // 5. Trigger de Notificação (Async)
        notifyInterestedUsers(savedProperty);
        
        return propertyMapper.toResponse(savedProperty);
    }

    /**
     * Busca usuários interessados e dispara notificação.
     * @Async garante que não trave a resposta da API.
     */
    @Async 
    protected void notifyInterestedUsers(Property property) {
        // Preparação de dados (Null Safety)
        Integer garageCount = (property.getGarage() != null && property.getGarage()) ? 1 : 0;
        Double lat = (property.getGeolocation() != null) ? property.getGeolocation().getLatitude() : null;
        Double lon = (property.getGeolocation() != null) ? property.getGeolocation().getLongitude() : null;
        String city = (property.getAddress() != null) ? property.getAddress().getCity() : null;
        String neighborhood = (property.getAddress() != null) ? property.getAddress().getNeighborhood() : null;

        // Busca Matches
        List<UserSearchPreference> matches = preferenceRepository.findMatchingPreferences(
            property.getPriceInCents(),
            property.getNumberOfBedrooms(),
            property.getNumberOfBathrooms(),
            garageCount,
            property.getFurnished(),
            property.getPetFriendly(),
            city,
            neighborhood,
            lat,
            lon
        );

        // Dispara Notificações
        for (UserSearchPreference preference : matches) {
            if (property.getUser() != null && preference.getUser().getUserId().equals(property.getUser().getUserId())) {
                continue; // Não notificar a si mesmo
            }

            notificationService.notifyListingMatch(
                preference.getUser().getUserId(),
                property.getPropertyId(), 
                preference.getName()
            );
        }
        log.info("Notificações enviadas para {} usuários interessados.", matches.size());
    }

    @Override
    public PropertyResponse getPropertyById(UUID id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imóvel não encontrado"));
        return propertyMapper.toResponse(property);
    }

    @Override
    public Page<PropertyResponse> getAllProperties(PropertyFilterRequest filters, Pageable pageable) {
        QProperty qProperty = QProperty.property;
        BooleanBuilder builder = new BooleanBuilder();
    
        if (filters != null) {
            if (filters.getLat() != null && filters.getLon() != null && filters.getRadius() != null) {
                NumberExpression<Double> dbLat = qProperty.geolocation.latitude;
                NumberExpression<Double> dbLon = qProperty.geolocation.longitude;
                
                NumberExpression<Double> distanceExpression = Expressions.numberTemplate(Double.class,
                        "(6371 * acos(cos(radians({0})) * cos(radians({1})) * cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1}))))",
                        Expressions.constant(filters.getLat()), 
                        dbLat,                                
                        dbLon,                                
                        Expressions.constant(filters.getLon())  
                );
                builder.and(distanceExpression.loe(filters.getRadius()));
            }
            if (filters.getMinPrice() != null) builder.and(qProperty.priceInCents.goe(filters.getMinPrice() * 100));
            if (filters.getMaxPrice() != null) builder.and(qProperty.priceInCents.loe(filters.getMaxPrice() * 100));
            if (filters.getMinRooms() != null) builder.and(qProperty.numberOfRooms.goe(filters.getMinRooms()));
            if (filters.getMinBedrooms() != null) builder.and(qProperty.numberOfBedrooms.goe(filters.getMinBedrooms()));
            if (filters.getFurnished() != null) builder.and(qProperty.furnished.eq(filters.getFurnished()));
            if (filters.getPetFriendly() != null) builder.and(qProperty.petFriendly.eq(filters.getPetFriendly()));
            if (filters.getType() != null) builder.and(qProperty.type.eq(filters.getType()));
            
            if (filters.getStatus() != null) {
                builder.and(qProperty.status.eq(filters.getStatus()));
            } else {
                 builder.and(qProperty.status.eq(PropertyStatus.ACTIVE));
            }

            if (filters.getCity() != null && !filters.getCity().isEmpty()) {
                builder.and(qProperty.address.city.containsIgnoreCase(filters.getCity()));
            }
            if (filters.getNeighborhood() != null && !filters.getNeighborhood().isEmpty()) {
                builder.and(qProperty.address.neighborhood.containsIgnoreCase(filters.getNeighborhood()));
            }
        }
        
        Page<Property> pageResult = propertyRepository.findAll((Predicate) builder, pageable);
        return pageResult.map(propertyMapper::toResponse);
    }

    @Override
    public List<PropertyResponse> getPropertiesByUserId(UUID userId) {
        return propertyRepository.findByUserUserId(userId).stream()
                .map(propertyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PropertyResponse updateProperty(UUID id, PropertyRequest request) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imóvel não encontrado"));

        Plan plan = getUserActivePlan(property.getUser());

        if (request.videoUrl() != null && !request.videoUrl().isBlank() && !plan.getHasVideo()) {
            throw new RuntimeException("Seu plano atual não permite adicionar vídeos.");
        }

        if (property.getStatus() != PropertyStatus.ACTIVE && request.status() == PropertyStatus.ACTIVE) {
            long currentActive = propertyRepository.countPropertiesByUser(property.getUser().getUserId(), PropertyStatus.ACTIVE);
            
            if (currentActive >= plan.getPropertiesCount()) {
                throw new RuntimeException("Limite de imóveis ativos atingido (" + plan.getPropertiesCount() + ").");
            }
        }

        propertyMapper.updateEntityFromDto(request, property);
        property.setIsPriority(plan.getIsPriority());

        return propertyMapper.toResponse(propertyRepository.save(property));
    }

    @Override
    public void deleteProperty(UUID id) {
        if (!propertyRepository.existsById(id)) {
            throw new RuntimeException("Imóvel não encontrado");
        }
        propertyRepository.deleteById(id);
    }

    @Override
    public PropertyResponse addPhotos(UUID id, List<MultipartFile> files) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imóvel não encontrado"));
        
        Plan plan = getUserActivePlan(property.getUser());
        
        if (property.getPhotoUrls() == null) {
            property.setPhotoUrls(new ArrayList<>());
        }
        
        int currentCount = property.getPhotoUrls().size();
        int newCount = files.size();
        int limit = plan.getImagesCount();
        
        if ((currentCount + newCount) > limit) {
            throw new RuntimeException("Limite de fotos excedido! Máximo: " + limit);
        }
        
        for (MultipartFile file : files) {
            String photoUrl = fileStorageService.uploadFile(file);
            property.getPhotoUrls().add(photoUrl);
        }
        
        return propertyMapper.toResponse(propertyRepository.save(property));
    }
}