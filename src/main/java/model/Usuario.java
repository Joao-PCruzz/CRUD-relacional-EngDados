package model;//O pacote model é responsável pro facilitar o projeto como um todo.
//Ao invés de quando precisar fazer um insert passar tudo, passa somente o usuario e acabou, evitando quebrar todo o código e criando praticidade
import java.time.LocalDate;
import java.util.List;

public class Usuario {
    private long cpf; //NUMERIC(13) em Java vira long
    private String nome;
    private LocalDate data_nascimento; //Date vira Local Date
    private List<String> email;
    private List<String> telefone;
    private String login;
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
