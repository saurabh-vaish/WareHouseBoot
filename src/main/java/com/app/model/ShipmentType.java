package com.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="shipmenttab")
public class ShipmentType {
	
	@Id
	@GeneratedValue(generator="shipment1")
	@GenericGenerator(name="shipment1",strategy="increment")
	@Column(name="id")
	private Integer shipmentId;
	
	@Column(name="mode")
	private String shipmentMode;
	
	@Column(name="code")
	private String shipmentCode;
	
	@Column(name="enable")
	private String enableShipment;
	
	@Column(name="grade")
	private String shipmentGrade;
	
	@Column(name="note")
	private String note;

	
	public ShipmentType(Integer shipmentId) {
		super();
		this.shipmentId = shipmentId;
	}
	
	
}
