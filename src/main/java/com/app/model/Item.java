package com.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="itemTab")
public class Item {

	@Id
	@GeneratedValue(generator="item")
	@GenericGenerator(name="item",strategy="increment")
	@Column(name="id")
	private Integer itemId;
	
	@Column(name="code")
	private String itemCode;
	
	@Column(name="length")
	private Double itemLength;
	
	@Column(name="width")
	private Double itemWidth;
	
	@Column(name="height")
	private Double itemHeight;
	
	@Column(name="cost")
	private String baseCost;
	
	@Column(name="currency")
	private String baseCurr;
	
	@Column(name="note")
	private String note;
	
	@ManyToOne						// multiplicity
	@JoinColumn(name="uomIdFk")		// join column
	private Uom uom ;  // integrating with Uom , Has-A relation
	
	@ManyToOne
	@JoinColumn(name="orderIdFk")
	private OrderMethod order ;   // integrating with OrderMethod , Has-A relation

	
	@ManyToOne
	@JoinColumn(name="whUserIdFk1")
	private WhUser user1 ;		 // integrating with WhUser , Has-A relation
	
	@ManyToOne
	@JoinColumn(name="whUserIdFk2")
	private WhUser user2 ;  	 // integrating with WhUser , Has-A relation
	

	public Item(Integer itemId) {
		super();
		this.itemId = itemId;
	}


	
}
