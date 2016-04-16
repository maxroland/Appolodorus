package br.com.sba.ctrl;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.NoResultException;


import br.com.sba.facade.UsuarioFacade;
//import br.com.museuid.banco.controle.ControleDAO;
//import br.com.museuid.model.TipoUsuario;
import br.com.sba.model.Usuario;
import br.com.sba.util.Campo;
import br.com.sba.util.Combo;
import br.com.sba.util.Dialogo;
import br.com.sba.util.Filtro;
import br.com.sba.util.Grupo;
import br.com.sba.util.Mensagem;
import br.com.sba.util.Modulo;
import br.com.sba.util.Nota;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class UsuarioController extends AnchorPane{
    
	private Usuario usuario;
    private List<Usuario> listaUsuarios;
    private UsuarioFacade usuarioFacade;
    private int idUsuario = 0;

    @FXML
    private Label lbTitulo;

    @FXML
    private TextField txtPesquisar;

    @FXML
    private ToggleGroup menu;

    @FXML
    private GridPane telaCadastro;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtLogin;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private PasswordField txtConfirmarSenha;

    @FXML
    private ComboBox<?> cbStatus;

    @FXML
    private ComboBox<?> cbPermissaoUsuario;

    @FXML
    private TextField txtEmail1;

    @FXML
    private TextField txtCep;

    @FXML
    private TextField txtCpf;

    @FXML
    private TextField txtTipoUsuario;

    @FXML
    private AnchorPane telaEdicao;

    @FXML
    private TableView<Usuario> tbUsuario;

    @FXML
    private TableColumn<Usuario, Number> colId;

    @FXML
    private TableColumn<Usuario, String > colNome;

    @FXML
    private TableColumn<Usuario, String> colLogin;

    @FXML
    private TableColumn<Usuario, String> colEmail;

    @FXML
    private TableColumn<Usuario, Number> colTipoUsuario;

    @FXML
    private TableColumn<Usuario, String> colEndereco;

    @FXML
    private TableColumn<Usuario, String> colCep;

    @FXML
    private Button btSalvar;

    @FXML
    private Button btEditar;

    @FXML
    private Button btExcluir;

    @FXML
    private Label legenda;
    
    public UsuarioController() {
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/br/com/sba/view/usuario.fxml"));

            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();

        }
        catch (IOException ex) {
            Mensagem.erro("Erro ao carregar tela do usu�rio! \n" + ex); 

        }
    }
    
    @FXML
    public void initialize() {
        telaCadastro(null);

        Grupo.notEmpty(menu);
        sincronizarBase();
        combos();

        txtPesquisar.textProperty().addListener((obs, old, novo) -> {
            filtro(novo, FXCollections.observableArrayList(listaUsuarios));
        });
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

//        boolean vazio = Campo.noEmpty(txtNome, txtLogin, txtSenha, txtConfirmarSenha);
//
//        String nome = txtNome.getText();
//        String login = txtLogin.getText().replaceAll(" ", "").trim();
//        String email = txtEmail.getText();
//        String confirmar = txtConfirmarSenha.getText();
//        String senha = txtSenha.getText();
//        String descricao = txtDescricao.getText();
//        boolean status = cbStatus.getValue().equals("Ativo");
//        TipoUsuario tipo = cbPermissaoUsuario.getValue();
//
//        if (vazio) {
//            Nota.alerta("Preencher campos vazios!");
//        } else if (cbPermissaoUsuario.getValue() == null) {
//            Nota.alerta("Permiss�o do Usu�rio n�o encontrada!");
//        } else if (!senha.equals(confirmar)) {
//            Nota.alerta("Senha inv�lida, verifique se senhas s�o iguais!");
//        } else if (ControleDAO.getBanco().getUsuarioDAO().isUsuario(idUsuario, login)) {
//            Nota.alerta("Login j� cadastrado na base de dados!");
//        } else {
//            Usuario user = new Usuario(idUsuario, nome, login, Criptografia.converter(senha), email, status, null, descricao, tipo);
//
//            if (idUsuario == 0) {
//                ControleDAO.getBanco().getUsuarioDAO().inserir(user);
//                Mensagem.info("Usu�rio cadastrado com sucesso!");
//            } else {
//                ControleDAO.getBanco().getUsuarioDAO().editar(user);
//                Mensagem.info("Usu�rio atualizado com sucesso!");
//            }
//
//            telaCadastro(null);
//            sincronizarBase();
//        }
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
            txtSenha.setText("");
            txtConfirmarSenha.setText("");
            lbTitulo.setText("Editar Usu�rio");
            menu.selectToggle(menu.getToggles().get(1));

            idUsuario = user.getIdusuario();

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
                //ControleDAO.getBanco().getUsuarioDAO().excluir(usuario.getId());
            	
                sincronizarBase();
                tabela();
            }

            tbUsuario.getSelectionModel().clearSelection();

        } catch (NullPointerException ex) {
            Mensagem.alerta("Selecione usu�rio na tabela para exclus�o!");
        }
    }



    /**
     * Preencher combos tela
     */
    private void combos() {
        Combo.popular(cbStatus, "Ativo", "Inativo");
//        Combo.popular(cbPermissaoUsuario, ControleDAO.getBanco().getUsuarioDAO().usuariosTipo());
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
        getAllUsuarios();//ControleDAO.getBanco().getUsuarioDAO().listar();
    }

    /**
     * Mapear dados objetos para inser��o dos dados na tabela
     */
    private void tabela() {

        ObservableList<Usuario> data = FXCollections.observableArrayList(listaUsuarios);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
//        colStatus.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Usuario, String>, ObservableValue<String>>() {
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<Usuario, String> obj) {
//                return new SimpleStringProperty(obj.getValue().isAtivo());
//            }
//        });
//        colTipo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Usuario, String>, ObservableValue<String>>() {
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<Usuario, String> obj) {
//                return new SimpleStringProperty(obj.getValue().getTipoUsuario().getNome());
//            }
//        });

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
//            } else if (usuario.getTipoUsuario().getNome().toLowerCase().startsWith(valor.toLowerCase())) {
//                return true;
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
    }

	public UsuarioFacade getUsuarioFacade() {
		if (usuarioFacade == null) {
			usuarioFacade = new UsuarioFacade();
		}

		return usuarioFacade;
	}

	public Usuario getUsuario() {
		if (usuario == null) {
			usuario = new Usuario();
		}

		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Usuario> getAllUsuarios() {
		if (usuario == null) {
			loadUsuarios();
		}

		return listaUsuarios ;
	}

	private void loadUsuarios() {
		listaUsuarios = getUsuarioFacade().listAll();
	}

	public void resetUsuario() {
		usuario = new Usuario();
	}


}
