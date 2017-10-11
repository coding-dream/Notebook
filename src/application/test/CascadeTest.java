package application.test;

import java.util.List;

import application.bean.Article;
import application.dao.ArticleDao;

public class CascadeTest {

	public static void main(String[] args) {

//		CategoryDaoTest.save(5);
		CategoryDaoTest.list();
//
//		ArticleDaoTest.save(5, 1L);
//		ArticleDaoTest.save(5, 2L);

//		List<Article> list = ArticleDao.getInstance().findArticleBy(1L);
		List<Article> list = ArticleDao.getInstance().findArticleBy("Àà±ð  : 1");
		System.out.println(list);

	}
}
