package com.app.dao;

import java.util.List;

import com.app.model.Shipping;

public interface IShippingDao {
	
	public Integer saveShipping(Shipping shipping);
	public void updateShipping(Shipping shipping);
	public void deleteShipping(Integer shipId);
	public Shipping getShippingById(Integer shipId);
	public List<Shipping> getAllShippings();
	public boolean isShippingCodeExist(String shipCode);

}
