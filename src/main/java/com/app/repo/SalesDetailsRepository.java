package com.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.app.model.SalesDetails;

public interface SalesDetailsRepository extends JpaRepository<SalesDetails, Integer>{

	

	@Query("update com.app.model.SalesDetails as s set s.shipStatus=:status where s.shipStatus is null and s.soHoId=:id")
	@Modifying
	@Transactional
	public Integer updateSalesDetailsBasedOnStatus(String status,Integer id);
	
	
	@Query("select count(s.salesDtlsId) from com.app.model.SalesDetails as s where s.soHoId=:orderId and s.shipStatus=null")
	public Long findNullShippingStatusCount(Integer orderId);
	
	
}
