import java.sql.*;
import java.time.LocalDate;

//import java.time.LocalDate;
import conexao.ConnectionFactory;
import dao.UsuarioDAO;
import model.Usuario;

public class Principal {
    //Teste simples de conexao com a database
    public static void main(String[] args) {
        System.out.println("Iniciando o teste de conexão com a AWS...");

        // O bloco try-with-resources abre a conexão e garante que ela será fechada automaticamente
        try (Connection conexao = ConnectionFactory.getConnection()) {
            if (conexao != null && !conexao.isClosed()) {
                System.out.println("Conexão com a AWS realizada com sucesso!");

                System.out.println("Iniciando opreação de vizualização...");
                UsuarioDAO dao = new UsuarioDAO();
                dao.consultarUsuarios();
                System.out.println("Realizando a operação de Update...");
                //É necessário fazer uma ligação dos Usuarios do model com o banco de dados se possível, entrtanto a operação está correta
                Usuario testeUsuario = new Usuario(11111111100L, "Prof XX", LocalDate.of(1980, 03, 05), "profA@email.com", "99998888,88889999", "profa", "senha1");
                dao.atualizarUsuario(testeUsuario, 123123123L);
                System.out.println("Vizualizando novamente.");
                dao.consultarUsuarios();
            }

        } catch (SQLException e) {
            System.err.println("Não foi possível conectar ao banco de dados.");
            e.printStackTrace();//Imprime detalhadamente o erro de execução ocorrido
        }
    }
}
