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
@Table(name="shipping")
public class Shipping {

	@Id
	@GeneratedValue(generator="ship_gen")
	@GenericGenerator(name="ship_gen",strategy="increment")
	@Column(name="ship_id")
	private Integer shipId;
	
	@Column(name="ship_code")
	private String shipCode;
	
	@Column(name="ship_ref_num")
	private String shipRefNum;
	
	@Column(name="cour_ref_num")
	private String courRefNum;
	
	@Column(name="cor_dtls")
	private String couContdtls;
	
	@Column(name="ship_desc")
	private String shipDesc;
	
	@Column(name="bill_addr")
	private String billAddr;
	
	@Column(name="ship_addr")
	private String shipAddr;
	
	@ManyToOne
	@JoinColumn(name="saleOrderId")
	private SaleOrder saleOrder;

	

	public Shipping(Integer shipId) {
		super();
		this.shipId = shipId;
	}

	
}
