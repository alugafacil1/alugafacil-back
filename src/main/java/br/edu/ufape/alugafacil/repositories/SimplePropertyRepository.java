package br.edu.ufape.alugafacil.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufape.alugafacil.models.SimpleProperty;

@Repository
public interface SimplePropertyRepository extends JpaRepository<SimpleProperty, UUID> {
}
