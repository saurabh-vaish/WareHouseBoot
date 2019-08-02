package com.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="docstab")
public class Document {

	@Id
	@GeneratedValue
	@Column(name="fid")
	private Integer fileId;
	
	@Column(name="fname")
	private String fileName;
	
	@Lob
	@Column(name="fdata")
	private byte[] fileData;

}
