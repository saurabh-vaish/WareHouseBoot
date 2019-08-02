package com.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.app.model.PurchaseDtl;

public interface PurchaseDtlRepository extends JpaRepository<PurchaseDtl, Integer>{

	
	@Query("update com.app.model.PurchaseDtl set grnStatus=:status where grnStatus is null and poHdrId=:id")
	@Modifying
	@Transactional
	public Integer updatePurchaseDtlBasedOnStatus(String status,Integer id); 
	
	
	@Query("select count(p.orderDtlId) from com.app.model.PurchaseDtl as p where p.poHdrId=:orderId and p.grnStatus=null")
	public Integer findNullGrnStatusCount(Integer orderId);
	
}
