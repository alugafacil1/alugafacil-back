package br.edu.ufape.alugafacil.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufape.alugafacil.dtos.realStateAgency.RealStateAgencyRequest;
import br.edu.ufape.alugafacil.models.RealStateAgency;
import br.edu.ufape.alugafacil.repositories.RealStateAgencyRepository;

@Service
public class RealStateAgencyService  {
    
    @Autowired
    private RealStateAgencyRepository realStateAgencyRepository;

     public List<RealStateAgency> getAllRealStateAgencies() {
        return realStateAgencyRepository.findAll();
    }

    public RealStateAgency getRealStateAgencyById(String agencyId) {
        return realStateAgencyRepository.findById(agencyId).orElse(null);
    }

    public RealStateAgency createRealStateAgency(RealStateAgencyRequest realStateAgency) {
        RealStateAgency newRealStateAgency = new RealStateAgency();

        newRealStateAgency.setName(realStateAgency.getName());
        newRealStateAgency.setCorporateName(realStateAgency.getCorporateName());
        newRealStateAgency.setEmail(realStateAgency.getEmail());
        newRealStateAgency.setPhotoUrl(realStateAgency.getPhotoUrl());
        newRealStateAgency.setCnpj(realStateAgency.getCnpj());
        newRealStateAgency.setWebsite(realStateAgency.getWebsite());
        newRealStateAgency.setPhoneNumber(realStateAgency.getPhoneNumber());

        return realStateAgencyRepository.save(newRealStateAgency);
    }

    public RealStateAgency updateRealStateAgency(String agencyId, RealStateAgencyRequest realStateAgency) {
        RealStateAgency existingAgency = realStateAgencyRepository.findById(agencyId).orElse(null);
        
        if (existingAgency != null) {
            existingAgency.setName(realStateAgency.getName());
            existingAgency.setCorporateName(realStateAgency.getCorporateName());
            existingAgency.setEmail(realStateAgency.getEmail());
            existingAgency.setPhotoUrl(realStateAgency.getPhotoUrl());
            existingAgency.setCnpj(realStateAgency.getCnpj());
            existingAgency.setWebsite(realStateAgency.getWebsite());
            existingAgency.setPhoneNumber(realStateAgency.getPhoneNumber());

            return realStateAgencyRepository.save(existingAgency);
        }
        return null;
    }

    public void deleteRealStateAgency(String agencyId) {
        realStateAgencyRepository.deleteById(agencyId);
    }
}
