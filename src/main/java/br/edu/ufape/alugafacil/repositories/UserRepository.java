package br.edu.ufape.alugafacil.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ufape.alugafacil.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u WHERE u.cpf =:cpf")
    Optional<User> findUserByCpf(@Param("cpf")String cpf);
}
