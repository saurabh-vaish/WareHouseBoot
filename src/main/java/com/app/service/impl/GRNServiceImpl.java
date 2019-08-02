package com.app.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.model.GoodRecieveNote;
import com.app.repo.GRNRepository;
import com.app.service.IGRNService;

@Service
public class GRNServiceImpl implements IGRNService {
	
	@Autowired
	private GRNRepository repo;

	@Transactional
	public Integer saveGRN(GoodRecieveNote grn) {
		return repo.save(grn).getGrnId();
	}

	@Transactional
	public void updateGRN(GoodRecieveNote grn) {
		repo.save(grn).getGrnId();
	}

	@Transactional
	public void deleteGRN(Integer grnId) {
		repo.deleteById(grnId);
	}

	
	@Transactional(readOnly=true)
	public GoodRecieveNote getGRNById(Integer grnId) {
		Optional<GoodRecieveNote> grn = repo.findById(grnId);
		if(grn.isPresent())
		{
			return grn.get();
		}
		else return null;
	}

	@Transactional(readOnly=true)
	public List<GoodRecieveNote> getAllGRNs() {
		return repo.findAll();
	}

	@Transactional(readOnly=true)
	public boolean isOrderCodeExist(String grnCode) {
		
		List<GoodRecieveNote> list= repo.findByGrnCode(grnCode);
		return (list.isEmpty() || list==null) ?false:true; 
	}

}
