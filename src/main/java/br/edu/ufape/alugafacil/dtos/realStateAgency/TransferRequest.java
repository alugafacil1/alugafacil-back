package br.edu.ufape.alugafacil.dtos.realStateAgency;

import java.util.UUID;

import lombok.Data;

@Data
public class TransferRequest {
    private UUID targetUserId;
}
