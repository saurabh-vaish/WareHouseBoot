package com.app.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.model.Shipping;
import com.app.repo.ShippingRepository;
import com.app.service.IShippingService;

@Service
public class ShippingServiceImpl implements IShippingService {

	@Autowired
	private ShippingRepository repo;

	@Transactional
	public Integer saveShipping(Shipping shipping) {
		return repo.save(shipping).getShipId();
	}

	@Transactional
	public void updateShipping(Shipping shipping) {
		repo.save(shipping).getShipId();
	}

	@Transactional
	public void deleteShipping(Integer shipId) {
		repo.deleteById(shipId);
	}

	@Transactional(readOnly=true)
	public Shipping getShippingById(Integer shipId) 
	{
		Optional<Shipping> list = repo.findById(shipId);
		return list.isPresent()?list.get():null;
	}

	@Transactional(readOnly=true)
	public List<Shipping> getAllShippings() 
	{
		return repo.findAll();
	}

	@Transactional(readOnly=true)
	public boolean isShippingCodeExist(String shipCode)
	{
		List<Shipping> list= repo.findShipCodeCountByShipCode(shipCode);
		
		return (list.isEmpty() || list==null)?false:true;
	}

}
