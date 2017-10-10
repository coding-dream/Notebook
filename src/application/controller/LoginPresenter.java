package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.codec.digest.DigestUtils;

import application.dialog.DialogHelper;
import application.util.L;
import application.view.MainView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginPresenter implements Initializable {

	@FXML TextField et_username;
	@FXML TextField et_password;
	@FXML Button btn_login;
	@FXML Button btn_reset;
	@FXML AnchorPane ap_login;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML public void login(ActionEvent event) {

		String username = et_username.getText();
		String password = DigestUtils.md5Hex(et_password.getText());
		if(username.equals("admin")){
			L.d("登录成功！", null);
			// 重设 scene的 布局
			MainView mainView = new MainView();

			Parent parent = mainView .getView();

			Stage stage = (Stage) ap_login.getScene().getWindow();
			stage.setScene(new Scene(parent));// 重设Scene,否则不会自动修改大小。
			stage.setOnCloseRequest(e->{
				mainView.executor.shutdown();// 必须关闭线程池,否则程序即使调用Platform.exit()也不会自动退出
				Platform.exit();
			});
		}else{
			DialogHelper.alert("提示", "登录失败！");
			et_username.setText("");
			et_password.setText("");
		}
	}

	@FXML public void reset(ActionEvent event) {
		et_username.setText("");
		et_password.setText("");
	}


}
