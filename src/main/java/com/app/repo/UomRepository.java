package com.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.model.Uom;

public interface UomRepository extends JpaRepository<Uom, Integer>{
	
	@Query("select count(u.uomCode) from com.app.model.Uom as u where u.uomCode = :uomCode")
	public Integer findUomCountByUomCode(String uomCode);

	@Query("select u.uomType,count(u.uomType) from com.app.model.Uom as u group by u.uomType ")
	public List<Object[]> findUomCountByUomType();

	
	@Query("select u.id,u.uomCode from com.app.model.Uom as u")
	public List<ViewF> findByUomIdAndUomCode();
	
	interface ViewF
	{
		Integer getId();
		String getUomCode();
	}
}
