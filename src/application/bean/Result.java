package application.bean;

import java.util.List;

public class Result<T> {

	public int currentPage;// 当前页
	public int totalCount;// 总记录
	public List<T> recordList;// 结果集
	public int pageCount; // 总页数

	public Result(int currentPage, int totalCount, List<T> recordList, int pageCount) {
		this.currentPage = currentPage;
		this.totalCount = totalCount;
		this.recordList = recordList;
		this.pageCount = pageCount;
	}

	@Override
	public String toString() {
		return "Result [currentPage=" + currentPage + ", recordList's length = " + recordList.size() + ", totalCount=" + totalCount + ", pageCount=" + pageCount + "]";
	}
}