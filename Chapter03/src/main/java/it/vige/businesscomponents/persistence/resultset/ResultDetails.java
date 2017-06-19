package it.vige.businesscomponents.persistence.resultset;

public class ResultDetails {

    private Integer id;
    private String title;
    private Integer orderCount;
    private Integer postCount;

	public ResultDetails(Integer id, String title, Integer orderCount, Integer postCount) {
		this.id = id;
		this.title = title;
		this.orderCount = orderCount;
		this.postCount = postCount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}

	public Integer getPostCount() {
		return postCount;
	}

	public void setPostCount(Integer postCount) {
		this.postCount = postCount;
	}
}
