package application.fragment;

import java.util.List;
import java.util.Map;

import application.bean.Category;
import application.dao.ArticleDao;
import application.dao.CategoryDao;
import application.dialog.AlertDialog;
import application.dialog.DialogHelper;
import application.dialog.LayoutInflater;
import application.util.ThreadUtils;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class CategoryFragment extends Fragment {
	private ListView<Category> listView;
	private Button btn_new;

	private AlertDialog alertDialog;

	@Override
	public void onCreate(Map<String, String> bundle) {

	}

	@Override
	public void onDestroy() {

	}

	@Override
	public String getLayout() {
		// TODO Auto-generated method stub
		return "fragment_category";
	}

	@Override
	public void initData(Parent node, Map<String, String> bundle) {
		listView = (ListView<Category>) node.lookup("#lv_category");
		btn_new = (Button) node.lookup("#btn_new");
		btn_new.setOnAction(e->{
			createDialog(new Category());
		});

		listView.setPrefWidth(300);
		Label lb_nodata = new Label("暂无数据");
		listView.setPlaceholder(lb_nodata);
		loadData();
		listView.setCellFactory(new Callback<ListView<Category>, ListCell<Category>>() {

			@Override
			public ListCell<Category> call(ListView<Category> listView) {

				return new ListCell<Category>(){
					private Label lb_id;
					private Label lb_title;
					private Button btn_edit;
					private Button btn_delete;

					protected void updateItem(Category item, boolean empty) {
						super.updateItem(item, empty);// 必须调用，否则错乱的bug
						// ======================
						Parent convertView = null;
						if(getGraphic() == null){
							convertView = LayoutInflater.inflate("item_category", Parent.class);
							lb_id = (Label) convertView.lookup("#lb_id");
							lb_title = (Label) convertView.lookup("#lb_title");
							btn_edit = (Button) convertView.lookup("#btn_edit");
							btn_delete = (Button) convertView.lookup("#btn_delete");
						}else{
							convertView = (Parent) getGraphic().lookup("#root");
						}

						if(empty){
							setText(null);
							setGraphic(null);
						}else{
							btn_delete.setOnAction(e->{
								CategoryDao.getInstance().delete(item.getId());// delete database
								// update ListView items
								listView.getItems().remove(item);// remove listItem
							});

							btn_edit.setOnAction(e->{
								// update
								createDialog(item);
							});

							lb_id.setText(item.getId()+"");
							lb_title.setText(item.getName());
							// setText(null);
							setGraphic(convertView);
						}
						// ======================
					};
				};
			}
		});

	}

	private void loadData() {
		ThreadUtils.run(new Runnable() {

			@Override
			public void run() {
				List<Category> datas;
				synchronized (ArticleFragment.class) {
					datas = CategoryDao.getInstance().findAll();
				}
				Platform.runLater(()->{
					listView.getItems().clear();
					listView.getItems().addAll(datas);
				});

			}
		});
	}

	private void createDialog(Category listItem) {
		List<Category> categories = CategoryDao.getInstance().findAll();

		AlertDialog.Builder builder = new AlertDialog.Builder();
		builder.title("类别编辑");
		builder.view("dialog_category_edit")
		.build();
		alertDialog = builder.build();
		Button btn_confirm = alertDialog.findView("#btn_confirm", Button.class);
		Button btn_cancel = alertDialog.findView("#btn_cancel", Button.class);

		TextField et_title = alertDialog.findView("#et_title", TextField.class);

		if(listItem.getName() != null) et_title.setText(listItem.getName());

		btn_confirm.setOnAction(ee ->{
			String name = et_title.getText();
			if(name.equals("")){
				DialogHelper.alert("Error", "内容不能为空！");
				return;
			}
			// update ListView items & database
			listItem.setName(name);
			CategoryDao.getInstance().saveOrUpdate(listItem);

			// 自动刷新
			loadData();
			alertDialog.close();
		});

		btn_cancel.setOnAction(eee->{
			alertDialog.close();
		});

		alertDialog.show();
	}
}
