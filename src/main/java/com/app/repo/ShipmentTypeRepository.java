package com.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.model.ShipmentType;

public interface ShipmentTypeRepository extends JpaRepository<ShipmentType, Integer>{

	@Query("select s.shipmentMode,count(s.shipmentMode) from com.app.model.ShipmentType as s group by s.shipmentMode")
	public List<Object[]> findShipmentTypeCountByMode();
	
	// select shipmentId,shipmentMode from shipmentType where enableShipment = "YES"
	public List<ViewA> findByEnableShipment(String enableShipment);
	
	interface ViewA
	{
		Integer getShipmentId();
		String getShipmentCode();
	}
}
