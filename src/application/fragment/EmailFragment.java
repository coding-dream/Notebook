package application.fragment;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

import application.Constants;
import application.bean.EmailFrom;
import application.dialog.DialogHelper;
import application.util.EmailManager;
import application.util.ThreadUtils;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EmailFragment extends Fragment {

	private CheckBox cb_batch;

	private TextArea ta_from;

	private TextArea ta_to;

	private TextArea ta_content;

	private TextField tx_url;

	private Button btn_send;


	@Override
	public void onCreate(Map<String, String> bundle) {

	}

	@Override
	public String getLayout() {
		return "fragment_email";
	}

	@Override
	public void onDestroy() {

	}

	@Override
	public void initData(Parent node, Map<String, String> bundle) {
		cb_batch = (CheckBox) node.lookup("#cb_batch");
		ta_from = (TextArea) node.lookup("#ta_from");
		ta_to = (TextArea) node.lookup("#ta_to");
		ta_content = (TextArea) node.lookup("#ta_content");
		btn_send = (Button) node.lookup("#btn_send");
		tx_url = (TextField) node.lookup("#tx_url");

		loadCache();

		btn_send.setOnAction(e -> {
			boolean batchAll = cb_batch.isSelected();
			ThreadUtils.run(new Runnable() {

				@Override
				public void run() {
					try {
						updateDB();
						EmailManager.getInstance().send(batchAll, ta_from, ta_to, ta_content, tx_url);
						System.out.println("=========> end <=========");
						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								DialogHelper.alert("信息", "邮件发送完毕!");
							}
						});
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
		});
	}

	private void loadCache() {
		File cacheFrom = new File(Constants.EMAIL_FROM);
		File cacheTo = new File(Constants.EMAIL_TO);
		File cacheHTML = new File(Constants.EMAIL_HTML);
		File cacheURL = new File(Constants.EMAIL_URL);

		if(!cacheFrom.exists()){
			EmailFrom emailFrom = new EmailFrom();
			emailFrom.setUser("hc59yv");
			emailFrom.setPwd("paiyon");
			emailFrom.setHostName("smtp.163.com");
			emailFrom.setSmtpPort(25);
			emailFrom.setFromAddr("hc59yv@163.com");
			emailFrom.setFromNick("小灰灰");
			emailFrom.setSubject("投稿申请: xx的文章投稿");
			emailFrom.setText("Your email client does not support HTML messages");
			emailFrom.setAttachmentName("xx.txt");
			emailFrom.setAttachmentPath("C:\\xx.txt");
			String json = new Gson().toJson(emailFrom);
			saveLocal(cacheFrom, json);
		}

		if(!cacheTo.exists()){
			StringBuilder builder = new StringBuilder();
			builder.append("xx@qq.com\r\n");
			builder.append("xx@163.com\r\n");
			saveLocal(cacheTo, builder.toString());
		}

		if(!cacheHTML.exists()){
			String html = "<html>hello</html>";
			saveLocal(cacheHTML, html);
		}

		if(!cacheURL.exists()){
			String url = "http://www.xx.com";
			saveLocal(cacheURL, url);
		}

		try {
			String fromContent = FileUtils.readFileToString(cacheFrom, "UTF-8");
			String toContent = FileUtils.readFileToString(cacheTo,"UTF-8");
			String url = FileUtils.readFileToString(cacheURL, "UTF-8");
			String html = FileUtils.readFileToString(cacheHTML,"UTF-8");

			ta_from.setText(fromContent);
			ta_to.setText(toContent);
			ta_content.setText(html);
			tx_url.setText(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private synchronized void saveLocal(File file, String text) {
		try {
			FileUtils.write(file, text, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updateDB() {
		File cacheFrom = new File(Constants.EMAIL_FROM);
		File cacheTo = new File(Constants.EMAIL_TO);
		File cacheHTML = new File(Constants.EMAIL_HTML);
		File cacheURL = new File(Constants.EMAIL_URL);
		saveLocal(cacheFrom, ta_from.getText());
		saveLocal(cacheTo, ta_to.getText());
		saveLocal(cacheHTML, ta_content.getText());
		saveLocal(cacheURL, tx_url.getText());
	}
}
