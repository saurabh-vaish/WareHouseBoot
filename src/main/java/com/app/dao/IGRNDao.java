package com.app.dao;

import java.util.List;

import com.app.model.GoodRecieveNote;


public interface IGRNDao {
	
	public Integer saveGRN(GoodRecieveNote grn);
	public void updateGRN(GoodRecieveNote grn);
	public void deleteGRN(Integer grnId);
	public GoodRecieveNote getGRNById(Integer grnId);
	public List<GoodRecieveNote> getAllGRNs();
	public boolean isOrderCodeExist(String grnCode);

}
