package br.edu.ufape.alugafacil.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ufape.alugafacil.models.Favorite;
import br.edu.ufape.alugafacil.models.Property;
import br.edu.ufape.alugafacil.models.User;
import br.edu.ufape.alugafacil.repositories.FavoriteRepository;
import br.edu.ufape.alugafacil.repositories.PropertyRepository;
import br.edu.ufape.alugafacil.repositories.UserRepository;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository, PropertyRepository propertyRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
    }

    public boolean checkIfFavorited(UUID userId, UUID propertyId) {
        return favoriteRepository.existsByUser_UserIdAndProperty_PropertyId(userId, propertyId);
    }

    @Transactional
    public boolean toggleFavorite(UUID userId, UUID propertyId) {
        Optional<Favorite> existingFavorite = favoriteRepository.findByUser_UserIdAndProperty_PropertyId(userId, propertyId);

        if (existingFavorite.isPresent()) {
            // Se já curtiu, então remove (Desfavorita)
            favoriteRepository.delete(existingFavorite.get());
            return false; // Retorna false indicando que não está mais favoritado
        } else {
            // Se não curtiu ainda, cria um novo (Favorita)
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
            Property property = propertyRepository.findById(propertyId)
                    .orElseThrow(() -> new RuntimeException("Imóvel não encontrado"));

            Favorite newFavorite = new Favorite();
            newFavorite.setUser(user);
            newFavorite.setProperty(property);
            
            favoriteRepository.save(newFavorite);
            return true; // Retorna true indicando que agora está favoritado
        }
    }

    public List<Favorite> getUserFavorites(UUID userId) {
        return favoriteRepository.findByUser_UserId(userId);
    }
}