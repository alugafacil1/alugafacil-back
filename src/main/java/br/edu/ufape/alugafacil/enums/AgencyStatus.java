package br.edu.ufape.alugafacil.enums;

public enum AgencyStatus {
    ACTIVE,     // Pagamento em dia, tudo funcionando
    BLOCKED,    // Bloqueada por infração ou moderação do Admin
    INACTIVE,   // Assinatura vencida / cancelada
    PENDING     // (Opcional) Aguardando Admin validar o CNPJ
}