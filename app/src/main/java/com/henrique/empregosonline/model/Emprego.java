package com.henrique.empregosonline.model;

import java.io.Serializable;

public class Emprego implements Serializable {

    private int vagaid;
    private String descricao;
    private int horasSemana;
    private double valor;

    public Emprego() {
    }

    public int getVagaid() {
        return vagaid;
    }

    public void setVagaid(int vagaid) {
        this.vagaid = vagaid;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getHorasSemana() {
        return horasSemana;
    }

    public void setHorasSemana(int horasSemana) {
        this.horasSemana = horasSemana;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Numero da vaga: "+ this.vagaid + "  Descrição:" + this.descricao + " Horas por Semana: " + this.horasSemana + " Valor: " + this.valor;
    }
}
