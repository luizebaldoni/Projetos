package com.example.trabalho_final.Auxiliares;

public class Emprestimos extends Financeiro {
    private int parcelas; //~ Número de parcelas
    private double parcelaValor; //~ Valor de cada parcela

    public Emprestimos(String descricao, double montante, int parcelas) {
        super(descricao, montante);
        this.parcelas = parcelas;
        this.parcelaValor = montante / parcelas;
    }

    public int getParcelas() {
        return parcelas;
    }

    public double getParcelaValor() {
        return parcelaValor;
    }
}
