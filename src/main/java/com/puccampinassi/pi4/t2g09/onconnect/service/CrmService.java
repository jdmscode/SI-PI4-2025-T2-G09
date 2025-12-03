package com.puccampinassi.pi4.t2g09.onconnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.text.Normalizer;

@Service
public class CrmService {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://www.consultacrm.com.br/api/index.php")
            .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
            .defaultHeader("Accept", "application/json")
            .defaultHeader("Referer", "https://www.consultacrm.com.br/")
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Valida CRM, nome do profissional e status ativo (se disponível)
     * @param crm número do CRM
     * @param nome nome completo do profissional
     * @return true se o CRM existe e o nome confere
     */
    public boolean validarCrm(String crm, String nome) {
        try {
            Mono<String> responseMono = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("tipo", "crm")
                            .queryParam("uf", "")
                            .queryParam("q", crm)
                            .queryParam("destino", "json")
                            .queryParam("chave", "57a73dfde990483")
                            .build())
                    .retrieve()
                    .bodyToMono(String.class);

            String responseBody = responseMono.block();

            if (responseBody == null || responseBody.isEmpty()) {
                System.out.println("Resposta vazia do CRM");
                return false;
            }

            JsonNode root = objectMapper.readTree(responseBody);

            if (!root.has("item") || !root.get("item").isArray()) {
                System.out.println("JSON inesperado: 'item' não encontrado ou não é array");
                return false;
            }

            for (JsonNode prof : root.get("item")) {
                String profCrm = prof.has("numero") ? prof.get("numero").asText() : "";
                String profNome = prof.has("nome") ? prof.get("nome").asText() : "";
                String profSituacao = prof.has("situacao") ? prof.get("situacao").asText() : "";

                
                if (crm.equals(profCrm) && nomesIguais(nome, profNome)) {
                    return true;
                }
            }

            return false;

        } catch (WebClientResponseException e) {
            System.err.println("Erro na chamada API CRM: " + e.getRawStatusCode());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Normaliza nome para comparação: remove acentos, quebras de linha e ignora maiúsculas/minúsculas
     */
    private boolean nomesIguais(String nome1, String nome2) {
        if (nome1 == null || nome2 == null) return false;

        String n1 = Normalizer.normalize(nome1, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase();

        String n2 = Normalizer.normalize(nome2, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase();

        return n1.equals(n2);
    }
}
