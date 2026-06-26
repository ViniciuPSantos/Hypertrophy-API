package com.hypertrophy.hypertrophy_api.dto.common;

public record ApiResponse<T>(
        boolean sucesso,
        String mensagem,
        T dados
) {
    public static <T> ApiResponse<T> ok(T dados) {
        return new ApiResponse<>(true, null, dados);
    }

    public static <T> ApiResponse<T> ok(String mensagem, T dados) {
        return new ApiResponse<>(true, mensagem, dados);
    }

    public static <T> ApiResponse<T> erro(String mensagem) {
        return new ApiResponse<>(false, mensagem, null);
    }
}
