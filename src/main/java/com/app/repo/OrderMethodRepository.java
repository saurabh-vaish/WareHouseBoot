package com.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.model.OrderMethod;

public interface OrderMethodRepository extends JpaRepository<OrderMethod, Integer>{

	@Query("select count(o.orderMode),o.orderMode from com.app.model.OrderMethod as o group by o.orderMode")
	public List<Object[]> findOrderModeCountByMode();
	
	@Query("select o.orderId,o.orderCode from com.app.model.OrderMethod as o")
	public List<ViewC> findByOrderIdAndOrderCode();
	
	interface ViewC
	{
		Integer getOrderId();
		String getOrderCode();
	}
	
}
