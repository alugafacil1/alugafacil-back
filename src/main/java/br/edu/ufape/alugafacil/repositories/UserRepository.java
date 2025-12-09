package br.edu.ufape.alugafacil.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ufape.alugafacil.models.User;

public interface UserRepository extends JpaRepository<User, String> {

    User findUserByCpf(@Param());
}
