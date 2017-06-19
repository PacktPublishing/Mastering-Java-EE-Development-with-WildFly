package it.vige.businesscomponents.persistence.resultset;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(name = "CustomerDetailsResult", classes = {
		@ConstructorResult(targetClass = ResultDetails.class, columns = {
				@ColumnResult(name = "id", type = Integer.class), @ColumnResult(name = "title"),
				@ColumnResult(name = "orderCount", type = Integer.class),
				@ColumnResult(name = "postCount", type = Integer.class) }) })
@Entity
public class ResultEntity {

	@Id
	@GeneratedValue
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
