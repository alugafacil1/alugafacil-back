package br.edu.ufape.alugafacil.models;

import jakarta.persistence.*;
import java.util.UUID;

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
