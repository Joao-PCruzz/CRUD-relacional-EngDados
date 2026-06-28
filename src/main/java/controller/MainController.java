package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane mainPane;

    // Método genérico para trocar o conteúdo do centro
    private void trocarTela(String fxmlName) {
        try {
            String caminhoCompleto = "/gui/" + fxmlName;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoCompleto));
            Parent novaTela = loader.load();

            mainPane.setCenter(novaTela);
        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela: " + fxmlName);
            e.printStackTrace();
        }
    }

    @FXML
    private void carregarEstudantes() {
        trocarTela("EstudantesView.fxml");
    }

    @FXML
    private void carregarCursos() {
        trocarTela("CursosView.fxml");
    }

    @FXML
    private void carregarUsuarios() {
        trocarTela("UsuariosView.fxml");
    }

    @FXML
    private void carregarVinculos() {
        trocarTela("VinculosView.fxml");
    }
}
