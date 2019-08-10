package com.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.model.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer>{

	public List<Purchase> findByOrderCode(String orderCode);
	
	@Query("select p.purId,p.orderCode from com.app.model.Purchase as p where p.status= :status")
	public List<Object[]> findInvoicePurchseOrderByStatus(String status);
	
	
	
}
