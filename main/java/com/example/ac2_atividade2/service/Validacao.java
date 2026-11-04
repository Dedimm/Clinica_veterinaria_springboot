package com.example.ac2_atividade2.service;

import com.example.ac2_atividade2.exception.RegraNegocioException;

final class Validacao {
    private Validacao() {}

    static String texto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new RegraNegocioException("O campo '" + campo + "' é obrigatório.");
        }
        return valor.trim();
    }

    static <T> T obrigatorio(T valor, String campo) {
        if (valor == null) {
            throw new RegraNegocioException("O campo '" + campo + "' é obrigatório.");
        }
        return valor;
    }

    static String normalizar(String valor) {
        return valor.trim().toLowerCase();
    }
}
