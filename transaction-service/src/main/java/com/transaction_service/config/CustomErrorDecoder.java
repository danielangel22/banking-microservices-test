package com.transaction_service.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        String responseBody = null;

        try {
            if (response.body() != null) {
                // Leer el cuerpo del InputStream sin usar IOUtils
                try (InputStream inputStream = response.body().asInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                    responseBody = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                }
            }
        } catch (Exception e) {
            responseBody = "No se pudo leer el cuerpo de la respuesta.";
        }

        HttpStatus status = HttpStatus.resolve(response.status());

        if (status != null) {
            switch (status) {
                case NOT_FOUND:
                    log.error("Recurso no encontrado: {}", responseBody);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, responseBody);
                case BAD_REQUEST:
                    log.error("Solicitud inválida: {}", responseBody);
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, responseBody);
                case INTERNAL_SERVER_ERROR:
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, responseBody);
                default:
                    log.error("Error desconocido: {}", responseBody);
                    return new ResponseStatusException(status, responseBody);
            }
        }

        return new Exception("Error desconocido en el cliente Feign: " + responseBody);
    }
}

