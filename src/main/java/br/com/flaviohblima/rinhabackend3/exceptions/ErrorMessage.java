package br.com.flaviohblima.rinhabackend3.exceptions;

import java.time.LocalDateTime;

public record ErrorMessage(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path) {
}
