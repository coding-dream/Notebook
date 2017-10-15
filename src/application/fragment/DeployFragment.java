package application.fragment;

import java.util.Map;

import application.util.ThreadUtils;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;

public class DeployFragment extends Fragment {
	private Button btn_deploy;
	private ProgressIndicator progressbar;

	@Override
	public void onCreate(Map<String, String> bundle) {

	}

	@Override
	public void onDestroy() {

	}

	@Override
	public String getLayout() {
		return "fragment_deploy";
	}

	@Override
	public void initData(Parent node, Map<String, String> bundle) {
		btn_deploy = (Button) node.lookup("#btn_deploy");
		progressbar = (ProgressIndicator) node.lookup("#progressbar");

		btn_deploy.setOnAction(e->{
			progressbar.isIndeterminate();// 一 个负数表示进度在非确定模式,但是progressbar设置一个数值有个小bug不会更新。
			progressbar.setVisible(true);
			progressbar.setProgress(-1f);
			progressbar.setProgress(0.5f);
			progressbar.setProgress(-1f);
			btn_deploy.setDisable(true);// 不可重复点击

			// do execute
			ThreadUtils.run(new Runnable() {

				@Override
				public void run() {
					// do work
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							progressbar.setProgress(1f);// 完成
							btn_deploy.setDisable(false);
						}
					});
				}
			});

		});



	}
}
