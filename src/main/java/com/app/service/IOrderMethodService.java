package com.app.service;

import java.util.List;
import java.util.Map;

import com.app.model.OrderMethod;

public interface IOrderMethodService {

	public Integer saveOrderMethod(OrderMethod orderMethod);
	public void updateOrderMethod(OrderMethod orderMethod);
	public void deleteOrderMethod(Integer id);
	public OrderMethod getOrderMethodById(Integer id);
	public List<OrderMethod> getAllOrderMethods();
	
	public List<Object[]> getOrderMethodCountByMode();
	public Map<Integer, String> getAllOrderMethodIdsAndCodes();

}
