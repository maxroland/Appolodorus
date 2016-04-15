package br.com.sba.ctrl;

import java.io.IOException;
import java.util.List;

import br.com.museuid.banco.controle.ControleDAO;
import br.com.museuid.model.TipoUsuario;
import br.com.museuid.model.Usuario;
import br.com.museuid.util.Campo;
import br.com.museuid.util.Combo;
import br.com.museuid.util.Criptografia;
import br.com.museuid.util.Dialogo;
import br.com.museuid.util.Filtro;
import br.com.museuid.util.Grupo;
import br.com.museuid.util.Mensagem;
import br.com.museuid.util.Modulo;
import br.com.museuid.util.Nota;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class UsuarioController extends AnchorPane {

    private List<Usuario> listaUsuario;
    private int idUsuario = 0;

    @FXML
    private GridPane telaCadastro;
    @FXML
    private Label legenda;
    @FXML
    private PasswordField txtSenha;
    @FXML
    private TableView<Usuario> tbUsuario;
    @FXML
    private Button btExcluir;
    @FXML
    private PasswordField txtConfirmarSenha;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtEmail;
    @FXML
    private Button btSalvar;
    @FXML
    private TextArea txtDescricao;
    @FXML
    private ToggleGroup menu;
    @FXML
    private TextField txtLogin;
    @FXML
    private TableColumn colNome;
    @FXML
    private TableColumn colEmail;
    @FXML
    private TableColumn colStatus;
    @FXML
    private TextField txtPesquisar;
    @FXML
    private ComboBox<String> cbStatus;
    @FXML
    private TableColumn colTipo;
    @FXML
    private Label lbTitulo;
    @FXML
    private TableColumn colDescricao;
    @FXML 
    private Button btEditar;
    @FXML
    private ComboBox<TipoUsuario> cbPermissaoUsuario;
    @FXML
    private TableColumn colId;
    @FXML
    private AnchorPane telaEdicao;
    @FXML
    private TableColumn colLogin;

    public UsuarioController() {
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/usuario.fxml"));

            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();

        } catch (IOException ex) {
            Mensagem.erro("Erro ao carregar tela do usu�rio! \n" + ex);
        }
    }

    @FXML
    void telaCadastro(ActionEvent event) {
        config("Cadastrar Usu�rio", "Campos obrigat�rios", 0);
        Modulo.visualizacao(true, telaCadastro, btSalvar);
        limpar();
    }

    @FXML
    void telaEdicao(ActionEvent event) {
        config("Editar Usu�rio", "Quantidade de usu�rios encontrados", 1);
        Modulo.visualizacao(true, telaEdicao, btEditar, txtPesquisar);
        tabela();
    }

    @FXML
    void telaExcluir(ActionEvent event) {
        config("Excluir Usu�rio", "Quantidade de usu�rios encontrados", 2);
        Modulo.visualizacao(true, telaEdicao, btExcluir, txtPesquisar);
        tabela();
    }

    @FXML
    void salvar(ActionEvent event) {

        boolean vazio = Campo.noEmpty(txtNome, txtLogin, txtSenha, txtConfirmarSenha);

        String nome = txtNome.getText();
        String login = txtLogin.getText().replaceAll(" ", "").trim();
        String email = txtEmail.getText();
        String confirmar = txtConfirmarSenha.getText();
        String senha = txtSenha.getText();
        String descricao = txtDescricao.getText();
        boolean status = cbStatus.getValue().equals("Ativo");
        TipoUsuario tipo = cbPermissaoUsuario.getValue();

        if (vazio) {
            Nota.alerta("Preencher campos vazios!");
        } else if (cbPermissaoUsuario.getValue() == null) {
            Nota.alerta("Permiss�o do Usu�rio n�o encontrada!");
        } else if (!senha.equals(confirmar)) {
            Nota.alerta("Senha inv�lida, verifique se senhas s�o iguais!");
        } else if (ControleDAO.getBanco().getUsuarioDAO().isUsuario(idUsuario, login)) {
            Nota.alerta("Login j� cadastrado na base de dados!");
        } else {
            Usuario user = new Usuario(idUsuario, nome, login, Criptografia.converter(senha), email, status, null, descricao, tipo);

            if (idUsuario == 0) {
                ControleDAO.getBanco().getUsuarioDAO().inserir(user);
                Mensagem.info("Usu�rio cadastrado com sucesso!");
            } else {
                ControleDAO.getBanco().getUsuarioDAO().editar(user);
                Mensagem.info("Usu�rio atualizado com sucesso!");
            }

            telaCadastro(null);
            sincronizarBase();
        }
    }

    @FXML
    void editar(ActionEvent event) {
        try {
            Usuario user = tbUsuario.getSelectionModel().getSelectedItem();
            user.getClass();

            telaCadastro(null);

            txtNome.setText(user.getNome());
            txtLogin.setText(user.getLogin());
            txtEmail.setText(user.getEmail());
            txtDescricao.setText(user.getDescricao());
            txtSenha.setText("");
            txtConfirmarSenha.setText("");
            cbPermissaoUsuario.setValue(user.getTipoUsuario());
            cbStatus.setValue(user.isAtivo());

            lbTitulo.setText("Editar Usu�rio");
            menu.selectToggle(menu.getToggles().get(1));

            idUsuario = user.getId();

        } catch (NullPointerException ex) {
            Nota.alerta("Selecione um usu�rio na tabela para edi��o!");
        }
    }

    @FXML
    void excluir(ActionEvent event) {
        try {
            Usuario usuario = tbUsuario.getSelectionModel().getSelectedItem();

            Dialogo.Resposta response = Mensagem.confirmar("Excluir usu�rio " + usuario.getNome() + " ?");

            if (response == Dialogo.Resposta.YES) {
                ControleDAO.getBanco().getUsuarioDAO().excluir(usuario.getId());
                sincronizarBase();
                tabela();
            }

            tbUsuario.getSelectionModel().clearSelection();

        } catch (NullPointerException ex) {
            Mensagem.alerta("Selecione usu�rio na tabela para exclus�o!");
        }
    }

    @FXML
    public void initialize() {
        telaCadastro(null);

        Grupo.notEmpty(menu);
        sincronizarBase();
        combos();

        txtPesquisar.textProperty().addListener((obs, old, novo) -> {
            filtro(novo, FXCollections.observableArrayList(listaUsuario));
        });
    }

    /**
     * Preencher combos tela
     */
    private void combos() {
        Combo.popular(cbStatus, "Ativo", "Inativo");
        Combo.popular(cbPermissaoUsuario, ControleDAO.getBanco().getUsuarioDAO().usuariosTipo());
    }

    /**
     * Configura��es de tela, titulos e exibi��o de telas e menus
     */
    private void config(String tituloTela, String mensagem, int grupoMenu) {
        lbTitulo.setText(tituloTela);
        Modulo.visualizacao(false, btExcluir, btSalvar, btEditar, telaCadastro, telaEdicao, txtPesquisar);

        legenda.setText(mensagem);
        tbUsuario.getSelectionModel().clearSelection();
        menu.selectToggle(menu.getToggles().get(grupoMenu));

        idUsuario = 0;
    }

    /**
     * Sincronizar dados com banco de dados
     */
    private void sincronizarBase() {
        listaUsuario = ControleDAO.getBanco().getUsuarioDAO().listar();
    }

    /**
     * Mapear dados objetos para inser��o dos dados na tabela
     */
    private void tabela() {

        ObservableList data = FXCollections.observableArrayList(listaUsuario);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colStatus.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Usuario, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Usuario, String> obj) {
                return new SimpleStringProperty(obj.getValue().isAtivo());
            }
        });
        colTipo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Usuario, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Usuario, String> obj) {
                return new SimpleStringProperty(obj.getValue().getTipoUsuario().getNome());
            }
        });

        tbUsuario.setItems(data);
    }

    /**
     * Campo de pesquisar para filtrar dados na tabela
     */
    private void filtro(String valor, ObservableList<Usuario> listaUsuario) {

        FilteredList<Usuario> dadosFiltrados = new FilteredList<>(listaUsuario, usuario -> true);
        dadosFiltrados.setPredicate(usuario -> {

            if (valor == null || valor.isEmpty()) {
                return true;
            } else if (usuario.getNome().toLowerCase().startsWith(valor.toLowerCase())) {
                return true;
            } else if (usuario.getEmail().toLowerCase().startsWith(valor.toLowerCase())) {
                return true;
            } else if (usuario.getLogin().toLowerCase().startsWith(valor.toLowerCase())) {
                return true;
            } else if (usuario.getTipoUsuario().getNome().toLowerCase().startsWith(valor.toLowerCase())) {
                return true;
            }

            return false;
        });

        SortedList<Usuario> dadosOrdenados = new SortedList<>(dadosFiltrados);
        dadosOrdenados.comparatorProperty().bind(tbUsuario.comparatorProperty());
        Filtro.mensagem(legenda, dadosOrdenados.size(), "Quantidade de usu�rios encontradas");

        tbUsuario.setItems(dadosOrdenados);
    }

    /**
     * Limpar campos textfield cadastro de cole��es
     */
    private void limpar() {
        Campo.limpar(txtConfirmarSenha, txtLogin, txtNome, txtSenha, txtEmail);
        Campo.limpar(txtDescricao);
    }

}
