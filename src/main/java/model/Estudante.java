package model;

import java.math.BigDecimal;

public class Estudante {
    private String mat_estudante; //Faz parte do "universidade.matricula"
    private long cpf; //Faz parte do "tipo_cpf"
    private BigDecimal mc; //Para mapear o tipo DECIMAL(2)
    private Integer ano_ingresso;

    //Construtores
    public Estudante() {
    }
    public Estudante(String mat_estudante, long cpf, BigDecimal mc, Integer ano_ingresso) {
        this.mat_estudante = mat_estudante;
        this.cpf = cpf;
        this.mc = mc;
        this.ano_ingresso = ano_ingresso;
    }

    //Metodos Getters and Setters
    public String getMat_estudante() {
        return mat_estudante;
    }
    public void setMat_estudante(String mat_estudante) {
        this.mat_estudante = mat_estudante;
    }
    public long getCpf() {
        return cpf;
    }
    public void setCpf(long cpf) {
        this.cpf = cpf;
    }
    public BigDecimal getMc() {
        return mc;
    }
    public void setMc(BigDecimal mc) {
        this.mc = mc;
    }
    public Integer getAno_ingresso() {
        return ano_ingresso;
    }
    public void setAno_ingresso(Integer ano_ingresso) {
        this.ano_ingresso = ano_ingresso;
    }

    @Override
    public String toString() {
        return "Estudante{" +
                "mat_estudante='" + mat_estudante + '\'' +
                ", cpf=" + cpf +
                ", mc=" + mc +
                ", ano_ingresso=" + ano_ingresso +
                '}';
    }
}
