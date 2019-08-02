package com.app.service;

import java.util.List;
import java.util.Map;

import com.app.model.Uom;

public interface IUomService {

	public Integer saveUom(Uom uom);
	public void updateUom(Uom uom);
	public void deleteUom(Integer id);
	public Uom getUomId(Integer id);
	public List<Uom> getAllUoms();
	
	public List<Object[]> getUomCountByUomType();
	public boolean isUomExist(String uomModel);
	public Map<Integer, String> getAllUomIdsAndModels();
}