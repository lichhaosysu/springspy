package com.lichhao.springspy;

import java.util.List;

public class Pagination {
	public static final int PAGE_SIZE = 30; // 每一页显示15条
	public static final int PAGE_RANGE = 10; // 分页条上面每次显示5页
	private List<PageItem> pageItems;

	public List<PageItem> getPageItems() {
		return pageItems;
	}

	public void setPageItems(List<PageItem> pageItems) {
		this.pageItems = pageItems;
	}
}
