package application.test;

public class CascadeTest {

	public static void main(String[] args) {

		CategoryDaoTest.save(5);
		CategoryDaoTest.list();
//
		ArticleDaoTest.save(30, 1L);
		ArticleDaoTest.save(30, 2L);

//		List<Article> list = ArticleDao.getInstance().findArticleBy(1L);
//		List<Article> list = ArticleDao.getInstance().findArticleBy("Àà±ð  : 1");
//		System.out.println(list);

	}
}
