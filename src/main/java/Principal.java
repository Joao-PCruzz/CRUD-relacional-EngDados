import java.sql.*;
//import java.time.LocalDate;
import conexao.ConnectionFactory;
import dao.UsuarioDAO;
//import model.Usuario;

public class Principal {
    //Teste simples de conexao com a database
    public static void main(String[] args) {
        System.out.println("Iniciando o teste de conexão com a AWS...");

        // O bloco try-with-resources abre a conexão e garante que ela será fechada automaticamente
        try (Connection conexao = ConnectionFactory.getConnection()) {
            if (conexao != null && !conexao.isClosed()) {
                System.out.println("Conexão com a AWS realizada com sucesso!");

                System.out.println("Iniciando opreação de vizualização.");
                UsuarioDAO dao = new UsuarioDAO();
                dao.consultaCompleta();
            }
        } catch (SQLException e) {
            System.err.println("Não foi possível conectar ao banco de dados.");
            e.printStackTrace();//Imprime detalhadamente o erro de execução ocorrido
        }
    }
}
