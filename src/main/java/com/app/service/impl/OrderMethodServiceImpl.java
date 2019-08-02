package com.app.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.model.OrderMethod;
import com.app.repo.OrderMethodRepository;
import com.app.repo.OrderMethodRepository.ViewC;
import com.app.service.IOrderMethodService;

@Service
public class OrderMethodServiceImpl implements IOrderMethodService{

	@Autowired
	private OrderMethodRepository repo;
	
	@Transactional
	public Integer saveOrderMethod(OrderMethod orderMethod) {
		return repo.save(orderMethod).getOrderId();
	}

	@Transactional
	public void updateOrderMethod(OrderMethod orderMethod) {
		 repo.save(orderMethod).getOrderId();
		
	}

	@Transactional
	public void deleteOrderMethod(Integer id) {
		repo.deleteById(id);
	}

	@Transactional(readOnly=true)
	public OrderMethod getOrderMethodById(Integer id) {
		Optional<OrderMethod> list= repo.findById(id);
		return list.isPresent()?list.get():null;
	}

	@Transactional(readOnly=true)
	public List<OrderMethod> getAllOrderMethods() {
		return repo.findAll();
	}

	
	@Transactional(readOnly=true)
	public List<Object[]> getOrderMethodCountByMode() {
		return repo.findOrderModeCountByMode();
	}

	@Override
	public Map<Integer, String> getAllOrderMethodIdsAndCodes() {
		List<ViewC> list = repo.findByOrderIdAndOrderCode();
		return list.stream()
				.collect(Collectors.toMap(ViewC::getOrderId, ViewC::getOrderCode));
	}
	
	
}
