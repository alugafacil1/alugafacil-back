package br.edu.ufape.alugafacil.services.interfaces;

import java.util.Map;

public interface IFCMService {
    void sendNotification(String targetToken, String title, String body, Map<String, String> data);
}
