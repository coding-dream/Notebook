package application.fragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;

import application.Constants;
import application.dialog.DialogHelper;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SettingFragment extends Fragment {
	private TextField et_download;
	private TextField et_secret;
	private TextField et_git_username;
	private PasswordField et_git_passwd;
	private PasswordField et_app_password;
	private PasswordField et_app_password_second;
	private Button btn_submit;

	@Override
	public void onCreate(Map<String, String> bundle) {

	}

	@Override
	public void onDestroy() {

	}

	@Override
	public String getLayout() {
		return "fragment_setting";
	}

	@Override
	public void initData(Parent node, Map<String, String> bundle) {
		et_download = (TextField) node.lookup("#et_download");
		et_secret = (TextField) node.lookup("#et_secret");
		et_git_username = (TextField) node.lookup("#et_git_username");
		et_git_passwd = (PasswordField) node.lookup("#et_git_passwd");
		et_app_password = (PasswordField) node.lookup("#et_app_password");
		et_app_password_second = (PasswordField) node.lookup("#et_app_password_second");
		btn_submit = (Button) node.lookup("#btn_submit");

		readFromProperty();

		btn_submit.setOnAction(e->{
			String message = "";
			if(!et_app_password.getText().trim().equals(et_app_password_second.getText().trim())){
				message = "两次密码输入不一致！";
				DialogHelper.alert("警告", message);
				return;
			}
			if(!"".equals(et_app_password.getText().trim()) && et_app_password.getText().trim().length() < 5){
				message = "密码长度太短,不够安全！";
				DialogHelper.alert("警告", message);
				return;
			}

			writeToProperty();
		});


	}

	private void readFromProperty() {
		try {
			File file = new File(Constants.CONFIG_PATH);
			if(file.exists()){
				FileInputStream fileInputStream = new FileInputStream(file);
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, "utf-8"));
				String line = "";
				StringBuilder builder = new StringBuilder();
				while((line = bufferedReader.readLine()) != null){
					builder.append(line + "\r\n");
				}
				bufferedReader.close();
				fileInputStream.close();
				JSONObject config = new JSONObject(builder.toString());
				String downloadPath = config.optString(Constants.CONFIG_DOWNLOAD_PATH, "");
				String secret = config.optString(Constants.CONFIG_SECRET, "");
				String git_user = config.optString(Constants.CONFIG_GIT_USER, "");
				String git_passwd = config.optString(Constants.CONFIG_GIT_PASSWORD, "");
				String app_passwd = config.optString(Constants.CONFIG_APP_PASSWORD, "");

				et_download.setText(downloadPath);
				et_secret.setText(secret);
				et_git_username.setText(git_user);
				et_git_passwd.setText(git_passwd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void writeToProperty() {
		try {
			JSONObject config;

			File file = new File(Constants.CONFIG_PATH);
			if(!file.exists()){
				if(!file.getParentFile().exists()){
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
				// create
				config = new JSONObject();
				config.put(Constants.CONFIG_DOWNLOAD_PATH, et_download.getText().trim());
				config.put(Constants.CONFIG_SECRET, et_secret.getText().trim());
				config.put(Constants.CONFIG_GIT_USER, et_git_username.getText().trim());
				config.put(Constants.CONFIG_GIT_PASSWORD, et_git_passwd.getText().trim());
				config.put(Constants.CONFIG_APP_PASSWORD, DigestUtils.md5Hex(et_app_password.getText().trim()));

			} else {
				// update
				FileInputStream fileInputStream = new FileInputStream(file);
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, "utf-8"));
				String line = "";
				StringBuilder builder = new StringBuilder();
				while((line = bufferedReader.readLine()) != null){
					builder.append(line + "\r\n");
				}
				bufferedReader.close();
				fileInputStream.close();
				config = new JSONObject(builder.toString());
				if(!"".equals(et_download.getText().trim())) config.put(Constants.CONFIG_DOWNLOAD_PATH, et_download.getText().trim());
				if(!"".equals(et_secret.getText().trim())) config.put(Constants.CONFIG_SECRET, et_secret.getText().trim());
				if(!"".equals(et_git_username.getText().trim())) config.put(Constants.CONFIG_GIT_USER, et_git_username.getText().trim());
				if(!"".equals(et_git_passwd.getText().trim())) config.put(Constants.CONFIG_GIT_PASSWORD, et_git_passwd.getText().trim());
				if(!"".equals(et_app_password.getText().trim())) config.put(Constants.CONFIG_APP_PASSWORD, DigestUtils.md5Hex(et_app_password.getText().trim()));
			}

			FileOutputStream fileOutputStream = new FileOutputStream(file);
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream,"utf-8"));
			bufferedWriter.write(config.toString());
			bufferedWriter.close();
			bufferedWriter.close();
			DialogHelper.alert("Success", "设置成功!");

		} catch (Exception e) {
			DialogHelper.alert("Failed", "设置失败!");
			e.printStackTrace();
		}
	}

	private boolean isEmpty(String ... strs) {
		for(String str : strs){
			if("".equals(str) || null == str){
				return true;
			}
		}
		return false;
	}
}
