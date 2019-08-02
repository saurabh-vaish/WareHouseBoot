package com.app.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.app.dao.IWhUserDao;
import com.app.model.WhUser;
import com.app.util.AppCollectionUtil;

//@Repository
public class WhUserDaoImpl implements IWhUserDao {

	
	@Autowired
	private HibernateTemplate ht;
	
	
	@Override
	public Integer saveWhUser(WhUser user) {
		return (Integer) ht.save(user);
	}

	@Override
	public void updateWhUser(WhUser user) {
		ht.update(user);
	}

	@Override
	public void deleteWhUser(Integer id) {
		ht.delete(new WhUser(id));
	}

	@Override
	public WhUser getWhUserById(Integer id) {
		return ht.get(WhUser.class, id);
	}

	@Override
	public List<WhUser> getAllWhUsers() {
		return ht.loadAll(WhUser.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getWhUserTypeCount() {

		/*String hql=  " select userFor,count(userFor) from "
				+ WhUserType.class.getName()
				+ " group by userFor";*/
		DetachedCriteria hql=
				DetachedCriteria.forClass(WhUser.class)
				.setProjection(
						Projections.projectionList()
						.add(Projections.groupProperty("whFor"))
						.add(Projections.count("whFor"))
						);
		List<Object[]> whUserTypes = (List<Object[]>) ht.findByCriteria(hql);

		return whUserTypes;
	}
	
	
	/*
	 * @Override public List<WhUser> getWhUserByWhType(String type) { // sql :
	 * select * from whUser where whType=? // hql: from com.app.model.WhUserType
	 * where whType=?
	 * 
	 * // since find() is deprecated to by using DetachedCriteria List<WhUser> list
	 * =(List<WhUser>) ht.findByCriteria( DetachedCriteria.forClass(WhUser.class) //
	 * specify model class .add(Restrictions.eq("whType", type)) // condtion );
	 * 
	 * return list; }
	 */
	
	@SuppressWarnings("unchecked")
	public Map<Integer, String> getAllWhUserByType(String userType) {
		//String hql = "select userId,userCode from "+WhUserType.class.getName()+" where userType=?";
		DetachedCriteria hql=
				DetachedCriteria.forClass(WhUser.class)
				.setProjection(
						Projections.projectionList()
						.add(Projections.property("whId"))
						.add(Projections.property("whCode"))

						)
				.add(Restrictions.eq("whType", userType))
				;
		List<Object[]> list=(List<Object[]>) ht.findByCriteria(hql);
		return AppCollectionUtil.toMap(list);
	}

}
