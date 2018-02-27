package application.bean;

public class EmailFrom {

	private String user;

	private String pwd;

	private String hostName;

	private int smtpPort;

	private String fromAddr;

	private String fromNick;

	private String subject;

	private String text;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(int smtpPort) {
		this.smtpPort = smtpPort;
	}

	public String getFromAddr() {
		return fromAddr;
	}

	public void setFromAddr(String fromAddr) {
		this.fromAddr = fromAddr;
	}

	public String getFromNick() {
		return fromNick;
	}

	public void setFromNick(String fromNick) {
		this.fromNick = fromNick;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "EmailFrom [user=" + user + ", pwd=" + pwd + ", hostName=" + hostName + ", smtpPort=" + smtpPort
				+ ", fromAddr=" + fromAddr + ", fromNick=" + fromNick + ", subject=" + subject + ", text=" + text + "]";
	}
}
