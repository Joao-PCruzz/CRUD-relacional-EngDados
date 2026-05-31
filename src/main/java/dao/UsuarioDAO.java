package dao; //Esse package é responsável por implementar todas as funções do CRUD separadamente
//Separando pelas tabelas que devem ser utilizadas
import java.sql.*;
import java.time.LocalDate;

import conexao.ConnectionFactory;

public class UsuarioDAO {

    //Método para CRIAR
    public void inserirUsuario() throws SQLException {
        
    }

    //Método para LER
    public void consultaCompleta() throws SQLException {
        String sql = "SELECT * FROM universidade.usuario";

        //Cria a conexao e prepara o comando pro meio do prepared statment
        try(Connection conexao = ConnectionFactory.getConnection(); 
        PreparedStatement comando = conexao.prepareStatement(sql); 
        ResultSet resultado = comando.executeQuery()){

        System.out.println("Executar consulta: " + sql);
        System.out.printf("%-15s | %-20s | %-15s | %-15s | %-15s%n", "CPF", "NOME", "DATA NASC.", "LOGIN", "SENHA");

        // Percorre cada linha retornada da tabela na AWS
        while (resultado.next()) {
            Long cpf = resultado.getLong("cpf"); 
            String nome = resultado.getString("nome");
            // Busca o DATE do SQL diretamente como LocalDate do Java
            LocalDate dataNascimento = resultado.getObject("data_nascimento", LocalDate.class);
            String login = resultado.getString("login");
            String senha = resultado.getString("senha");
            
            // Exibe os dados formatados em colunas no terminal
            System.out.printf("%-15d | %-20s | %-15s | %-15s | %-15s%n", 
                    cpf, nome, dataNascimento, login, senha);
        }
        System.out.println("Comando executado com sucesso.");
        System.out.println();

        } catch (SQLException e) {
            System.out.println("Não foi possível consultar a tabela");
            e.printStackTrace();
        }
        //Ao chegar aqui, o java fecha a execução e o comando automaticamente
    }
    
    //método para ATUALIZAR
    //Método para DELETARss
}
