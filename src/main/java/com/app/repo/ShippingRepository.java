package com.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.model.Shipping;

public interface ShippingRepository extends JpaRepository<Shipping, Integer>{

	
	@Query("select count(s.shipCode) from com.app.model.Shipping as s where s.shipCode = :shipCode")
	public List<Shipping> findShipCodeCountByShipCode(String shipCode);
}
