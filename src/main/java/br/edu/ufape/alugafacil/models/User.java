package br.edu.ufape.alugafacil.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;

import br.edu.ufape.alugafacil.enums.UserType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    private String name;
    
    @Column(unique = true)
    private String email;
    
    private String photoUrl;
    private String cpf;
    private String creciNumber; // Para corretores [cite: 88]
    private String passwordHash;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private RealStateAgency agency;

    @OneToMany(mappedBy = "user")
    private List<Property> properties; // Imóveis do proprietário

    @OneToMany(mappedBy = "user")
    private List<Subscription> subscriptions;
}
