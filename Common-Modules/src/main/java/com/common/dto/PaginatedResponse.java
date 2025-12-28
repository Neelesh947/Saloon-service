package com.common.dto;

import java.util.List;

public class PaginatedResponse<T> {
	private List<T> content; // main list
	private PageInfo pageInfo; // metadata at bottom

	public PaginatedResponse(List<T> content, long totalElements, int pageNumber, int pageSize) {
		this.content = content;
		this.pageInfo = new PageInfo(totalElements, pageNumber, pageSize);
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}

	// Inner class for metadata
	public static class PageInfo {
		private long totalElements;
		private int pageNumber;
		private int pageSize;

		public PageInfo(long totalElements, int pageNumber, int pageSize) {
			this.totalElements = totalElements;
			this.pageNumber = pageNumber;
			this.pageSize = pageSize;
		}

		public long getTotalElements() {
			return totalElements;
		}

		public void setTotalElements(long totalElements) {
			this.totalElements = totalElements;
		}

		public int getPageNumber() {
			return pageNumber;
		}

		public void setPageNumber(int pageNumber) {
			this.pageNumber = pageNumber;
		}

		public int getPageSize() {
			return pageSize;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}
	}
}