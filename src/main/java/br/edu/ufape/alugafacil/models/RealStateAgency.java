package br.edu.ufape.alugafacil.models;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class RealStateAgency {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID agencyId;

    private String name;
    private String corporateName;
    private String email;
    private String photoUrl;
    private String cnpj;
    private String website;
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
