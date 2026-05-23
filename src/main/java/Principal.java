import java.sql.*;
import conexao.ConnectionFactory;

public class Principal {
    //Teste simples de conexao com a database
    public static void main(String[] args) {
        System.out.println("Iniciando o teste de conexão com a AWS...");

        // O bloco try-with-resources abre a conexão e garante que ela será fechada automaticamente
        try (Connection conexao = ConnectionFactory.getConnection()) {
            if (conexao != null && !conexao.isClosed()) {
                System.out.println("Conexão com a AWS realizada com sucesso!");
            }
        } catch (SQLException e) {
            System.err.println("Não foi possível conectar ao banco de dados.");
            e.printStackTrace();//Imprime detalhadamente o erro de execução ocorrido
        }
    }
}
