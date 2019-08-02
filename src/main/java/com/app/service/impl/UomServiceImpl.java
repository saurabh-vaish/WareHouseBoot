package com.app.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.model.Uom;
import com.app.repo.UomRepository;
import com.app.repo.UomRepository.ViewF;
import com.app.service.IUomService;

@Service
public class UomServiceImpl implements IUomService {

	@Autowired
	private UomRepository repo;
	
	@Override
	@Transactional
	public Integer saveUom(Uom uom) {
		return repo.save(uom).getId();
	}

	@Override
	@Transactional
	public void updateUom(Uom uom) {
		repo.save(uom).getId();

	}

	@Override
	@Transactional
	public void deleteUom(Integer id) {
		repo.deleteById(id);

	}

	@Transactional(readOnly=true)
	public Uom getUomId(Integer id) 
	{
		Optional<Uom> list = repo.findById(id);
		return (list.isPresent())?list.get():null;
	}

	@Transactional(readOnly=true)
	public List<Uom> getAllUoms() {
		return repo.findAll();
	}

	
	@Transactional(readOnly=true)
	public List<Object[]> getUomCountByUomType() {
		return repo.findUomCountByUomType();
	}
	
	
	@Transactional(readOnly=true)
	public boolean isUomExist(String uomCode) {
		return repo.findUomCountByUomCode(uomCode)>0?true:null;
	}
	
	@Transactional(readOnly=true)
	public Map<Integer, String> getAllUomIdsAndModels() 
	{
		List<ViewF> list = repo.findByUomIdAndUomCode();
		return list.stream().collect(Collectors.toMap(ViewF::getId, ViewF::getUomCode));
	}

}

