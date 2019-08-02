package com.app.model;

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="usertab")
public class User {

	@Id
	@GeneratedValue(generator="user")
	@GenericGenerator(name="user",strategy="increment")
	@Column(name="uid")
	private Integer userId;
	
	@Column(name="un")
	private String userName;
	
	@Column(name="email")
	private String userEmail;
	
	@Column(name="pwd")
	private String userPwd;
	
	@Column(name="mobile")
	private String userMobile;
	
	@Column(name="gender")
	private String gender;
	
	
	@ElementCollection(fetch=FetchType.EAGER) // for data page also
	@CollectionTable(name="user_roles_tab",
				joinColumns=@JoinColumn(name="uid")       // key column
	)
	// @OrderColumn not needed bcs we are using set here 
	@Column(name="roles")  //element column
	private Set<String> roles;
	
	
	public User(Integer userId) {
		super();
		this.userId = userId;
	}

}
