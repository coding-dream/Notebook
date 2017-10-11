package application.test;

public class CascadeTest {

	public static void main(String[] args) {
		CategoryDaoTest.save(5);
		CategoryDaoTest.list();

		ArticleDaoTest.save(5, 1L);
		ArticleDaoTest.save(5, 2L);

		CategoryDaoTest.delete(1L);
		CategoryDaoTest.delete(2L);

		ArticleDaoTest.list();
	}
}
