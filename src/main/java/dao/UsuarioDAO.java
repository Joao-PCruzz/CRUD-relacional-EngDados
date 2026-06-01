package dao; //Esse package é responsável por implementar todas as funções do CRUD separadamente
//Separando pelas tabelas que devem ser utilizadas
import java.sql.*;
import java.time.LocalDate;
import model.Usuario;
import conexao.ConnectionFactory;

public class UsuarioDAO {

    //Método para CRIAR
    public void inserirUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO universidade.usuario( cpf, nome, data_nascimento, email, telefone, login, senha) VALUES (?, ?, ?, ?, ?, ?, ?);";

        try(Connection conexao = ConnectionFactory.getConnection();
            PreparedStatement comando = conexao.prepareStatement(sql)){
                System.out.println("Realizando inserção...");

                //Comando para substituir os valores "?" pelos valores reais
                comando.setLong(1, usuario.getCpf());
                comando.setString(2, usuario.getNome());
                comando.setDate(3, java.sql.Date.valueOf(usuario.getData_nascimento())); // Converte LocalDate para o tipo Date do SQL
                //Primeiro é necessário converter o email e telefone em array para ser possível passar para a mascara '?'
                //O método createArrayOf converte a lista para um array nativo em SQL
                Array arrayEmail = conexao.createArrayOf("VARCHAR", usuario.getEmail().toArray());
                comando.setArray(4, arrayEmail);
                Array arrayTelefone = conexao.createArrayOf("VARCHAR", usuario.getTelefone().toArray());
                comando.setArray(5, arrayTelefone);
                
                comando.setString(6, usuario.getLogin());
                comando.setString(7, usuario.getSenha());

                //Execução da Query diretamente para a núvem da AWS
                comando.executeUpdate();
                System.out.println("Usuario inserido com sucesso!");

        } catch(SQLException e) {
            System.out.println("Nao foi possivel inserir o Usuario.");
            e.printStackTrace();
        }
        //Ao chegar aqui, o java fecha a execução e o comando automaticamente.
    }

    //Método para LER
    public void consultarUsuarios() throws SQLException {
        String sql = "SELECT * FROM universidade.usuario;";

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
    
    //Método para ATUALIZAR
    public void atualizarUsuario(Usuario usuario, Long novoCPF){
        //Esse "SQL 1" é um tipo de retorno, ele busca o cpf específico, se encontrar retorna 1, sendo extremamente rápido
        String sqlVerifica = "SELECT 1 FROM universidade.usuario WHERE cpf = ?;";
        String sqlUpdate = "UPDATE universidade.usuario SET cpf = ? WHERE cpf = ?;";

        try (Connection conexao = ConnectionFactory.getConnection()) {
            
            // Verificando se o novo CPF já está cadastrado no banco
            try (PreparedStatement comandoVerifica = conexao.prepareStatement(sqlVerifica)) {
                comandoVerifica.setLong(1, novoCPF);
                try (ResultSet result = comandoVerifica.executeQuery()) {
                    //isso é feito pois o result começa em uma linha "Fantasma" onde não há registros, ou seja, na posição 0
                    //Ao ir para a linah 1, ele entra na tabela de fato, e verifica se o cpf foi encontrado ou não
                    if (result.next()) {
                        // Se o result.next() retornou true, significa que o CPF já existe
                        System.out.println("Erro: O CPF " + novoCPF + " já está cadastrado para outro usuário.");
                        return; // Encerra o método aqui e impede o UPDATE
                    }
                }
            }

            //Se passou pelo teste acima, o CPF está livre.
            try (PreparedStatement comandoUpdate = conexao.prepareStatement(sqlUpdate)) {
                //Substituição dos valores de UPDATE no comando sql
                comandoUpdate.setLong(1, novoCPF);           
                comandoUpdate.setLong(2, usuario.getCpf());  
                
                //Variável instanciada pois no SLQ comandos podem ser considerados válidos sem atualizar nada, essa variável permite ter controle sobre isso
                int linhasAfetadas = comandoUpdate.executeUpdate();
                
                if (linhasAfetadas > 0) {
                    System.out.println("CPF atualizado com sucesso!");
                    usuario.setCpf(novoCPF); // Atualiza o objeto na memória também
                } else {
                    System.out.println("Aviso: Nenhum usuário encontrado com o CPF atual: " + usuario.getCpf());
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro de banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Método para DELETAR
    public void deletarUsuario(Long cpf){
        //Mesmo tipo de consulta relizada acima, pois a lógica é praticamente a mesma
        String sqlDelete = "DELETE FROM universidade.usuario WHERE cpf = ?;";

        try(Connection conexao = ConnectionFactory.getConnection(); 
            PreparedStatement comando = conexao.prepareStatement(sqlDelete)){
                //Substitui a interrogação pelo CFP em sí
                comando.setLong(1, cpf);

                //Executa update retorna a quantidade de linhas apagadas no banco
                int linhasAfetadas = comando.executeUpdate();

                if(linhasAfetadas > 0){
                    System.out.println("CPF encontrado, usuario deletado!");
                } else{
                    System.out.println("O CPF inserido nao esta no sistema");
                }

        }catch(SQLException e){
            System.out.println("Nao foi possivel fazer a operação de deletar.");
            e.printStackTrace();
        }
    }
}

