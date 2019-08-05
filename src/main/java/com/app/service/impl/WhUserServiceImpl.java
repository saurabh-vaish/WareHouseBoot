package com.app.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.model.WhUser;
import com.app.repo.WhUserRepository;
import com.app.service.IWhUserService;

@Service
public class WhUserServiceImpl implements IWhUserService {

	
	@Autowired
	private WhUserRepository repo;

	@Transactional
	public Integer saveWhUser(WhUser user) {
		return repo.save(user).getWhId();
	}

	@Transactional
	public void updateWhUser(WhUser user) {
		repo.save(user).getWhId();
	}

	@Transactional
	public void deleteWhUser(Integer id) {
		repo.deleteById(id);
	}

	@Transactional(readOnly=true)
	public WhUser getWhUserById(Integer id) 
	{
		Optional<WhUser> list =repo.findById(id);
		return list.isPresent()?list.get():null;
	}

	@Transactional(readOnly=true)
	public List<WhUser> getAllWhUsers() {
		return repo.findAll();
	}
	
	@Transactional(readOnly=true)
	public List<Object[]> getWhUserTypeCount() {
		
		return repo.findWhUserCountByType();
	}

	

	@Transactional(readOnly=true)
	public Map<Integer, String> getAllWhUserByType(String userType)
	{
		List<Object[]> list = repo.findAllWhUserByType(userType);
		
		return list.stream().collect(Collectors.toMap(
							ob->(Integer)ob[0],				// key	-- id	
							ob->(String)ob[1])				// value --  Code
				);
	}

	
}