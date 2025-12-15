package br.edu.ufape.alugafacil.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import br.edu.ufape.alugafacil.models.Property;


@Repository
public interface PropertyRepository extends 
		JpaRepository<Property, UUID>,
		QuerydslPredicateExecutor<Property> {
	List<Property> findByUserUserId(UUID userId);
}
