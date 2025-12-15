package br.edu.ufape.alugafacil.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ufape.alugafacil.models.RealStateAgency;

public interface RealStateAgencyRepository extends JpaRepository<RealStateAgency, UUID> {
    
}
