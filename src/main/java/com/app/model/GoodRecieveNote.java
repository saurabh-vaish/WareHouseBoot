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
@Table(name="grn_tab")
public class GoodRecieveNote {
	
	@Id
	@GeneratedValue(generator="grn_gen")
	@GenericGenerator(name="grn_gen",strategy="increment")
	@Column(name="grn_id")
	private Integer grnId;
	
	@Column(name="grn_code")
	private String grnCode;
	
	@Column(name="grn_type")
	private String grnType;
	
	@Column(name="grn_desc")
	private String grnDesc;
	
	@ManyToOne
	@JoinColumn(name="purchaseId")
	private Purchase purchase;

	

	public GoodRecieveNote(Integer grnId) {
		super();
		this.grnId = grnId;
	}
	
	
}
