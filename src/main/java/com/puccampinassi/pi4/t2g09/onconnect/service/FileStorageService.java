//Flavio

package com.puccampinassi.pi4.t2g09.onconnect.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {

    private final Path uploadDir = Paths.get("uploads");

    public String salvarImagem(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            return null;
        }

        try {
            // garante que a pasta exista
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String originalName = arquivo.getOriginalFilename();
            String ext = "";

            if (originalName != null && originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf("."));
            }

            String nomeArquivo = UUID.randomUUID() + ext;
            Path destino = uploadDir.resolve(nomeArquivo);

            Files.copy(arquivo.getInputStream(), destino);

            // URL pública que será usada no front
            return "/uploads/" + nomeArquivo;

        } catch (IOException e) {
            log.error("Erro ao salvar arquivo de imagem", e);
            throw new RuntimeException("Erro ao salvar arquivo de imagem", e);
        }
    }
}
