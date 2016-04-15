package br.com.sba.view.login;

import java.io.IOException;

import br.com.sba.AppolodorusApp;
import br.com.sba.facade.UsuarioFacade;
import br.com.sba.model.Usuario;
import br.com.sba.util.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class LoginAppController{
	@FXML
	private TextField pwdSenha;
	@FXML
	private TextField txtLogin;
    @FXML
    private Label lblErroLogin;
	@FXML
	private Button btnEntrar;
	
	private Usuario currentUser;
	
	
	@FXML
	public void doLogin(ActionEvent event ) throws IOException{
		currentUser = new Usuario(txtLogin.getText(), pwdSenha.getText());
		if(validateInput()){
			UsuarioFacade usuarioFacade = new UsuarioFacade();
			currentUser =  usuarioFacade.isValidLogin(currentUser.getLogin(), currentUser.getSenha());
				
			if(currentUser != null){
				//Passa a responsabilidade do Palco pelo objeto Parent a classe Appolodorus
				((Node) (event.getSource())).getScene().getWindow().hide();
				Parent parent = FXMLLoader.load(getClass().getResource("/br/com/sba/view/app/app.fxml"));
				new AppolodorusApp().startByParent(parent);			
	        }
			
			lblErroLogin.setText("Login inv�lido!");
		}
	}

	
	public boolean validateInput(){
		String errorMessage = validateForm();
		if (!errorMessage.isEmpty()) {
			showValidationErrorAccess(errorMessage);
			return false;
		}
		return true;
	}
	
	private String validateForm() {
		StringBuilder errorMessage = new StringBuilder();

		if (StringUtils.isEmpty(currentUser.getLogin())) {
			errorMessage.append("Preencha o login").append(StringUtils.newLine());
		}

		if (StringUtils.isEmpty(currentUser.getSenha())) {
			errorMessage.append("Preencha a senha").append(StringUtils.newLine());
		}

		return errorMessage.toString();
	}
	
	private void showValidationErrorAccess(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Erro de Acesso");
		alert.setHeaderText("Campos obrigat�rios n�o preenchidos!");
		alert.setContentText(message);
		alert.showAndWait();
	}

}
