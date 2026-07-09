package model;
import java.time.LocalDate;
import jakarta.persistence.*;
import model.converters.StatusEstudanteConverter;
import model.enums.StatusEstudante;

@Entity
@Table(name = "Vinculo", schema = "universidade")
public class Vinculo {
    @Id
    @Column(name = "idVinculo")
    private Integer idVinculo;
    @Column(name = "data_entrada")
    private LocalDate data_entrada;
    @Convert(converter = StatusEstudanteConverter.class)
    @Column(name = "status")
    private StatusEstudante status; //Antes era String; agora reflete o domínio "status_estudante" do banco
    @Column(name = "data_saida")
    private LocalDate data_saida;

    // Muitos vínculos pertencem a um estudante
    @ManyToOne
    @JoinColumn(name = "mat_estudante", referencedColumnName = "mat_estudante")
    private Estudante estudante;

    // Muitos vínculos pertencem a um curso
    @ManyToOne
    @JoinColumn(name = "curso", referencedColumnName = "idCurso")
    private Curso curso;

    //construtores
    public Vinculo() {
    }
    public Vinculo(Integer idVinculo, Estudante estudante, Curso curso, LocalDate data_entrada, StatusEstudante status, LocalDate data_saida) {
        this.idVinculo = idVinculo;
        this.estudante = estudante;
        this.curso = curso;
        this.data_entrada = data_entrada;
        this.status = status;
        this.data_saida = data_saida;
    }

    //Metodo Getters and Setters
    public Integer getIdVinculo() {
        return idVinculo;
    }
    public void setIdVinculo(Integer idVinculo) {
        this.idVinculo = idVinculo;
    }
    public Estudante getEstudante() {
        return estudante;
    }
    public void setEstudante(Estudante estudante) {
        this.estudante = estudante;
    }
    public Curso getCurso() {
        return curso;
    }
    public void setCurso(Curso curso) {
        this.curso = curso;
    }
    public LocalDate getData_entrada() {
        return data_entrada;
    }
    public void setData_entrada(LocalDate data_entrada) {
        this.data_entrada = data_entrada;
    }
    public StatusEstudante getStatus() {
        return status;
    }
    public void setStatus(StatusEstudante status) {
        this.status = status;
    }
    public LocalDate getData_saida() {
        return data_saida;
    }
    public void setData_saida(LocalDate data_saida) {
        this.data_saida = data_saida;
    }

   @Override
    public String toString() {
        return "Vinculo{" +
                "idVinculo=" + idVinculo +
                ", mat_estudante='" + (estudante != null ? estudante.getMat_estudante() : "null") + '\'' +
                ", id_curso=" + (curso != null ? curso.getIdCurso() : "null") +
                ", data_entrada=" + data_entrada +
                ", status='" + status + '\'' +
                ", data_saida=" + data_saida +
                '}';
}
}
