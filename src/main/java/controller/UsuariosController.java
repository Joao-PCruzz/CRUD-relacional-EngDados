package controller;

import dao.UsuarioDAO;
import model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import conexao.JPAUtil;

public class UsuariosController {
    //ATTRIBUTES
    //Formulário de cadastro
    @FXML private TextField txtCpf;
    @FXML private TextField txtNome;
    @FXML private DatePicker dpDataNascimento;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtLogin;
    @FXML private PasswordField txtSenha;

    //Tabela de usuários
    @FXML private TableView<Usuario> tvUsuarios;
    @FXML private TableColumn<Usuario, Long> colCpf;
    @FXML private TableColumn<Usuario, String> colNome;
    @FXML private TableColumn<Usuario, LocalDate> colDataNasc;
    @FXML private TableColumn<Usuario, String> colEmail;
    @FXML private TableColumn<Usuario, String> colTelefone;
    @FXML private TableColumn<Usuario, String> colLogin;

    //Botões
    @FXML private Button btnSalvar;
    @FXML private Button btnAtualizar;
    @FXML private Button btnPesquisar;
    @FXML private Button btnDeletar;

    //Barra de pesquisa
    @FXML private TextField txtPesquisa;

    private UsuarioDAO usuarioDAO;

    //METHODS
    //Carregar dados iniciais
    @FXML
    public void initialize() {
        EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();

        this.usuarioDAO = new UsuarioDAO(emf);

        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colDataNasc.setCellValueFactory(new PropertyValueFactory<>("data_nascimento"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colLogin.setCellValueFactory(new PropertyValueFactory<>("login"));

        atualizarTabela();

        tvUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                preencherFormulario(selecionado);
            }
        });
    }

    //Ação ao clicar no botão salvar
    @FXML
    private void handleSalvarUsuario() {
        try {
            Usuario usuario = new Usuario();

            String cpfTexto = txtCpf.getText().replaceAll("[^0-9]", "");
            usuario.setCpf(Long.parseLong(cpfTexto));
            usuario.setNome(txtNome.getText());
            usuario.setData_nascimento(dpDataNascimento.getValue());
            usuario.setLogin(txtLogin.getText());
            usuario.setSenha(txtSenha.getText());

            usuario.setEmail(converterTextoParaLista(txtEmail.getText()));
            usuario.setTelefone(converterTextoParaLista(txtTelefone.getText()));

            usuarioDAO.inserirUsuario(usuario);

            limparFormulario();
            atualizarTabela();
        } catch (Exception e) {
            System.err.println("Erro ao salvar usuário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Ação ao clicar no botão atualizar
    @FXML
    private void handleAtualizarUsuario() {
        try {
            Usuario usuario = new Usuario();

            usuario.setCpf(Long.parseLong(txtCpf.getText()));
            usuario.setNome(txtNome.getText());
            usuario.setData_nascimento(dpDataNascimento.getValue());
            usuario.setLogin(txtLogin.getText());
            usuario.setSenha(txtSenha.getText());

            usuario.setEmail(converterTextoParaLista(txtEmail.getText()));
            usuario.setTelefone(converterTextoParaLista(txtTelefone.getText()));

            usuarioDAO.atualizarUsuario(usuario);

            limparFormulario();
            atualizarTabela();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Ação ao clicar no botão deletar
    @FXML
    private void handleDeletarUsuario() {
        String cpfTexto = txtCpf.getText();
        if (cpfTexto != null && !cpfTexto.isEmpty()) {
            long cpf = Long.parseLong(cpfTexto.replaceAll("[^0-9]", ""));
            usuarioDAO.deletarUsuario(cpf);
            limparFormulario();
            atualizarTabela();
        }
    }

    //Ação ao clicar no botão pesquisar
    @FXML
    private void handlePesquisarUsuario() {
        String busca = txtPesquisa.getText();
        if (busca == null || busca.isEmpty()) {
            atualizarTabela();
        } else {
            try {
                long cpf = Long.parseLong(busca.replaceAll("[^0-9]", ""));
                Usuario u = usuarioDAO.buscarPorCPF(cpf);
                if (u != null) {
                    tvUsuarios.getItems().setAll(u);
                } else {
                    tvUsuarios.getItems().clear();
                }
            } catch (NumberFormatException e) {
                System.err.println("Termo de pesquisa inválido para CPF.");
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
        List<Usuario> lista = usuarioDAO.consultarUsuarios();
        tvUsuarios.getItems().setAll(lista);
    }

    //Preenche o formulário de cadastro
    private void preencherFormulario(Usuario usuario) {
        txtCpf.setText(String.valueOf(usuario.getCpf()));
        txtNome.setText(usuario.getNome());
        dpDataNascimento.setValue(usuario.getData_nascimento());
        txtLogin.setText(usuario.getLogin());
        txtSenha.setText(usuario.getSenha());

        if (usuario.getEmail() != null) {
            txtEmail.setText(String.join(", ", usuario.getEmail()));
        }
        if (usuario.getTelefone() != null) {
            txtTelefone.setText(String.join(", ", usuario.getTelefone()));
        }

        txtCpf.setDisable(true);
    }

    //Limpa o formulário de cadastro
    private void limparFormulario() {
        txtCpf.clear();
        txtNome.clear();
        dpDataNascimento.setValue(null);
        txtEmail.clear();
        txtTelefone.clear();
        txtLogin.clear();
        txtSenha.clear();

        txtCpf.setDisable(false);
        tvUsuarios.getSelectionModel().clearSelection();
    }

    //Converte a String de inputs em List<String> para o mapeamento do JPA
    private List<String> converterTextoParaLista(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return Arrays.stream(texto.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}