package com.example.trabalho_final.Auxiliares;

public class Investimentos extends Financeiro {
    private double taxaJuros; // Taxa de juros ou retorno esperado

    public Investimentos(String descricao, double montante, double taxaJuros) {
        super(descricao, montante);
        this.taxaJuros = taxaJuros;
    }

    public double getTaxaJuros() {
        return taxaJuros;
    }
}
