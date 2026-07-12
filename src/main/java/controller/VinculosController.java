package controller;

import dao.VinculoDAO;
import dao.EstudanteDAO;
import dao.CursoDAO;
import model.Vinculo;
import model.Estudante;
import model.Curso;
import model.enums.StatusEstudante;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.List;
import conexao.JPAUtil;

public class VinculosController {
    //ATTRIBUTES
    //Formulário de cadastro
    @FXML private TextField txtIdVinculo; // ID do vínculo
    @FXML private DatePicker dpDataEntrada; // Data de ingresso
    @FXML private ComboBox<StatusEstudante> cbStatus; // Status (Domínio status_estudante)
    @FXML private DatePicker dpDataSaida; // Data de saída (opcional)
    @FXML private ComboBox<Estudante> cbEstudante; // FK para Estudante
    @FXML private ComboBox<Curso> cbCurso; // FK para Curso

    //Tabela de vínculos
    @FXML private TableView<Vinculo> tvVinculos;
    @FXML private TableColumn<Vinculo, Integer> colIdVinculo;
    @FXML private TableColumn<Vinculo, LocalDate> colDataEntrada;
    @FXML private TableColumn<Vinculo, StatusEstudante> colStatus;
    @FXML private TableColumn<Vinculo, LocalDate> colDataSaida;
    @FXML private TableColumn<Vinculo, Estudante> colEstudante;
    @FXML private TableColumn<Vinculo, Curso> colCurso;

    //Barra de pesquisa
    @FXML private TextField txtPesquisa;

    //Botões
    @FXML private Button btnSalvar;
    @FXML private Button btnAtualizar;
    @FXML private Button btnPesquisar;
    @FXML private Button btnDeletar;

    private VinculoDAO vinculoDAO;
    private EstudanteDAO estudanteDAO;
    private CursoDAO cursoDAO;

    //METHODS
    //Carregar dados iniciais
    @FXML
    public void initialize() {
        EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();

        this.vinculoDAO = new VinculoDAO(emf);
        this.estudanteDAO = new EstudanteDAO(emf);
        this.cursoDAO = new CursoDAO(emf);

        txtIdVinculo.setEditable(false);
        colIdVinculo.setCellValueFactory(new PropertyValueFactory<>("idVinculo"));
        colDataEntrada.setCellValueFactory(new PropertyValueFactory<>("data_entrada"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colDataSaida.setCellValueFactory(new PropertyValueFactory<>("data_saida"));
        colEstudante.setCellValueFactory(new PropertyValueFactory<>("estudante"));
        colCurso.setCellValueFactory(new PropertyValueFactory<>("curso"));

        //Preenche o ComboBox com os valores fixos do enum (equivalente ao ENUM do Postgres)
        cbStatus.setItems(FXCollections.observableArrayList(StatusEstudante.values()));

        atualizarTabela();
        carregarComboBoxEstudantes();
        carregarComboBoxCursos();

        tvVinculos.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                preencherFormulario(selecionado);
            }
        });
    }

    //Ação ao clicar no botão salvar
    @FXML
    private void handleSalvarVinculo() {
        try {
            Vinculo vinculo = new Vinculo();
            vinculo.setData_entrada(dpDataEntrada.getValue());
            vinculo.setStatus(cbStatus.getValue());
            vinculo.setData_saida(dpDataSaida.getValue());
            vinculo.setEstudante(cbEstudante.getValue());
            vinculo.setCurso(cbCurso.getValue());

            vinculoDAO.inserirVinculo(vinculo);

            limparFormulario();
            atualizarTabela();
        } catch (Exception e) {
            System.err.println("Erro ao salvar vínculo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Ação ao clicar no botão atualizar
    @FXML
    private void handleAtualizarVinculo() {
        try {
            Vinculo vinculo = new Vinculo();
            vinculo.setIdVinculo(Integer.parseInt(txtIdVinculo.getText()));
            vinculo.setData_entrada(dpDataEntrada.getValue());
            vinculo.setStatus(cbStatus.getValue());
            vinculo.setData_saida(dpDataSaida.getValue());
            vinculo.setEstudante(cbEstudante.getValue());
            vinculo.setCurso(cbCurso.getValue());

            vinculoDAO.atualizarVinculo(vinculo);

            limparFormulario();
            atualizarTabela();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Ação ao clicar no botão deletar
    @FXML
    private void handleDeletarVinculo() {
        String idTexto = txtIdVinculo.getText();
        if (idTexto != null && !idTexto.isEmpty()) {
            Integer id = Integer.parseInt(idTexto);
            vinculoDAO.deletarVinculo(id);
            limparFormulario();
            atualizarTabela();
        }
    }

    //Ação ao clicar no botão pesquisar
    @FXML
    private void handlePesquisarVinculo() {
        String busca = txtPesquisa.getText();
        if (busca == null || busca.isEmpty()) {
            atualizarTabela();
        } else {
            try {
                Integer id = Integer.parseInt(busca);
                Vinculo v = vinculoDAO.buscarPorId(id);
                if (v != null) {
                    tvVinculos.getItems().setAll(v);
                } else {
                    tvVinculos.getItems().clear();
                }
            } catch (NumberFormatException e) {
                System.err.println("Termo de pesquisa inválido para ID do Vínculo.");
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
        List<Vinculo> lista = vinculoDAO.consultarVinculos();
        tvVinculos.getItems().setAll(lista);
    }

    //Carrega a box de estudantes
    private void carregarComboBoxEstudantes() {
        List<Estudante> estudantes = estudanteDAO.consultarEstudantes();
        cbEstudante.getItems().setAll(estudantes);
    }

    //Carrega a box de cursos
    private void carregarComboBoxCursos() {
        List<Curso> cursos = cursoDAO.consultarCursos();
        cbCurso.getItems().setAll(cursos);
    }

    //Preenche o formulário de cadastro
    private void preencherFormulario(Vinculo vinculo) {
        txtIdVinculo.setText(vinculo.getIdVinculo().toString());
        dpDataEntrada.setValue(vinculo.getData_entrada());
        cbStatus.setValue(vinculo.getStatus());
        dpDataSaida.setValue(vinculo.getData_saida());
        cbEstudante.setValue(vinculo.getEstudante());
        cbCurso.setValue(vinculo.getCurso());

        txtIdVinculo.setDisable(true);
    }

    //Limpa o formulário de cadastro
    private void limparFormulario() {
        txtIdVinculo.clear();
        dpDataEntrada.setValue(null);
        cbStatus.setValue(null);
        dpDataSaida.setValue(null);
        cbEstudante.setValue(null);
        cbCurso.setValue(null);

        txtIdVinculo.setDisable(false);
        tvVinculos.getSelectionModel().clearSelection();
    }
}