package com.example.trabalho_final.Auxiliares;

public class MetasPoupanca extends Financeiro {
    private double objetivo; // Meta de poupança

    public MetasPoupanca(String descricao, double montante, double objetivo) {
        super(descricao, montante);
        this.objetivo = objetivo;
    }

    public double getObjetivo() {
        return objetivo;
    }
}

