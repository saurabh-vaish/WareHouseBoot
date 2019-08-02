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
@Table(name="uomtab")
public class Uom {
	
	@Id
	@GeneratedValue(generator="uom")
	@GenericGenerator(name="uom",strategy="increment")
	@Column(name="id")
	private Integer id;
	
	@Column(name="type")
	private String uomType;
	
	@Column(name="code")
	private String uomCode;
	
	@Column(name="enable")
	private String enableUom;
	
	@Column(name="metacode")
	private String metaCode;
	
	@Column(name="size")
	private String adjSize;
	
	@Column(name="note")
	private String note;
	

	public Uom(Integer id) {
		super();
		this.id = id;
	}

	
	
}