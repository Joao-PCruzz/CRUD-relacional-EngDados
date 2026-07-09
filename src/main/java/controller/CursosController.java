package controller;

import dao.CursoDAO;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import model.Curso;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import model.enums.TipoGrau;
import model.enums.TipoTurno;
import model.enums.TipoNivel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class CursosController {
    //ATTRIBUTES
    //Formulário de cadastro
    @FXML private TextField txtIdCurso;
    @FXML private TextField txtNomeCurso;
    @FXML private ComboBox<TipoGrau> cbGrau;
    @FXML private ComboBox<TipoTurno> cbTurno;
    @FXML private TextField txtCampus;
    @FXML private ComboBox<TipoNivel> cbNivel;

    //Barra de pesquisa
    @FXML private TextField txtPesquisa;

    //Tabela de cursos
    @FXML private TableView<Curso> tvCursos;
    @FXML private TableColumn<Curso, Integer> colIdCurso;
    @FXML private TableColumn<Curso, String> colNomeCurso;
    @FXML private TableColumn<Curso, TipoGrau> colGrau;
    @FXML private TableColumn<Curso, TipoTurno> colTurno;
    @FXML private TableColumn<Curso, String> colCampus;
    @FXML private TableColumn<Curso, TipoNivel> colNivel;

    //Botões
    @FXML private Button btnSalvar;
    @FXML private Button btnAtualizar;
    @FXML private Button btnPesquisar;
    @FXML private Button btnDeletar;

    private CursoDAO cursoDAO;

    @FXML
    public void initialize() {
        Map<String, Object> overrides = new HashMap<>();
        overrides.put("jakarta.persistence.jdbc.url", System.getenv("DB_URL"));
        overrides.put("jakarta.persistence.jdbc.user", System.getenv("DB_USER"));
        overrides.put("jakarta.persistence.jdbc.password", System.getenv("DB_PASSWORD"));

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("universidade-pu", overrides);
        this.cursoDAO = new CursoDAO(emf);

        colIdCurso.setCellValueFactory(new PropertyValueFactory<>("idCurso"));
        colNomeCurso.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colGrau.setCellValueFactory(new PropertyValueFactory<>("grau"));
        colTurno.setCellValueFactory(new PropertyValueFactory<>("turno"));
        colCampus.setCellValueFactory(new PropertyValueFactory<>("campus"));
        colNivel.setCellValueFactory(new PropertyValueFactory<>("nivel"));

        //Preenche os ComboBox com os valores fixos dos enums (equivalentes aos ENUMs do Postgres)
        cbGrau.setItems(FXCollections.observableArrayList(TipoGrau.values()));
        cbTurno.setItems(FXCollections.observableArrayList(TipoTurno.values()));
        cbNivel.setItems(FXCollections.observableArrayList(TipoNivel.values()));

        atualizarTabela();

        tvCursos.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                preencherFormulario(selecionado);
            }
        });
    }

    //Ação ao clicar no botão salvar
    @FXML
    private void handleSalvarCurso() {
        try {
            Curso curso = new Curso();
            curso.setIdCurso(Integer.parseInt(txtIdCurso.getText()));
            curso.setNome(txtNomeCurso.getText());
            curso.setGrau(cbGrau.getValue());
            curso.setTurno(cbTurno.getValue());
            curso.setCampus(txtCampus.getText());
            curso.setNivel(cbNivel.getValue());

            cursoDAO.inserirCurso(curso);

            limparFormulario();
            atualizarTabela();
            System.out.println("Novo curso cadastrado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao salvar curso: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Ação ao clicar no botão atualizar
    @FXML
    private void handleAtualizarCurso() {
        try {
            Curso curso = new Curso();
            curso.setIdCurso(Integer.parseInt(txtIdCurso.getText()));
            curso.setNome(txtNomeCurso.getText());
            curso.setGrau(cbGrau.getValue());
            curso.setTurno(cbTurno.getValue());
            curso.setCampus(txtCampus.getText());
            curso.setNivel(cbNivel.getValue());

            cursoDAO.atualizarCurso(curso);

            limparFormulario();
            atualizarTabela();
            System.out.println("Curso atualizado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao atualizar curso: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Ação ao clicar no botão deletar
    @FXML
    private void handleDeletarCurso() {
        try {
            String idTexto = txtIdCurso.getText();
            if (idTexto != null && !idTexto.isEmpty()) {
                Integer id = Integer.parseInt(idTexto);
                cursoDAO.deletarCurso(id);

                limparFormulario();
                atualizarTabela();
                System.out.println("Curso removido com sucesso!");
            }
        } catch (Exception e) {
            System.err.println("Erro ao remover curso: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Ação ao clicar no botão pesquisar
    @FXML
    private void handlePesquisarCurso() {
        String busca = txtPesquisa.getText();
        if (busca == null || busca.isEmpty()) {
            atualizarTabela();
        } else {
            try {
                Integer id = Integer.parseInt(busca);
                Curso curso = cursoDAO.buscarPorId(id);

                if (curso != null) {
                    tvCursos.getItems().setAll(curso);
                } else {
                    tvCursos.getItems().clear();
                }
            } catch (NumberFormatException e) {
                System.out.println("Insira um ID numérico válido para pesquisar.");
            }
        }
    }
//
//    @FXML
//    private void aoLimpar() {
//        limparFormulario();
//    }

    //---MÉTODOS DE COLETA E ATUALIZAÇÃO DOS DADOS---

    //Atualiza a interface a cada ação do usuário
    private void atualizarTabela() {
        List<Curso> lista = cursoDAO.consultarCursos();
        tvCursos.getItems().setAll(lista);
    }

    //Preenche o formulário de cadastro
    private void preencherFormulario(Curso curso) {
        txtIdCurso.setText(String.valueOf(curso.getIdCurso()));
        txtNomeCurso.setText(curso.getNome());
        cbGrau.setValue(curso.getGrau());
        cbTurno.setValue(curso.getTurno());
        txtCampus.setText(curso.getCampus());
        cbNivel.setValue(curso.getNivel());

        // Bloqueia alteração do ID primário em edições
        txtIdCurso.setDisable(true);
    }

    //Limpa o formulário de cadastro
    private void limparFormulario() {
        txtIdCurso.clear();
        txtNomeCurso.clear();
        cbGrau.setValue(null);
        cbTurno.setValue(null);
        txtCampus.clear();
        cbNivel.setValue(null);

        txtIdCurso.setDisable(false);
        tvCursos.getSelectionModel().clearSelection();
    }
}