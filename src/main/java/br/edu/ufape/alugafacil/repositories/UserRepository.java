package br.edu.ufape.alugafacil.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ufape.alugafacil.models.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT u FROM User u WHERE u.cpf =:cpf")
    Optional<User> findUserByCpf(@Param("cpf")String cpf);

    @Query("SELECT u FROM User u WHERE u.email =:email")
    User findByEmail(@Param("email")String email);

    @Query("SELECT u FROM User u WHERE u.agency.agencyId =:agencyId")
    List<User> findByAgencyId(UUID agencyId);
}
