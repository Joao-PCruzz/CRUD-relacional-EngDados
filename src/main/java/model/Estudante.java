package model;

import java.math.BigDecimal;
import jakarta.persistence.*; //Importante para a integração do ORM

@Entity
@Table(name = "Estudante", schema = "universidade")
public class Estudante {
    @Id
    @Column(name = "mat_estudante", length = 7)
    private String mat_estudante; //Faz parte do "universidade.matricula"
    @Column(name = "mc")
    private BigDecimal mc; //Para mapear o tipo DECIMAL(2)
    @Column(name = "ano_ingresso")
    private Integer ano_ingresso;

    // Relacionamento 1:1 com Usuario
    @OneToOne
    @JoinColumn(name = "cpf", referencedColumnName = "cpf")
    private Usuario usuario; 

    //Construtores
    public Estudante() {
    }
    public Estudante(String mat_estudante, BigDecimal mc, Integer ano_ingresso, Usuario usuario) {
        this.mat_estudante = mat_estudante;
        this.usuario = usuario;
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
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
                ", cpf=" + (usuario != null ? usuario.getCpf() : "null") +
                ", mc=" + mc +
                ", ano_ingresso=" + ano_ingresso +
                '}';
    }
}
