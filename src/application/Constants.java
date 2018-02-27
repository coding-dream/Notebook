package application;

import javafx.scene.image.Image;

public class Constants {
	 public static final String APP_NAME = "Notebook v1.0.0";
	 public static final int PAGE_SIZE = 20;
	 public static final String ENCODING = "UTF-8";
	 public static final Image APP_LOGO = new Image("/images/logo.jpg");
	 public static final String BrowserCanOpenNew = "(http://|https://|www.|ftp://|thunder:)[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?";
	 public static final String CONFIG_PATH = "C:\\Program Files\\Notebook\\notebook.json";
	 public static final String CONFIG_TEMP_DB = "C:\\Program Files\\Notebook\\temp.db";
	 public static final String CONFIG_DOWNLOAD_PATH = "download_path";
	 public static final String CONFIG_DEPLOY_PATH = "deploy_path";
	 public static final String CONFIG_SECRET = "secret";
	 public static final String CONFIG_GIT_USER = "git_user";
	 public static final String CONFIG_GIT_PASSWORD = "git_passwd";
	 public static final String CONFIG_APP_PASSWORD = "app_passwd";
	 public static final String EMAIL_FROM = "C:\\JavaFX\\email\\from.json";
	 public static final String EMAIL_TO = "C:\\JavaFX\\email\\to.txt";
	 public static final String EMAIL_HTML = "C:\\JavaFX\\email\\email.html";
	 public static final String EMAIL_URL = "C:\\JavaFX\\email\\url.txt";
	 public static final int EMAIL_SEND_TIMEOUT = 3000;
}
