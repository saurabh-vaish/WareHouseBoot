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
@Table(name="podtl_tab")
public class PurchaseDtl {

	@Id
	@Column(name="dtl_id")
	@GeneratedValue(generator="purchaseDtl")
	@GenericGenerator(name="purchaseDtl",strategy="increment")
	private Integer orderDtlId;
	
	@Column(name="poHdr_Id")
	private Integer poHdrId;

	@Transient
	@Column(name="dtl_slno")
	private int slno;

	@ManyToOne
	@JoinColumn(name="item_id_fk")
	private Item itemDtl;

	@Column(name="bs_cost")
	private Double baseCost;
	
	@Column(name="grn_status")
	private String grnStatus;
	
	@Column(name="itm_qty")
	private Long itemsQty;
	
	@Column(name="line_val")
	private Double lineValue;

	
	public PurchaseDtl(Integer orderDtlId) {
		super();
		this.orderDtlId = orderDtlId;
	}

	
}
