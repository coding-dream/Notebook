package application.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;

import application.Constants;

public class Preferences {

	public static String get(String key){
		try {
			String json = readFile(Constants.CONFIG_PATH);
			JSONObject config = new JSONObject(json);
			String value = config.optString(key, "");
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String readFile(String configpath) {
		try {
			File file = new File(configpath);
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
				return builder.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
