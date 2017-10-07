package application.test;

import java.util.List;
import java.util.Random;

import application.bean.Article;
import application.bean.Result;
import application.dao.ArticleDao;

public class DaoTest {
	public static void main(String[] args) {
//		save(30);
//		delete(1L);
//		update();
//		get(4L);
		list();
//		getPage(1);
	}

	private static void getPage(int page) {
		Result<Article> result = ArticleDao.getInstance().getPage(page);
		System.out.println(result);
	}

	private static void update() {
		Article article = get(4L);
		article.setTitle("change 4L title");
		ArticleDao.getInstance().update(article);
	}

	private static Article get(Long id) {
		Article article = ArticleDao.getInstance().getById(id);
		return article;
	}

	private static void delete(Long id) {
		ArticleDao.getInstance().delete(id);
	}

	private static void save(int size) {
		for(int i = 0;i < size;i++){
			Article entity = new Article();
			entity.setTitle("µÚÒ»ÆªÎÄÕÂ" + new Random().nextInt(100));
			entity.setContent("Hello Jack,I love you so much!");
			ArticleDao.getInstance().save(entity);
		}
	}

	private static void list() {
		List<Article> list = ArticleDao.getInstance().findAll();
		for(Article article : list){
			System.out.println(article);
		}
	}
}
