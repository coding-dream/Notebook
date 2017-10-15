package application.fragment;

import java.util.List;
import java.util.Map;

import application.Constants;
import application.bean.Article;
import application.bean.Category;
import application.bean.Result;
import application.dao.ArticleDao;
import application.dao.CategoryDao;
import application.dialog.AlertDialog;
import application.dialog.DialogHelper;
import application.dialog.LayoutInflater;
import application.dialog.OnClickListener;
import application.util.L;
import application.util.ThreadUtils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class ArticleFragment extends Fragment {
	private ListView<Article> listView;
	private Pagination pager_article;
	private Button btn_search;
	private Button btn_new;
	private TextField et_input;

	private boolean FIRST = true;

	private AlertDialog alertDialog;

	@Override
	public void onCreate(Map<String, String> bundle) {

	}

	@Override
	public String getLayout() {
		return "fragment_article";
	}

	@Override
	public void onDestroy() {
		System.out.println("=====> onDestroy");
	}

	@Override
	public void initData(Parent node, Map<String, String> bundle) {
		listView = (ListView<Article>) node.lookup("#lv_article");

		et_input = (TextField) node.lookup("#et_input");
		btn_search = (Button) node.lookup("#btn_search");
		btn_new = (Button) node.lookup("#btn_new");

		et_input.setOnKeyPressed(e->{
			if(e.getCode() == KeyCode.ENTER){
				System.out.println("Enter");
				e.consume();
			}
		});

		btn_search.setOnAction(e->{
			if(et_input.getText().equals("")){
				System.out.println("search can not null");
				DialogHelper.alert("Error", "搜索内容不能为空！");
				return;
			}

			List<Article> list = ArticleDao.getInstance().search(et_input.getText());
			listView.getItems().clear();
			listView.getItems().addAll(list);

		});

		btn_new.setOnAction(e->{
			createDialog(new Article());
		});

		pager_article = (Pagination) node.lookup("#pager_article");
		pager_article.setCurrentPageIndex(0);
		pager_article.setVisible(false);
//		pager_article.setPageCount(result.pageCount);
//		pager_article.setPageCount(Pagination.INDETERMINATE);// 无限页模式,注意如果 setPageCount.的页数更新,则会多调用一次Node  call(0);不影响结果。
		pager_article.setPageFactory(new Callback<Integer, Node>() {

			@Override
			public Node call(Integer pageIndex) {
				// pageIndex 下标从0开始
				Label content = null;
				if (pageIndex >= 0 ) {
					content = new Label("当前页: " + (pageIndex + 1));
				}
				L.D("pageIndex " + pageIndex);
				loadData(pageIndex + 1);
				return content;
			}
		});


		listView.setPrefWidth(300);
		Label lb_nodata = new Label("暂无数据");
		listView.setPlaceholder(lb_nodata);

		loadData(1);
		listView.setCellFactory(new Callback<ListView<Article>, ListCell<Article>>() {

			@Override
			public ListCell<Article> call(ListView<Article> listView) {

				return new ListCell<Article>(){
					private Label lb_id;
					private Label lb_title;
					private Label lb_update;
					private Button btn_edit;
					private Button btn_delete;

					protected void updateItem(Article item, boolean empty) {
						super.updateItem(item, empty);// 必须调用，否则错乱的bug
						// ======================
						Parent convertView = null;
						if(getGraphic() == null){
							convertView = LayoutInflater.inflate("item_article", Parent.class);
							lb_id = (Label) convertView.lookup("#lb_id");
							lb_title = (Label) convertView.lookup("#lb_title");
							lb_update = (Label) convertView.lookup("#lb_update");
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
								DialogHelper.confim("操作确认", "警告！你真的要删除？", new OnClickListener() {

									@Override
									public void onClick(Stage stage) {
										stage.close();
										ArticleDao.getInstance().delete(item.getId());// delete database
										// update ListView items
										listView.getItems().remove(item);// remove listItem
									}
								}, new OnClickListener(){
									@Override
									public void onClick(Stage stage) {
										stage.close();
									}
								});
							});

							btn_edit.setOnAction(e->{
								// update
								createDialog(item);
							});

							convertView.setOnMouseClicked(e->{
								if(e.getClickCount() == 2){
									DialogHelper.showArticle(item.getContent());
								}
							});
							lb_id.setText(item.getId()+"");
							lb_title.setText(item.getTitle());
							lb_update.setText(item.getUpdateTime());
							// setText(null);
							setGraphic(convertView);
						}
						// ======================
					};
				};
			}
		});

	}

	private void createDialog(Article listItem) {
		List<Category> categories = CategoryDao.getInstance().findAll();

		AlertDialog.Builder builder = new AlertDialog.Builder();
		builder.title("文章编辑");
		builder.view("dialog_article_edit")
		.build();
		alertDialog = builder.build();
		HTMLEditor htmlEditor = alertDialog.findView("#et_html", HTMLEditor.class);
		ChoiceBox<Category> choiceBox = alertDialog.findView("#choiceBox", ChoiceBox.class);
		Button btn_confirm = alertDialog.findView("#btn_confirm", Button.class);
		Button btn_cancel = alertDialog.findView("#btn_cancel", Button.class);

		Label lb_articleId = alertDialog.findView("#lb_articleId", Label.class);
		TextField et_title = alertDialog.findView("#et_title", TextField.class);
		Label lb_categoryId = alertDialog.findView("#lb_categoryId", Label.class);
		Label lb_error = alertDialog.findView("#lb_error", Label.class);

		if(listItem.getId() != null) lb_articleId.setText(listItem.getId() + "");
		if(listItem.getTitle() != null) et_title.setText(listItem.getTitle());
		if(listItem.getContent() != null) htmlEditor.setHtmlText(listItem.getContent());
		if(listItem.getCategoryId() != null) lb_categoryId.setText(listItem.getCategoryId() + "");

		choiceBox.getItems().addAll(categories);
		for(Category category : categories){
			if(category.getId() == listItem.getCategoryId()){
				choiceBox.getSelectionModel().select(category);
			}
		}

		choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Category>() {

			@Override
			public void changed(ObservableValue<? extends Category> observable, Category oldValue, Category newValue) {
				lb_categoryId.setText(newValue.getId() + "");
			}
		});

		choiceBox.setConverter(new StringConverter<Category>() {

			@Override
			public String toString(Category category) {
				return category.getName();
			}

			@Override
			public Category fromString(String str) {
				// choiceBox 不使用此方法(把字符串转换为对象),ComboBox组件才用到此方法
				return null;
			}
		});
		btn_confirm.setOnAction(ee ->{
			Long articleId = null,categoryId = Constants.DEFAULT_CATEGORY;// 默认类别
			if(!lb_articleId.getText().equals("")){
				articleId = Long.parseLong(lb_articleId.getText());
			}
			if(!lb_categoryId.getText().equals("")){
				categoryId = Long.parseLong(lb_categoryId.getText());
			}
			String title = et_title.getText();
			String content = htmlEditor.getHtmlText();

			if(title.equals("") || content.equals("")){
				lb_error.setText("标题或内容不能为空!");
				return;
			}else{
				lb_error.setText("");
			}
			// update ListView items & database
			listItem.setId(articleId);
			listItem.setCategoryId(categoryId);
			listItem.setTitle(title);
			listItem.setContent(content);
			ArticleDao.getInstance().saveOrUpdate(listItem);
			if(articleId == null){
				// 新建数据,需要刷新ListView
				loadData(pager_article.getCurrentPageIndex());
			}
			alertDialog.close();
		});

		btn_cancel.setOnAction(eee->{
			alertDialog.close();
		});

		alertDialog.show();
	}

	private void loadData(int page) {

		ThreadUtils.run(() ->{
			Result<Article> result;
			synchronized (ArticleFragment.class) {
				 result = ArticleDao.getInstance().getPage(page);
			}
			Platform.runLater(()->{
				listView.getItems().clear();
				List<Article> datas = result.recordList;
				listView.getItems().addAll(datas);

				pager_article.setPageCount(result.pageCount);// 注意页数的动态变化
				if(FIRST){
					pager_article.setVisible(true);
					FIRST = false;
				}
			});
		});

	}
}
