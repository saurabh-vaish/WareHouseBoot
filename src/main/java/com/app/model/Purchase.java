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
@Table(name="purchaseTab")
public class Purchase {

	@Id
	@GeneratedValue(generator="purchase")
	@GenericGenerator(name="purchase",strategy="increment")
	@Column(name="pid")
	private Integer purId;
	
	@Column(name="ocode")
	private String orderCode;
	
	@Column(name="refnum")
	private String refNum;
	
	@Column(name="qcheck")
	private String qualityCheck;
	
	@Column(name="status")
	private String status="OPEN";
	
	@Column(name="note")
	private String note;
	
	@ManyToOne
	@JoinColumn(name="sidfk")
	private ShipmentType shipmentCode;   // integration with Shipment Type
	
	
	@ManyToOne
	@JoinColumn(name="whidfk")
	private WhUser vendor;			// integration with WhUser

	//child items data
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="order_id_fk")
	private List<PurchaseDtl> details=new ArrayList<>(0);

	
	public Purchase(Integer purId) {
		super();
		this.purId = purId;
	}
	
	
}
