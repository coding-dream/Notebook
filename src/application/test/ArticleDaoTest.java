package application.test;

import java.util.List;

import application.bean.Article;
import application.bean.Result;
import application.dao.ArticleDao;

public class ArticleDaoTest {
	public static void main(String[] args) {
//		save(5,1L);
//		save(5,2L);
//		delete(1L);
//		update();
//		get(4L);
		list();
//		getPage(1);
	}

	public static void getPage(int page) {
		Result<Article> result = ArticleDao.getInstance().getPage(page);
		System.out.println(result);
	}

	public static void update() {
		Article article = get(4L);
		article.setTitle("ндуб4: update");
		ArticleDao.getInstance().update(article);
	}

	public static Article get(Long id) {
		Article article = ArticleDao.getInstance().getById(id);
		return article;
	}

	public static void delete(Long id) {
		ArticleDao.getInstance().delete(id);
	}

	public static void save(int size,Long categoryId) {
		for(int i = 0;i < size;i++){
			Article entity = new Article();
			entity.setTitle("ндуб : " + (i+1));
			entity.setContent("Hello Jack,I love you so much!");
			entity.setCategoryId(categoryId);
			ArticleDao.getInstance().save(entity);
		}
	}

	public static void list() {
		List<Article> list = ArticleDao.getInstance().findAll();
		for(Article article : list){
			System.out.println(article);
		}
	}
}
