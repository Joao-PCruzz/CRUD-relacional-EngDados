package model;

import jakarta.persistence.*; //Importante para a integração do ORM
import model.converters.TipoGrauConverter;
import model.converters.TipoNivelConverter;
import model.converters.TipoTurnoConverter;
import model.enums.TipoGrau;
import model.enums.TipoNivel;
import model.enums.TipoTurno;

@Entity
@Table(name = "Curso", schema = "universidade")
public class Curso{

    @Id
    @Column(name = "idCurso")
    private Integer idCurso;
    @Column(name = "nome", unique = true)
    private String nome;
    @Convert(converter = TipoGrauConverter.class)
    @Column(name = "grau")
    private TipoGrau grau; // Reflete o domínio "tipo_grau" do banco
    @Convert(converter = TipoTurnoConverter.class)
    @Column(name = "turno", unique = true)
    private TipoTurno turno; // Reflete o domínio "tipo_turno" do banco
    @Column(name = "campus", unique = true)
    private String campus;
    @Convert(converter = TipoNivelConverter.class)
    @Column(name = "nivel", unique = true)
    private TipoNivel nivel; // Reflete o domínio "tipo_nivel" do banco

    //Constrututores
    public Curso() {
    }
    public Curso(Integer idCurso, String nome, TipoGrau grau, TipoTurno turno, String campus, TipoNivel nivel) {
        this.idCurso = idCurso;
        this.nome = nome;
        this.grau = grau;
        this.turno = turno;
        this.campus = campus;
        this.nivel = nivel;
    }

    //Metodos Getters and Setters
    public Integer getIdCurso() {
        return idCurso;
    }
    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public TipoGrau getGrau() {
        return grau;
    }
    public void setGrau(TipoGrau grau) {
        this.grau = grau;
    }
    public TipoTurno getTurno() {
        return turno;
    }
    public void setTurno(TipoTurno turno) {
        this.turno = turno;
    }
    public String getCampus() {
        return campus;
    }
    public void setCampus(String campus) {
        this.campus = campus;
    }
    public TipoNivel getNivel() {
        return nivel;
    }
    public void setNivel(TipoNivel nível) {
        this.nivel = nível;
    }

    @Override
    public String toString() {
        return "Curso{" +
                "idCurso=" + idCurso +
                ", nome='" + nome + '\'' +
                ", grau='" + grau + '\'' +
                ", turno='" + turno + '\'' +
                ", campus='" + campus + '\'' +
                ", nivel='" + nivel + '\'' +
                '}';
    }
}