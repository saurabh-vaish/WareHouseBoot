package com.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="sales_dtls_tab")
public class SalesDetails {

	@Id
	@Column(name="sd_id")
	@GeneratedValue(generator="sd_gen")
	@GenericGenerator(name="sd_gen",strategy="increment")
	private Integer salesDtlsId;
	
	@Column(name="so_ho_id")
	private Integer soHoId;
	
	@Transient
	@Column(name="slno")
	private int slno;
	
	@Column(name="qnty")
	private Long quantity;
	
	@Column(name="ship_status")
	private String shipStatus;
	
	@Column(name="cost")
	private double baseCost;
	
	@ManyToOne
	@JoinColumn(name="itemId")
	private Item item;


	public SalesDetails(Integer salesDtlsId) {
		super();
		this.salesDtlsId = salesDtlsId;
	}

	
}
