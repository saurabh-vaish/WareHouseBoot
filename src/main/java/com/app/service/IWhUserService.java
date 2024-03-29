package com.app.service;

import java.util.List;
import java.util.Map;

import com.app.model.WhUser;

public interface IWhUserService {

	public Integer saveWhUser(WhUser user);
	public void updateWhUser(WhUser user);
	public void deleteWhUser(Integer id);
	public WhUser getWhUserById(Integer id);
	public List<WhUser> getAllWhUsers();
	
	public List<Object[]> getWhUserTypeCount();
	
	public Map<Integer, String> getAllWhUserByType(String userType);

}
