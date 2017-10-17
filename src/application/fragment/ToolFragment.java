package application.fragment;

import java.io.File;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.less.downloadmanager.lib.DownloadException;
import com.less.downloadmanager.lib.DownloadManager;
import com.less.downloadmanager.lib.request.FileCallBack;
import com.less.downloadmanager.lib.request.GetBuilder;
import com.less.downloadmanager.lib.request.RequestCall;

import application.dialog.DialogHelper;
import application.util.L;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

public class ToolFragment extends Fragment {
	private TextField tf_download_url;
	private TextField tf_download_folder;
	private TextField tf_download_fname;
	private Button btn_download_start;
	private Button btn_download_pause;
	private Button btn_download_cancel;
	private ProgressIndicator progressbar;
	private Label lb_tip;

	private String tag = DigestUtils.md5Hex("https://github.com/wangli0");

	@Override
	public void onCreate(Map<String, String> bundle) {

	}

	@Override
	public void initData(Parent node, Map<String, String> bundle) {
		tf_download_url = (TextField) node.lookup("#tf_download_url");
		tf_download_folder = (TextField) node.lookup("#tf_download_folder");
		tf_download_fname = (TextField) node.lookup("#tf_download_fname");
		btn_download_start = (Button) node.lookup("#btn_download_start");
		btn_download_pause = (Button) node.lookup("#btn_download_pause");
		btn_download_cancel = (Button) node.lookup("#btn_download_cancel");
		progressbar = (ProgressIndicator) node.lookup("#progressbar");
		lb_tip = (Label) node.lookup("#lb_tip");

		btn_download_start.setOnAction(e-> {
			String url = tf_download_url.getText();
			String name = tf_download_fname.getText();
			String folder = tf_download_folder.getText();
			if(isEmpty(url,name,folder)){
				DialogHelper.alert("Error", "输入不能为空！");
				return;
			}

			RequestCall call = new GetBuilder()
					.name(name)
					.uri(url)
					.folder(new File(folder))
					.tag(tag)
					.build();

			DownloadManager downloadManager = DownloadManager.getInstance();
			downloadManager.start(call, new FileCallBack() {

				@Override
				public void onStart() {
					L.D("============== onStart ============== ");
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							progressbar.setVisible(true);
						}
					});
				}

				@Override
				public void onDownloadFailed(DownloadException e) {
					e.printStackTrace();
					L.D("============== onDownloadFailed ============== " + e.toString());
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							lb_tip.setText("下载失败！" + e.getMessage());
							progressbar.setVisible(false);
						}
					});
				}

				@Override
				public void onDownloadCompleted(File file) {
					L.D("============== onDownloadCompleted ============== " + file.getAbsolutePath());
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							lb_tip.setText("下载完成： " + file.getAbsolutePath());
							progressbar.setProgress(1f);
						}
					});
				}

				@Override
				public void onDownloadProgress(long finished, long totalLength, int percent) {
					L.D("============== onDownloadProgress ============== " + percent);
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							progressbar.setProgress((float)(percent)/100);
							lb_tip.setText("已下载   " + percent + " %");
						}
					});
				}
			});

		});

		btn_download_pause.setOnAction(e-> {
			DownloadManager.getInstance().pause(tag);
		});

		btn_download_cancel.setOnAction(e-> {
			DownloadManager.getInstance().cancel(tag);
		});
	}

	private boolean isEmpty(String ...strings ) {
		for(String str : strings){
			if(null == str || "".equals(str)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void onDestroy() {

	}

	@Override
	public String getLayout() {
		return "fragment_tool";
	}

}
