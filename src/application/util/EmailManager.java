package application.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;

import com.google.gson.Gson;

import application.Constants;
import application.bean.EmailFrom;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EmailManager {

	private static EmailManager instance;

	public static EmailManager getInstance(){
		if(instance == null){
			synchronized (EmailManager.class) {
				if(instance == null){
					instance = new EmailManager();
				}
			}
		}
		return instance;
	}

	public void send(boolean batchAll, TextArea taFrom, TextArea taTo, TextArea taContent, TextField txUrl) throws IOException{
		String from = taFrom.getText();
		String to = taTo.getText();
		String html = taContent.getText();
		String url = txUrl.getText();

		if(from.isEmpty() || to.isEmpty() || html.isEmpty()){
			System.out.println("input can't is empty!");
			return;
		}
		List<String> emails = FileUtils.readLines(new File(Constants.EMAIL_TO), "UTF-8");
		Gson gson = new Gson();
		EmailFrom emailFrom = gson.fromJson(from, EmailFrom.class);
		System.err.println(emailFrom);
		System.err.println(emails);
		if(emailFrom.getAttachmentName().equals("xx.txt")){
			emailFrom.setAttached(false);
		} else {
			emailFrom.setAttached(true);
		}
		if(batchAll){
			sendBatch(emailFrom, emails, url, html);
		} else {
			for(String email : emails){
				sendSingle(emailFrom, email, url, html);
				sleep(Constants.EMAIL_SEND_TIMEOUT);
			}
		}
	}

	private void sendBatch(EmailFrom emailFrom, List<String> list, String _url, String html) {
		try {
			String[] emails = new String[list.size()];
	        list.toArray(emails);
	        ImageHtmlEmail email = createImageEmail(emailFrom, _url, html);
	        email.addTo(emails);
        	attachFile(email, emailFrom);
	        email.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendSingle(EmailFrom emailFrom, String singleEmail, String _url, String html) {
		try {
	        ImageHtmlEmail email = createImageEmail(emailFrom, _url, html);
	        email.addTo(singleEmail);
        	attachFile(email, emailFrom);
	        email.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ImageHtmlEmail createImageEmail(EmailFrom emailFrom, String _url, String html) throws Exception {
        ImageHtmlEmail email = new ImageHtmlEmail();
        email.setCharset("UTF-8");
		URL url = new URL(_url);
		email.setDataSourceResolver(new DataSourceUrlResolver(url));
		email.setHostName(emailFrom.getHostName());
	    email.setSmtpPort(emailFrom.getSmtpPort());
	    email.setAuthenticator(new DefaultAuthenticator(emailFrom.getUser(), emailFrom.getPwd()));
	    email.setSSLOnConnect(true);
        email.setFrom(emailFrom.getFromAddr(), emailFrom.getFromNick());
        email.setSubject(emailFrom.getSubject());
        email.setTextMsg(emailFrom.getText());
        email.setHtmlMsg(html);
        return email;
	}

	private void attachFile(ImageHtmlEmail email, EmailFrom emailFrom) {
        if(!emailFrom.isAttached()){
        	return;
        }
        // 创建附件
        EmailAttachment attachment = new EmailAttachment();
        // 本地路径
        attachment.setPath(emailFrom.getAttachmentPath());
        // 网络路径
        // attachment.setURL(new URL("http://www.baidu.com/xx.zip"));
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        // 附件描述
        // attachment.setDescription("文章投稿附件");
        // 附件名称
        attachment.setName(emailFrom.getAttachmentName());
        // 可以添加多个附件
        // email.attach(attachment1);
        // email.attach(attachment2);
        try {
			email.attach(attachment);
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	private void sleep(int emailSendTimeout) {
		try {
			Thread.sleep(emailSendTimeout);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
