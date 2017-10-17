package application.fragment;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import application.Constants;
import application.dialog.DialogHelper;
import application.util.HttpGets;
import application.util.HttpGets.Callback;
import application.util.PBECoder;
import application.util.Preferences;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SyncFragment extends Fragment {
	private Image sync_defalt = new Image("images/blue/sync_default.png");
	private Image sync_enter = new Image("images/blue/sync_enter.png");
	private Image down_default = new Image("images/blue/down_default.png");
	private Image down_enter = new Image("images/blue/down_enter.png");

	private ImageView iv_sync;
	private ImageView iv_down;
	private ProgressIndicator progressbar;

	@Override
	public void onCreate(Map<String, String> bundle) {

	}

	@Override
	public void onDestroy() {

	}

	@Override
	public String getLayout() {
		return "fragment_sync";
	}

	@Override
	public void initData(Parent node, Map<String, String> bundle) {
		progressbar = (ProgressIndicator) node.lookup("#progressbar");

		iv_sync = (ImageView) node.lookup("#iv_sync");
		iv_down = (ImageView) node.lookup("#iv_down");

		iv_sync.setOnMouseEntered(e-> {
			iv_sync.setImage(sync_enter);
		});
		iv_sync.setOnMouseExited(e-> {
			iv_sync.setImage(sync_defalt);
		});

		iv_down.setOnMouseEntered(e-> {
			iv_down.setImage(down_enter);
		});
		iv_down.setOnMouseExited(e-> {
			iv_down.setImage(down_default);
		});

		iv_down.setOnMouseClicked(e->{
			 download();
		});

		iv_sync.setOnMouseClicked(e->{
			sync();
		});
	}

	private void sync() {
		File tempFile = new File(Constants.CONFIG_TEMP_DB);
		if(!tempFile.exists()){
			DialogHelper.alert("警告", "同步文件不存在，请先下载后再同步！");
			return;
		}
		progressbar.setVisible(true);
		progressbar.setProgress(-1f);
		progressbar.setProgress(0.5f);
		progressbar.setProgress(-1f);
		iv_down.setDisable(true);
		iv_sync.setDisable(true);

		String projectDir = System.getProperty("user.dir");
		File destFile = new File(projectDir,"notebook.db");

		if(destFile.exists()){
			destFile.delete();
		}
		try {
			FileUtils.moveFile(tempFile, destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		progressbar.setProgress(1f);
		iv_down.setDisable(false);
		iv_sync.setDisable(false);
		DialogHelper.alert("Success", "同步成功！-> \r\n" + tempFile.getAbsolutePath() + " 已被移动至 " + projectDir);
	}

	private void download() {
		progressbar.setVisible(true);
		progressbar.setProgress(-1f);
		progressbar.setProgress(0.5f);
		progressbar.setProgress(-1f);
		iv_down.setDisable(true);
		iv_sync.setDisable(true);

		String url = Preferences.get(Constants.CONFIG_DOWNLOAD_PATH);
		HttpGets.getInstance().sendRequest(url, new Callback() {

			@Override
			public <T> void done(T ret, Exception e) {
				if(e == null){
					byte[] datas = (byte[]) ret;
					decryptData(datas);
				}else{
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							DialogHelper.alert("Error", "下载过程中出现异常！");
							progressbar.setVisible(false);
							iv_down.setDisable(false);
							iv_sync.setDisable(false);
						}
					});
				}
			}
		});
	}

	protected void decryptData(byte[] datas) {
		File tempFile = new File(Constants.CONFIG_TEMP_DB);
		if(!tempFile.exists()){
			if(!tempFile.getParentFile().exists()){
				tempFile.getParentFile().mkdirs();
			}
			try {
				tempFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String secret = Preferences.get(Constants.CONFIG_SECRET);
		if("".equals(secret)){
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					DialogHelper.alert("警告", "未设置秘钥！");
					progressbar.setVisible(false);
					iv_down.setDisable(false);
					iv_sync.setDisable(false);
				}
			});
			return;
		}

		byte[] decryptDatas = PBECoder.decrypt(datas, secret, "13572468".getBytes());
		try {
			FileUtils.writeByteArrayToFile(tempFile, decryptDatas);
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					progressbar.setProgress(1f);
					iv_down.setDisable(false);
					iv_sync.setDisable(false);
					DialogHelper.alert("Success", "下载并解密完成,临时文件保存在-> \r\n" + tempFile.getAbsolutePath());
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
