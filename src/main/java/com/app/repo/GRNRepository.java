package com.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.model.GoodRecieveNote;

public interface GRNRepository extends JpaRepository<GoodRecieveNote, Integer>{

	
	public List<GoodRecieveNote> findByGrnCode(String grnCode);
}
