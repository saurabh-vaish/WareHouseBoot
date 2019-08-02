package com.app.service;

import java.util.List;
import java.util.Map;

import com.app.model.ShipmentType;

public interface IShipmentTypeService {
	
	public Integer saveShipmentType(ShipmentType shipmentType);
	public void updateShipmentType(Integer shipmentId,ShipmentType shipmentType);
	public void deleteShipmentType(Integer id);
	public ShipmentType getShipmentTypeById(Integer id);
	public List<ShipmentType> getAllShipmentTypes();
	
	public List<Object[]> getShipmentCountByMode();
	
	public Map<Integer, String> getEnableShipmentIdsAndCodes();
	

}
