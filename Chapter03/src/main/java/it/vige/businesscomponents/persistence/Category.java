package it.vige.businesscomponents.persistence;

import static javax.persistence.CascadeType.REMOVE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.search.annotations.Indexed;

/**
 * Category of forums.
 */

@NamedQueries({
		@NamedQuery(name = "findCategoryByIdFetchForums", query = "select c from Category as c join fetch c.forums "
				+ "where c.id=:categoryId"),
		@NamedQuery(name = "findCategories", query = "select c from Category as c " + "order by c.order asc"),
		@NamedQuery(name = "findCategoriesFetchForums", query = "select c from Category as c "
				+ "left outer join fetch c.forums " + "order by c.order asc"),
		@NamedQuery(name = "getLastCategoryOrder", query = "select max(c.order) from Category " + "as c ") })
@Entity
@Table(name = "JBP_FORUMS_CATEGORIES")
@Indexed(index = "indexes/categories")
public class Category implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8164247625235206934L;
	@OneToMany(mappedBy = "category", cascade = REMOVE)
	@OrderBy("order ASC")
	private List<Forum> forums;

	@Column(name = "JBP_ORDER")
	private int order;

	@Column(name = "JBP_TITLE")
	private String title;

	@Id
	@Column(name = "JBP_ID")
	@GeneratedValue
	private Integer id;

	/**
	 * Creates a new {@link Category} object.
	 */
	public Category() {
		setForums(new ArrayList<Forum>());
	}

	public Category(String title) {
		this();
		this.title = title;
	}

	/**
	 * @return the list of forums of the category
	 */
	public List<Forum> getForums() {
		return forums;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param value
	 *            DOCUMENT_ME
	 */
	public void setForums(List<Forum> value) {
		forums = value;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param value
	 *            DOCUMENT_ME
	 */
	public void addForum(Forum value) {
		value.setCategory(this);
		forums.add(value);
	}

	/**
	 * @return the order number of the category
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param order
	 *            DOCUMENT_ME
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * @return the title of the category
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param title
	 *            DOCUMENT_ME
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the id of teh category
	 */
	public Integer getId() {
		return id;
	}
}