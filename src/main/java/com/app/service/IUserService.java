package com.app.service;

import java.util.List;

import com.app.model.User;

public interface IUserService {

	public Integer saveUser(User user);
	public void updateUser(User user);
	public void deleteUser(Integer id);
	public User getUserById(Integer id);
	public List<User> getAllUsers();
	
	public List<Object[]> getUsersCount();
	public boolean isEmailOrMobileExist(String type,String userEmailOrmobile);
	public User findByUserEmail(String username);
	
	public void updatePass(User user);
	
}
