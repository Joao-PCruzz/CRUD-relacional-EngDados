package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SistemaAcademico extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/Janela.fxml"));

            Parent root = loader.load();

            Scene scene = new Scene(root, 1050, 650);

            primaryStage.setTitle("Sistema Acadêmico - Ufs");
            primaryStage.setScene(scene);

            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Erro: Não foi possível carregar o arquivo FXML da interface");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
