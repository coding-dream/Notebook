package application.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import application.Constants;
import application.bean.Article;
import application.bean.Result;
import application.db.BaseDao;
import application.db.DBHelper;
import application.util.L;

public class ArticleDao implements BaseDao<Article> {
	private String tableName = getEntityClass().getSimpleName();
	private static ArticleDao articleDao;

	private ArticleDao(){
		createTable();
	}
	public static ArticleDao getInstance() {
        if (articleDao == null) {
            synchronized (ArticleDao.class) {
                if (articleDao == null) {
                	articleDao = new ArticleDao();
                }
            }
        }
        return articleDao;
    }

    private void createTable() {
    	 String sql = String.format("create table if not exists %s(id integer primary key autoincrement, title text, content text,createTime text, updateTime text,categoryId integer,foreign key (categoryId) references Category(id) on delete cascade on update cascade)", tableName);
	     DBHelper.execSQL(sql, null);
	     L.D("createTable success");
    }

    public void dropTable() {
    	String sql = String.format("drop table if exists %s", tableName);
    	DBHelper.execSQL(sql, null);
    	L.D("dropTable success");
    }

	@Override
	public void save(Article entity) {
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] params = new String[] { entity.getTitle(), entity.getContent(), sf.format(date),sf.format(date),entity.getCategoryId() + "" };
		String sql = String.format("insert into %s(title,content,createTime,updateTime,categoryId) values(?,?,?,?,?)", tableName);
		DBHelper.execSQL(sql, params);
	}

	@Override
	public void delete(Long id) {
		String sql = String.format("delete from %s where id = ?", tableName);
		DBHelper.execSQL(sql, new String[] { id + "" });
		L.D(id + " delete success");
	}

	@Override
	public void update(Article entity) {
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = String.format("update %s set title = ?, content = ?, updateTime = ?,categoryId = ? where id = ?", tableName);
		DBHelper.execSQL(sql, new String[] { entity.getTitle(), entity.getContent(), sf.format(date),entity.getCategoryId() + "", entity.getId() + ""});
		L.D("update success");
	}

	@Override
	public Article getById(Long id) {
		String sql = String.format("select * from %s where id = ? ", tableName);
		List<Map<String, String>> results = DBHelper.rawSQLMapList(sql, new String[] { id + "" });
		Article article = null;
		if(results.size() > 0){
			Map<String,String> result = results.get(0);
			article = new Article();
			article.setId(Long.parseLong(result.get("id")));
			article.setTitle(result.get("title"));
			article.setContent(result.get("content"));
			article.setCreateTime(result.get("createTime".toLowerCase()));
			article.setUpdateTime(result.get("updateTime".toLowerCase()));
			article.setCategoryId(Long.parseLong(result.get("categoryId".toLowerCase())));
		}
		return article;
	}

	@Override
	public List<Article> findAll() {
		List<Article> list = new ArrayList<Article>();
		String sql = String.format("select * from %s order by id", tableName);
		List<Map<String, String>> results = DBHelper.rawSQLMapList(sql, null);
		Article info = null;
		for(Map<String,String> result : results){
			info = new Article();
        	info.setId(Long.parseLong(result.get("id")));
        	info.setTitle(result.get("title"));
        	info.setContent(result.get("content"));
        	info.setCreateTime(result.get("createTime".toLowerCase()));
        	info.setUpdateTime(result.get("updateTime".toLowerCase()));
        	info.setCategoryId(Long.parseLong(result.get("categoryId".toLowerCase())));
        	list.add(info);
        }
		return list;
	}

	@Override
	public Result<Article> getPage(int currentPage) {
		List<Article> list = new ArrayList<Article>();

		int firstResult = (currentPage - 1) * Constants.PAGE_SIZE;
		String sql = String.format("select * from %s order by id limit %d,%d", tableName,firstResult,Constants.PAGE_SIZE);
		List<Map<String, String>> results = DBHelper.rawSQLMapList(sql, null);
		for(Map<String,String> result : results){
			Article info = new Article();
        	info.setId(Long.parseLong(result.get("id")));
        	info.setTitle(result.get("title"));
        	info.setContent(result.get("content"));
        	info.setCreateTime(result.get("createTime".toLowerCase()));
        	info.setUpdateTime(result.get("updateTime".toLowerCase()));
        	info.setCategoryId(Long.parseLong(result.get("categoryId".toLowerCase())));
        	list.add(info);
        }

		int totalCount = count();
		int pageCount = (totalCount + Constants.PAGE_SIZE - 1) / Constants.PAGE_SIZE;// totalCount + Constants.PAGE_SIZE - 1 就是 totalRecord / PAGE_SIZE 的最大的余数
		Result<Article> result = new Result<>(currentPage, totalCount, list, pageCount);
		return result;
	}

	@Override
	public int count(){
		String sql = String.format("select count(*) from %s", tableName);
		List<Object[]> results = DBHelper.rawSQLObjsList(sql, null);
		int count = 0;
		if(results.size() > 0){
			count = Integer.parseInt(results.get(0)[0] + "");
		}
		return count;
	}

	@Override
	public Class getEntityClass() {
		return Article.class;
	}

	@Override
	public List<Article> search(String text) {
		String query = "%" + text + "%";
		List<Article> list = new ArrayList<Article>();
		String sql = String.format("select * from %s where title like '%s' order by id", tableName,query);
		List<Map<String, String>> results = DBHelper.rawSQLMapList(sql, null);
		for(Map<String,String> result : results){
			Article info = new Article();
        	info.setId(Long.parseLong(result.get("id")));
        	info.setTitle(result.get("title"));
        	info.setContent(result.get("content"));
        	info.setCreateTime(result.get("createTime".toLowerCase()));
        	info.setUpdateTime(result.get("updateTime".toLowerCase()));
        	info.setCategoryId(Long.parseLong(result.get("categoryId".toLowerCase())));
        	list.add(info);
        }
		return list;
	}
}
