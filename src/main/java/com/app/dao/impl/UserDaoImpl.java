package com.app.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.app.dao.IUserDao;
import com.app.model.User;

//@Repository
public class UserDaoImpl implements IUserDao {

	
	@Autowired
	private HibernateTemplate ht;
	
	
	@Override
	public Integer saveUser(User user) {
		return (Integer) ht.save(user);
	}

	@Override
	public void updateUser(User user) {
		ht.update(user);
	}

	@Override
	public void deleteUser(Integer id) {
		ht.delete(new User(id));
	}

	@Override
	public User getUserById(Integer id) {
		return ht.get(User.class, id);
	}

	@Override
	public List<User> getAllUsers() {
		return ht.loadAll(User.class);
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Object[]> getUsersCount() {

		String hql =   "select gender,count(gender) from "
				+User.class.getCanonicalName()
				+" group by gender";

		List<Object[]> users = (List<Object[]>) ht.find(hql);

		return users;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public boolean isEmailOrMobileExist(String type, String userEmailOrmobile) {

		long count=0;

		String hql="select count("+type+") from "+User.class.getName()+" where "+type+"=?";

		List<Long> whUserType=(List<Long>) ht.find(hql, userEmailOrmobile);

		if (whUserType!=null && !whUserType.isEmpty()) {
			count=whUserType.get(0);
		}
		return count>0?true:false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public User findByUserEmail(String username) {
		User user=null;
		List<User> users=(List<User>) ht.findByCriteria(
				DetachedCriteria.forClass(User.class)
				.add(Restrictions.eq("userEmail", username))
				);
		if(users!=null && !users.isEmpty()) {
			user=users.get(0);
		}
		return user;
	}

	
}
