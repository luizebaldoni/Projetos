package com.example.trabalho_final.Auxiliares;

public class Impostos extends Financeiro {
    private String tipoImposto; // Exemplo: Imposto de Renda, IPVA, etc.

    public Impostos(String descricao, double montante, String tipoImposto) {
        super(descricao, montante);
        this.tipoImposto = tipoImposto;
    }

    public String getTipoImposto() {
        return tipoImposto;
    }
}