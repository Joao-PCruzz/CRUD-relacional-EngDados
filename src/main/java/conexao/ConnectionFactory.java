package conexao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class ConnectionFactory {
    //É executado somente uma vez sempre que a JVM lê a classe
    static {
        try {
            // Carrega o driver do Postgres
            Class.forName("org.postgresql.Driver"); 
        } catch (ClassNotFoundException e) {
            System.err.println("Driver do PostgreSQL não encontrado!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        //FileInputStream está sendo usado para ler o arquivo config.properties
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo config.properties.");
            throw new SQLException(e); //joga tudo na exeção SQL que é necessária
        }
        String url = "jdbc:postgresql://" + props.getProperty("db.host") + ":" 
                     + props.getProperty("db.port") + "/" + props.getProperty("db.name");
        //Retorna a conexao criada
        return DriverManager.getConnection(url, props.getProperty("db.user"), props.getProperty("db.password"));
    }
}
