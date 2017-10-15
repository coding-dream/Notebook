package application.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import application.bean.Category;
import application.bean.Result;
import application.db.BaseDao;
import application.db.DBHelper;
import application.util.L;

public class CategoryDao implements BaseDao<Category> {
	private String tableName = getEntityClass().getSimpleName();
	private static CategoryDao articleDao;

	private CategoryDao(){
		createTable();
	}
	public static CategoryDao getInstance() {
        if (articleDao == null) {
            synchronized (CategoryDao.class) {
                if (articleDao == null) {
                	articleDao = new CategoryDao();
                }
            }
        }
        return articleDao;
    }

    private void createTable() {
    	 String sql1 = String.format("create table if not exists %s(id integer primary key autoincrement, name text)", tableName);
    	 DBHelper.execSQL(sql1);
    	 String sql2 = String.format("create trigger if not exists lw_trigger before DELETE ON category "
    	 		+ "FOR EACH ROW "
    	 		+ "BEGIN "
    	 		+ "delete from article where old.id = article.categoryid; "
    	 		+ "END;");
    	 DBHelper.execSQL(sql2);
	     L.D("createTable success");
    }

    public void dropTable() {
    	String sql = String.format("drop table if exists %s", tableName);
    	DBHelper.execSQL(sql);
    	L.D("dropTable success");
    }

	@Override
	public void save(Category entity) {
		String sql = String.format("insert into %s(name) values(?)", tableName);
		DBHelper.execSQL(sql, entity.getName());
	}

	@Override
	public void delete(Long id) {
		String sql1 = "PRAGMA foreign_keys=ON;";
		// 设置了级联删除和级联更新
        // sqlite 在执行有级联关系的语句的时候必须先设置"PRAGMA foreign_keys=ON"
        // 否则级联关系默认失效
		String sql2 = String.format("delete from %s where id = ?", tableName);

		String sqls[] = new String[]{sql1,sql2};
		String params[][] = new String[2][];
		params[0] = null;
		params[1] = new String[]{id + "" };

		try {
			DBHelper.execSQLAll(sqls, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		L.D(id + " delete success");
	}

	@Override
	public void update(Category entity) {
		String sql = String.format("update %s set name = ? where id = ?", tableName);
		DBHelper.execSQL(sql, new String[] { entity.getName(), entity.getId() + "" });
		L.D("update success");
	}

	@Override
	public Category getById(Long id) {
		String sql = String.format("select * from %s where id = ? ", tableName);
		List<Map<String, String>> results = DBHelper.rawSQLMapList(sql,  id + "");
		Category category = null;
		if(results.size() > 0){
			Map<String,String> result = results.get(0);
			category = new Category();
			category.setId(Long.parseLong(result.get("id")));
			category.setName(result.get("name"));
		}
		return category;
	}

	@Override
	public List<Category> findAll() {
		List<Category> list = new ArrayList<Category>();
		String sql = String.format("select * from %s order by id", tableName);
		List<Map<String, String>> results = DBHelper.rawSQLMapList(sql);
		Category info = null;
		for(Map<String,String> result : results){
			info = new Category();
        	info.setId(Long.parseLong(result.get("id")));
        	info.setName(result.get("name"));
        	list.add(info);
        }
		return list;
	}

	@Override
	public Result<Category> getPage(int currentPage) {
		throw new RuntimeException("not implement");
	}

	@Override
	public int count(){
		String sql = String.format("select count(*) from %s", tableName);
		List<Object[]> results = DBHelper.rawSQLObjsList(sql);
		int count = 0;
		if(results.size() > 0){
			count = Integer.parseInt(results.get(0)[0] + "");
		}
		return count;
	}

	@Override
	public Class getEntityClass() {
		return Category.class;
	}

	@Override
	public List<Category> search(String text) {
		throw new RuntimeException("not implement");
	}

	@Override
	public void saveOrUpdate(Category entity) {
		if(entity.getId() == null){
			save(entity);
		}else{
			update(entity);
		}
	}
}
