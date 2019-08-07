package com.app.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="saleorderTab")
public class SaleOrder {

	@Id
	@GeneratedValue(generator="salesgen")
	@GenericGenerator(name="salesgen",strategy="increment")
	
	@Column(name="saleid")
	private Integer saleId;
	
	@Column(name="ocode")
	private String orderCode;
	
	@Column(name="refnum")
	private String refNum;
	
	@Column(name="stockmode")
	private String stockMode;
	
	@Column(name="stockSource")
	private String stockSource;
	
	@Column(name="status")
	private String status="OPEN";
	
	@Column(name="note")
	private String note;
	
	@ManyToOne
	@JoinColumn(name="sidfk")
	private ShipmentType shipmentCode;   // integration with Shipment Type
	
	
	@ManyToOne
	@JoinColumn(name="whidfk")
	private WhUser customer;			// integration with WhUser

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="saleOrderId")
	private List<SalesDetails> salesDetails = new ArrayList<>();


	public SaleOrder(Integer saleId) {
		super();
		this.saleId = saleId;
	}

	
	
}
