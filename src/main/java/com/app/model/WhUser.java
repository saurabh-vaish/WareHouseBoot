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
@Table(name="whUsertab")
public class WhUser {

	@Id
	@GeneratedValue(generator="whuser")
	@GenericGenerator(name="whuser",strategy="increment")
	@Column(name="id")
	private Integer whId;
	
	@Column(name="type")
	private String whType;
	
	@Column(name="code")
	private String whCode;
	
	@Column(name="name")
	private String whName;	
	
	@Column(name="whfor")
	private String whFor;
	
	@Column(name="email")
	private String whEmail;
	
	@Column(name="contact")
	private String whContact;
	
	@Column(name="idtype")
	private String whIdType;
	
	@Column(name="number")
	private String whNum;
	
	public WhUser(Integer whId) {
		super();
		this.whId = whId;
	}

	
}
