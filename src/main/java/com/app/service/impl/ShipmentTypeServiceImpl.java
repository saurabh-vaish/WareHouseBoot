package com.app.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.excetion.ShipmentTypeException;
import com.app.model.ShipmentType;
import com.app.repo.ShipmentTypeRepository;
import com.app.repo.ShipmentTypeRepository.ViewA;
import com.app.service.IShipmentTypeService;


@Service
public class ShipmentTypeServiceImpl implements IShipmentTypeService {

	@Autowired
	private ShipmentTypeRepository repo;
	
	@Transactional
	public Integer saveShipmentType(ShipmentType shipmentType) {
		return repo.save(shipmentType).getShipmentId();
	}

	@Transactional
	@CachePut(value="warehouse-cache",key = "#shipmentId")
	public void updateShipmentType(Integer shipmentId,ShipmentType shipmentType) {
		
		repo.save(shipmentType).getShipmentId();

	}

	@Transactional
	@CacheEvict(value="warehouse-cache",key = "#shipmentId")
	public void deleteShipmentType(Integer shipmentId) {
		repo.deleteById(shipmentId);

	}

	
	@Transactional(readOnly=true)
	@Cacheable(value="warehouse-cache",key = "#shipmentId")
	public ShipmentType getShipmentTypeById(Integer shipmentId) {
	
		Optional<ShipmentType> ship=repo.findById(shipmentId);
		
		if(ship.isPresent())
		{
			return ship.get();
		}
		else throw new ShipmentTypeException("Shipment Not Found");
	}

	
	@Transactional(readOnly=true)
	public List<ShipmentType> getAllShipmentTypes() {
		return repo.findAll();
	}

	@Transactional(readOnly=true)
	@Cacheable(value="warehouse-cache",key = "#shipmentId")
	public List<Object[]> getShipmentCountByMode() {
		List<Object[]> list= repo.findShipmentTypeCountByMode();
	
		list.stream().map((p)->p[0]+","+p[1]).forEach(System.out::println);
	
		return list;
	}

	
	@Transactional(readOnly=true)
	public Map<Integer,String> getEnableShipmentIdsAndCodes() {
		
		// converting List<ViewA> to map<Integer,String>
		
		return repo.findByEnableShipment("YES")   
				.parallelStream()
				.collect(
						Collectors.toMap(
							ViewA::getShipmentId,
							ViewA::getShipmentCode
						));
		
	}

	
}
