package controller;

import dao.EstudanteDAO;
import dao.UsuarioDAO;
import javafx.scene.control.*;
import model.Estudante;
import model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.math.BigDecimal;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class EstudantesController {
    //ATTRIBUTES
    //Formulário de cadastro
    @FXML private TextField txtMatricula;
    @FXML private TextField txtMc;
    @FXML private TextField txtAnoIngresso;
    @FXML private ComboBox<Usuario> cbUsuario;

    //Tabela de estudantes
    @FXML private TableView<Estudante> tvEstudantes;
    @FXML private TableColumn<Estudante, String> colMatricula;
    @FXML private TableColumn<Estudante, BigDecimal> colMc;
    @FXML private TableColumn<Estudante, Integer> colAnoIngresso;
    @FXML private TableColumn<Estudante, Usuario> colUsuario;

    //Barra de pesquisa
    @FXML private TextField txtPesquisa;

    //Botões
    @FXML private Button btnSalvar;
    @FXML private Button btnAtualizar;
    @FXML private Button btnPesquisar;
    @FXML private Button btnDeletar;

    private EstudanteDAO estudanteDAO;
    private UsuarioDAO usuarioDAO;

    //METHODS
    //Carregar dados iniciais
    @FXML
    public void initialize() {
        Map<String, Object> overrides = new HashMap<>();
        overrides.put("jakarta.persistence.jdbc.url", System.getenv("DB_URL"));
        overrides.put("jakarta.persistence.jdbc.user", System.getenv("DB_USER"));
        overrides.put("jakarta.persistence.jdbc.password", System.getenv("DB_PASSWORD"));

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("universidade-pu", overrides);

        this.estudanteDAO = new EstudanteDAO(emf);
        this.usuarioDAO = new UsuarioDAO(emf);

        colMatricula.setCellValueFactory(new PropertyValueFactory<>("mat_estudante"));
        colMc.setCellValueFactory(new PropertyValueFactory<>("mc"));
        colAnoIngresso.setCellValueFactory(new PropertyValueFactory<>("ano_ingresso"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));

        atualizarTabela();
        carregarComboBoxUsuarios();

        tvEstudantes.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                preencherFormulario(selecionado);
            }
        });
    }

    //Ação ao clicar no botão salvar
    @FXML
    private void handleSalvarEstudante() {
        try {
            Estudante estudante = new Estudante();
            estudante.setMat_estudante(txtMatricula.getText());
            estudante.setMc(new BigDecimal(txtMc.getText()));
            estudante.setAno_ingresso(Integer.parseInt(txtAnoIngresso.getText()));
            estudante.setUsuario(cbUsuario.getValue());

            estudanteDAO.inserirEstudante(estudante);

            limparFormulario();
            atualizarTabela();
        } catch (Exception e) {
            System.err.println("Erro ao salvar estudante: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Ação ao clicar no botão atualizar
    @FXML
    private void handleAtualizarEstudante() {
        try {
            Estudante estudante = new Estudante();
            estudante.setMat_estudante(txtMatricula.getText());
            estudante.setMc(new BigDecimal(txtMc.getText()));
            estudante.setAno_ingresso(Integer.parseInt(txtAnoIngresso.getText()));
            estudante.setUsuario(cbUsuario.getValue());

            estudanteDAO.atualizarEstudante(estudante);

            limparFormulario();
            atualizarTabela();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Ação ao clicar no botão deletar
    @FXML
    private void handleDeletarEstudante() {
        String matricula = txtMatricula.getText();
        if (matricula != null && !matricula.isEmpty()) {
            estudanteDAO.deletarEstudante(matricula);
            limparFormulario();
            atualizarTabela();
        }
    }

    //Ação ao clicar no botão pesquisar
    @FXML
    private void handlePesquisarEstudante() {
        String busca = txtPesquisa.getText();
        if (busca == null || busca.isEmpty()) {
            atualizarTabela();
        } else {
            // Se buscou algo, filtra por matrícula
            Estudante e = estudanteDAO.buscarPorMatricula(busca);
            if (e != null) {
                tvEstudantes.getItems().setAll(e);
            } else {
                tvEstudantes.getItems().clear(); // Nulo se não achar nenhum
            }
        }
    }

//    @FXML
//    private void handleLimpar() {
//        limparFormulario();
//    }

    //---MÉTODOS DE COLETA E ATUALIZAÇÃO DOS DADOS---

    //Atualiza a tabela a cada ação do usuário
    private void atualizarTabela() {
        List<Estudante> lista = estudanteDAO.consultarEstudantes();
        tvEstudantes.getItems().setAll(lista);
    }

    //Carrega a box de usuários
    private void carregarComboBoxUsuarios() {
        List<Usuario> usuarios = usuarioDAO.consultarUsuarios();
        cbUsuario.getItems().setAll(usuarios);
    }

    //Preenche o formulário de cadastro
    private void preencherFormulario(Estudante estudante) {
        txtMatricula.setText(estudante.getMat_estudante());
        txtMc.setText(estudante.getMc().toString());
        txtAnoIngresso.setText(String.valueOf(estudante.getAno_ingresso()));
        cbUsuario.setValue(estudante.getUsuario());

        txtMatricula.setDisable(true);
    }

    //Limpa o formulário de cadastro
    private void limparFormulario() {
        txtMatricula.clear();
        txtMc.clear();
        txtAnoIngresso.clear();
        cbUsuario.setValue(null);
        txtMatricula.setDisable(false);
        tvEstudantes.getSelectionModel().clearSelection();
    }
}
