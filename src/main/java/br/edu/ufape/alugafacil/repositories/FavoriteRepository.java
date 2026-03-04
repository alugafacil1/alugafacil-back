package br.edu.ufape.alugafacil.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufape.alugafacil.models.Favorite;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {
    
    // Busca todos os favoritos de um usuário
    List<Favorite> findByUser_UserId(UUID userId);
    
    // Verifica se um imóvel específico já foi favoritado por um usuário específico
    Optional<Favorite> findByUser_UserIdAndProperty_PropertyId(UUID userId, UUID propertyId);
    
    boolean existsByUser_UserIdAndProperty_PropertyId(UUID userId, UUID propertyId);
}