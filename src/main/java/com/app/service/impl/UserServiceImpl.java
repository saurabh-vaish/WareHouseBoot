package com.app.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.model.User;
import com.app.repo.UserRepository;
import com.app.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	//@Autowired
	//private BCryptPasswordEncoder ncoder;
	
	@Autowired
	private UserRepository repo;

	@Transactional
	public Integer saveUser(User user) {
		//user.setUserPwd(ncoder.encode(user.getUserPwd()));
		return repo.save(user).getUserId();
	}

	@Transactional
	public void updateUser(User user) {
		repo.save(user).getUserId();
	}

	@Transactional
	public void deleteUser(Integer id) {
		repo.deleteById(id);
	}

	@Transactional(readOnly=true)
	public User getUserById(Integer id) 
	{
		Optional<User> list = repo.findById(id);
		return list.isPresent()?list.get():null;
	}

	@Transactional(readOnly=true)
	public List<User> getAllUsers() {
		return repo.findAll();
	}
	
	@Transactional(readOnly=true)
	public List<Object[]> getUsersCount() {
		return repo.findUserCount();
	}
	
	@Transactional(readOnly=true)
	public boolean isEmailOrMobileExist(String type, String userEmailOrmobile) {
	//	return repo.isEmailOrMobileExist(type, userEmailOrmobile);
	return false;
	}
	
	
	@Transactional(readOnly=true)
	public User findByUserEmail(String username) {
		return repo.findByUserEmail(username);
	}

	@Transactional
	public void updatePass(User user) {
		//user.setUserPwd(ncoder.encode(user.getUserPwd()));
		repo.save(user).getUserId();
		
	}
	
	
}
