package br.edu.ufape.alugafacil.repositories;


import br.edu.ufape.alugafacil.models.UserSearchPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserSearchPreferenceRepository extends JpaRepository<UserSearchPreference, UUID> {

    @Query("SELECT p FROM UserSearchPreference p WHERE p.user.userId = :idUser")
    List<UserSearchPreference> findByIdUser(@Param("idUser")UUID idUser);

    @Query("SELECT p FROM UserSearchPreference p WHERE " +
           // 1. Filtro de Preço (Se user definiu max, imóvel deve ser menor ou igual)
           "(:price <= p.maxPriceInCents OR p.maxPriceInCents IS NULL) AND " +
           
           // 2. Filtros de Quantidade (Quartos, Banheiros, Vagas)
           "(:bedrooms >= p.minBedrooms OR p.minBedrooms IS NULL) AND " +
           "(:bathrooms >= p.minBathrooms OR p.minBathrooms IS NULL) AND " +
           // Nota: Assumindo que seu imóvel tem numero de vagas. Se for booleano 'garage', ajustamos a logica
           "(:garageCount >= p.garageCount OR p.garageCount IS NULL) AND " +

           // 3. Booleanos (Mobiliado, Pet Friendly) - Se user exige (true), imóvel TEM que ser true
           "(p.furnished = false OR p.furnished IS NULL OR :furnished = true) AND " +
           "(p.petFriendly = false OR p.petFriendly IS NULL OR :petFriendly = true) AND " +

           // 4. Localização Textual (Cidade e Bairro - Case Insensitive)
           "(:city IS NULL OR p.city IS NULL OR LOWER(p.city) = LOWER(:city)) AND " +
           "(:neighborhood IS NULL OR p.neighborhood IS NULL OR LOWER(p.neighborhood) = LOWER(:neighborhood)) AND " +

           // 5. Verificação de Token (Só pegamos quem tem token para notificar)
           "(p.user.fcmToken IS NOT NULL) AND " +

           // 6. Geolocalização (Haversine Formula Simplificada em JPQL)
           // Se o usuário definiu centro e raio, calculamos a distância
           "(p.searchCenter.latitude IS NULL OR p.searchCenter.longitude IS NULL OR p.searchRadiusInMeters IS NULL OR " +
           "(6371000 * acos(cos(radians(:lat)) * cos(radians(p.searchCenter.latitude)) * " +
           "cos(radians(p.searchCenter.longitude) - radians(:lon)) + " +
           "sin(radians(:lat)) * sin(radians(p.searchCenter.latitude)))) <= p.searchRadiusInMeters)"
    )
    List<UserSearchPreference> findMatchingPreferences(
        @Param("price") Integer price,
        @Param("bedrooms") Integer bedrooms,
        @Param("bathrooms") Integer bathrooms,
        @Param("garageCount") Integer garageCount,
        @Param("furnished") Boolean furnished,
        @Param("petFriendly") Boolean petFriendly,
        @Param("city") String city,
        @Param("neighborhood") String neighborhood,
        @Param("lat") Double lat,
        @Param("lon") Double lon
    );

}
