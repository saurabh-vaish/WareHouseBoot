package com.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.model.SaleOrder;

public interface SaleOrderRepository extends JpaRepository<SaleOrder, Integer>{

	
	public List<SaleOrder> findByOrderCode(String orderCode);
	
	
	@Query("select s.saleId,s.orderCode from com.app.model.SaleOrder as s where status= :status")
	public List<ViewE> findInvoiceSaleOrderByStatus(String status);
	
	
	interface ViewE
	{
		Integer getSaleId();
		String getOrderCode();
	}
	
}
