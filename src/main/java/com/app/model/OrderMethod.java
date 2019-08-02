package com.app.model;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="ordertab")
public class OrderMethod {

	@Id
	@GeneratedValue(generator="orderMethod")
	@GenericGenerator(name="orderMethod",strategy="increment")
	@Column(name="omid")
	private Integer orderId;
	
	@Column(name="ommode")
	private String orderMode;
	
	@Column(name="omcode")
	private String orderCode;
	
	@Column(name="omtype")
	private String exeType;
	
	@ElementCollection(fetch=FetchType.EAGER) // for collection type and fetch for getting data from all child table
	@CollectionTable(name="omaccpettab",  // child table name
		joinColumns=@JoinColumn(name="omid")   // for key column
	)  
	@OrderColumn(name="pos")  // position column
	@Column(name="data")  // element column
	private List<String> orderAccpet;
	
	@Column(name="onote")
	private String orderNote;
	
	
	public OrderMethod(Integer orderId) {
		super();
		this.orderId = orderId;
	}

	
	
}
