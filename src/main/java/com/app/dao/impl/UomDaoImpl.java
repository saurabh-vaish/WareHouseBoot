package com.app.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.app.dao.IUomDao;
import com.app.model.Uom;
import com.app.util.AppCollectionUtil;

//@Repository
public class UomDaoImpl implements IUomDao {

	@Autowired
	private HibernateTemplate ht;
	
	@Override
	public Integer saveUom(Uom uom) {
		return (Integer) ht.save(uom);
	}

	@Override
	public void updateUom(Uom uom) {
		ht.update(uom);
	}

	@Override
	public void deleteUom(Integer id) {
		ht.delete(new Uom(id));
	}

	@Override
	public Uom getUomId(Integer id) {
		return ht.get(Uom.class, id);
	}

	@Override
	public List<Uom> getAllUoms() {
		return ht.loadAll(Uom.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getUomCountByUomType() {
		/*
		 * String hql = " select uomType,count(uomType) "+" from "+Uom.class.getName()
		 * +" group by uomType "; List<Object[]> list = (List<Object[]>) ht.find(hql);
		 * return list;
		 */
		
		List<Object[]> list =	(List<Object[]>) ht.findByCriteria(
				DetachedCriteria.forClass(Uom.class)
				.setProjection(
						Projections.projectionList()
						.add(Projections.groupProperty("uomType"))
						.add(Projections.count("uomType"))
						)
				);
				
			return list;	
	}
	
	@SuppressWarnings("unchecked")
	public boolean isUomExist(String uomCode) {

		long count=0;
		//String hql= "select count(uomCode) from "+Uom.class.getName()+" where uomCode=?";
		DetachedCriteria hql=
				DetachedCriteria.forClass(Uom.class)
				.setProjection(Projections.count("uomCode"))
				.add(Restrictions.eq("uomCode", uomCode));

		List<Long> uoms=(List<Long>) ht.findByCriteria(hql);

		if (uoms!=null && !uoms.isEmpty()) {
			count=uoms.get(0);
		} 
		return count>0?true:false;
	}

	@SuppressWarnings("unchecked")
	public Map<Integer, String> getAllUomIdsAndModels() {

		//String hql = "select uomId,uomCode from "+Uom.class.getName();
		DetachedCriteria hql=
				DetachedCriteria.forClass(Uom.class)
				.setProjection(
						Projections.projectionList()
						.add(Projections.property("uomId"))
						.add(Projections.property("uomCode"))
						);

		List<Object[]> list=(List<Object[]>)ht.findByCriteria(hql);
		return AppCollectionUtil.toMap(list);
	}

}
