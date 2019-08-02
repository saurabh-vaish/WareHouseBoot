package com.app.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.app.dao.IShippingDao;
import com.app.model.Shipping;

//@Repository
public class ShippingDaoImpl implements IShippingDao{
	
	@Autowired
	private HibernateTemplate ht;

	public Integer saveShipping(Shipping shipping) {
		return (Integer) ht.save(shipping);
	}

	public void updateShipping(Shipping shipping) {
		ht.update(shipping);
	}

	public void deleteShipping(Integer shipId) {
		ht.delete(new Shipping(shipId));
	}

	public Shipping getShippingById(Integer shipId) {
		return ht.get(Shipping.class, shipId);
	}

	public List<Shipping> getAllShippings() {
		return ht.loadAll(Shipping.class);
	}

	@SuppressWarnings("unchecked")
	public boolean isShippingCodeExist(String shipCode) {
		
		long count=0;
		
		DetachedCriteria hql =
				DetachedCriteria.forClass(Shipping.class)
				.setProjection(Projections.projectionList()
						.add(Projections.count("shipCode"))
						)
				.add(Restrictions.eq("shipCode", shipCode));
		List<Long> shippings = (List<Long>) ht.findByCriteria(hql);
		
		if (shippings!=null && !shippings.isEmpty()) {
			count = shippings.get(0);
		}
		
		return count>0?true:false;
	}

}
