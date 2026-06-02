package model;//O pacote model é responsável pro facilitar o projeto como um todo.
//Ao invés de quando precisar fazer um insert passar tudo, passa somente o usuario e acabou, evitando quebrar todo o código e criando praticidade
import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*; //É responsável por implementar a especificação padrão para a ORM

@Entity //Diz ao JPA que esta classe é uma entidade que representa uma tabela no banco
@Table(name = "Usuario") // Define o nome exato da tabela no PostgreSQL
public class Usuario {

    @Id // Define que este atributo é a Chave Primária (Primary Key) da tabela
    @Column(name = "cpf")
    private long cpf; //NUMERIC(13) em Java vira long
    @Column(name="nome", length=45)
    private String nome;
    @Column(name="data_nascimento")
    private LocalDate data_nascimento; //Date vira Local Date

    @JdbcTypeCode(SqlTypes.JSON) // Diz ao Hibernate para ler/gravar como JSON no Postgres
    @Column(name = "email", columnDefinition = "jsonb") // jsonb é a versão otimizada de JSON no Postgres
    private List<String> email;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "telefone", columnDefinition = "jsonb")
    private List<String> telefone;
    @Column(name = "login", unique = true, length = 45)
    private String login;
    @Column(name = "senha", length = 32)
    private String senha;

    //Construtores
    public Usuario(){

    }
    public Usuario(long cpf, String nome, LocalDate data_nascimento, List<String> email, List<String> telefone, String login, String senha) {
        this.cpf = cpf;
        this.nome = nome;
        this.data_nascimento = data_nascimento;
        this.email = email;
        this.telefone = telefone;
        this.login = login;
        this.senha = senha;
    }

    //Metodos Getters e Setters
    public long getCpf() {
        return cpf;
    }
    public void setCpf(long cpf) {
        this.cpf = cpf;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public LocalDate getData_nascimento() {
        return data_nascimento;
    }
    public void setData_nascimento(LocalDate data_nascimento) {
        this.data_nascimento = data_nascimento;
    }
    public void setEmail(List<String> email) {
        this.email = email;
    }
    public List<String> getEmail() {
        return email;
    }
    public void setTelefone(List<String> telefone) {
        this.telefone = telefone;
    }
    public List<String> getTelefone() {
        return telefone;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString(){
        return "Usuario [CPF=" + cpf + ", Nome=" + nome + ", Login=" + login + "]";
    }
}
