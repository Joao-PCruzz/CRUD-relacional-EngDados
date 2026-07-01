package controller;

import dao.CursoDAO;
import javafx.scene.control.Button;
import model.Curso;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class CursosController {
    //ATTRIBUTES
    //Formulário de cadastro
    @FXML private TextField txtIdCurso;
    @FXML private TextField txtNomeCurso;
    @FXML private TextField txtGrau;
    @FXML private TextField txtTurno;
    @FXML private TextField txtCampus;
    @FXML private TextField txtNivel;

    //Barra de pesquisa
    @FXML private TextField txtPesquisa;

    //Tabela de cursos
    @FXML private TableView<Curso> tvCursos;
    @FXML private TableColumn<Curso, Integer> colIdCurso;
    @FXML private TableColumn<Curso, String> colNomeCurso;
    @FXML private TableColumn<Curso, String> colGrau;
    @FXML private TableColumn<Curso, String> colTurno;
    @FXML private TableColumn<Curso, String> colCampus;
    @FXML private TableColumn<Curso, String> colNivel;

    //Botões
    @FXML private Button btnSalvar;
    @FXML private Button btnAtualizar;
    @FXML private Button btnPesquisar;
    @FXML private Button btnDeletar;

    private CursoDAO cursoDAO;

    @FXML
    public void initialize() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("universidade-pu");
        this.cursoDAO = new CursoDAO(emf);

        colIdCurso.setCellValueFactory(new PropertyValueFactory<>("idCurso"));
        colNomeCurso.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colGrau.setCellValueFactory(new PropertyValueFactory<>("grau"));
        colTurno.setCellValueFactory(new PropertyValueFactory<>("turno"));
        colCampus.setCellValueFactory(new PropertyValueFactory<>("campus"));
        colNivel.setCellValueFactory(new PropertyValueFactory<>("nivel"));

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
            curso.setGrau(txtGrau.getText());
            curso.setTurno(txtTurno.getText());
            curso.setCampus(txtCampus.getText());
            curso.setNivel(txtNivel.getText());

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
            curso.setGrau(txtGrau.getText());
            curso.setTurno(txtTurno.getText());
            curso.setCampus(txtCampus.getText());
            curso.setNivel(txtNivel.getText());

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
        txtGrau.setText(curso.getGrau());
        txtTurno.setText(curso.getTurno());
        txtCampus.setText(curso.getCampus());
        txtNivel.setText(curso.getNivel());

        // Bloqueia alteração do ID primário em edições
        txtIdCurso.setDisable(true);
    }

    //Limpa o formulário de cadastro
    private void limparFormulario() {
        txtIdCurso.clear();
        txtNomeCurso.clear();
        txtGrau.clear();
        txtTurno.clear();
        txtCampus.clear();
        txtNivel.clear();

        txtIdCurso.setDisable(false);
        tvCursos.getSelectionModel().clearSelection();
    }
}