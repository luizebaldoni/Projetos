//* CLASSE PAI: FINANCEIRO
package com.example.trabalho_final.Auxiliares;

public abstract class Financeiro {
    private String descricao;
    private double montante;

    public Financeiro(String descricao, double montante) {
        this.descricao = descricao;
        this.montante = montante;
    }

    public String get_descricao() {
        return descricao;
    }

    public double get_montante() {
        return montante;
    }

    @Override
    public String toString() {
        return descricao + ": $" + String.format("%.2f", montante);
    }
}