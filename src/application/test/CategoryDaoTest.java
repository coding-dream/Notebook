package application.test;

import java.util.List;

import application.bean.Category;
import application.dao.CategoryDao;

public class CategoryDaoTest {
	public static void main(String[] args) {
//		save(5);
		delete(1L);
//		delete(2L);
//		update();
//		get(4L);
		list();
	}

	public static void update() {
		Category category = get(4L);
		category.setName("类别4: update");
		CategoryDao.getInstance().update(category);
	}

	public static Category get(Long id) {
		Category category = CategoryDao.getInstance().getById(id);
		return category;
	}

	public static void delete(Long id) {
		CategoryDao.getInstance().delete(id);
	}

	public static void save(int size) {
		for(int i = 0;i < size;i++){
			Category entity = new Category();
			entity.setName("类别  : " + (i+1));
			CategoryDao.getInstance().save(entity);
		}
	}

	public static void list() {
		List<Category> list = CategoryDao.getInstance().findAll();
		for(Category category : list){
			System.out.println(category);
		}
	}
}
