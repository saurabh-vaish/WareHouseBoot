package com.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	public User findByUserEmail(String userEmail);
	
	@Query("select u.gender,count(u.gender) from User as u group by u.gender")
	public List<Object[]> findUserCount();
}
