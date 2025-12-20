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

}
