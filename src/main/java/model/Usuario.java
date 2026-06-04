package model;// O pacote model é responsável pro facilitar o projeto como um todo.
// Ao invés de quando precisar fazer um insert passar tudo, passa somente o usuario e acabou, evitando quebrar todo o código e criando praticidade
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*; // É responsável por implementar a especificação padrão para a ORM

@Entity // Diz ao JPA que esta classe é uma entidade que representa uma tabela no banco
@Table(name = "Usuario", schema = "universidade") // Define o nome exato da tabela no PostgreSQL e também o schema em que está
public class Usuario {

    @Id // Define que este atributo é a Chave Primária (Primary Key) da tabela
    @Column(name = "cpf", columnDefinition = "universidade.tipo_cpf") // Fala sobre o nome da coluna e o seu tipo
    private long cpf; // NUMERIC(13) em Java vira long
    @Column(name="nome", length=45) // Length define o tamanho máximo da String
    private String nome;
    @Column(name="data_nascimento")
    private LocalDate data_nascimento; // Date vira Local Date

    // O Hibernate 6 mapeia List<String> diretamente para VARCHAR[] no Postgres
    @Column(name = "email")
    private List<String> email;
    @Column(name = "telefone")
    private List<String> telefone;
    @Column(name = "login", unique = true, length = 45) //Unique é para definir o login como único
    private String login;
    @Column(name = "senha", length = 32)
    private String senha;

    //Construtores (talevz seja necessária e criação de mais)
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
    public String toString() {
        // Junta os elementos da lista em uma única String separada por vírgula para exibição, pois está em formato List
        String emailsStr = (email != null) ? String.join(", ", email) : "";
        String fonesStr = (telefone != null) ? String.join(", ", telefone) : "";

        // Corta os textos se forem grandes demais para não quebrar o alinhamento da tela
        if (emailsStr.length() > 30) emailsStr = emailsStr.substring(0, 27) + "...";
        if (fonesStr.length() > 20) fonesStr = fonesStr.substring(0, 17) + "...";
        String nomeCurto = (nome != null && nome.length() > 20) ? nome.substring(0, 17) + "..." : nome;

        // Retorna uma linha alinhada com espaçamentos fixos (%-20s = 20 caracteres alinhados à esquerda)
        return String.format("| %-12d | %-20s | %-12s | %-30s | %-20s | %-10s |", 
                cpf, nomeCurto, data_nascimento, emailsStr, fonesStr, login);
    }
}
