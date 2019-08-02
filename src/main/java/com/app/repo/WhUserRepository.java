package com.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.model.WhUser;

public interface WhUserRepository extends JpaRepository<WhUser, Integer>{

	
	@Query("select w.whFor,count(w.whFor) from com.app.model.WhUser as w group by w.whFor ")
	public List<Object[]> findWhUserCountByType();

	
	@Query("select w.whId,w.whCode from com.app.model.WhUser as w where w.whType=:whType")
	public List<ViewG> findAllWhUserByType(String whType);
	
	interface ViewG
	{
		Integer getWhId();
		String getWhCode();
	}
}
