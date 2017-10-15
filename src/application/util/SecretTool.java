package application.util;

public class SecretTool {
	private static String secret;

	public static boolean hasKey() {
		if(null == secret || "".equals(secret)){
			return false;
		}else{
			return true;
		}
	}

	public static void set(String key){
		secret = key;
	}

	public static String get(){
		return secret;
	}
}
