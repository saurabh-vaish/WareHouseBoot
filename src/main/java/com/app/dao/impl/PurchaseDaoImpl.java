package com.app.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.app.dao.IPurchaseDao;
import com.app.model.Purchase;
import com.app.model.PurchaseDtl;
import com.app.util.AppCollectionUtil;

//@Repository
public class PurchaseDaoImpl implements IPurchaseDao {

	@Autowired
	private HibernateTemplate ht;
	
	@Override
	public Integer savePurchase(Purchase purchase) {
		return (Integer) ht.save(purchase);
	}

	@Override
	public void updatePurchase(Purchase purchase) {
		ht.update(purchase);
	}

	@Override
	public void deletePurchase(Integer id) {
		ht.delete(new Purchase(id));
	}

	@Override
	public Purchase getPurchaseById(Integer id) {
		return ht.get(Purchase.class, id);
	}

	@Override
	public List<Purchase> getAllPurchases() {
		return ht.loadAll(Purchase.class);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isOrderCodeExits(String ocode) {

		// hql = select count(orderCode) from com.app.model.Purchase
		
		List<Long> list = (List<Long>) ht.findByCriteria(			// return Primitive if one column selected
					DetachedCriteria.forClass(Purchase.class)
					.setProjection(
							Projections.projectionList()
							.add(Projections.count("orderCode"))
							)
					.add(Restrictions.eq("orderCode", ocode))
				);
		System.out.println(list.get(0));
		return (list.get(0)==0?false:true);  // if duplicate there then other than 0 else 0
	}
	
	
	public void deletePurchaseDtlById(Integer orderDtlId) {
		ht.delete(new PurchaseDtl(orderDtlId));
	}

	@SuppressWarnings("unchecked")
	public Map<Integer, String> getInvoicedPurchaseOrders(String status) {

		DetachedCriteria hql = 
				DetachedCriteria.forClass(Purchase.class)
				.setProjection(Projections.projectionList()
						.add(Projections.property("purId"))
						.add(Projections.property("orderCode"))
						)
				.add(Restrictions.eq("status", status));
		
		return AppCollectionUtil.toMap((List<Object[]>) ht.findByCriteria(hql));
	}

	public PurchaseDtl getPurchaseDtlsById(Integer orderDtlId) {
		return ht.get(PurchaseDtl.class, orderDtlId);
	}

	public void updatePurchaseDtls(PurchaseDtl purchaseDtl) {
		ht.update(purchaseDtl);
	}

	@SuppressWarnings("deprecation")
	public int updateAllPurchaseDtlsStatus(String grnStatus,Integer poHdrId) {
		
		String hql = "update "+PurchaseDtl.class.getName()
				+ " set grnStatus=? "
				+ " where grnStatus is null and poHdrId=?";
		int rowCount=ht.bulkUpdate(hql, grnStatus,poHdrId);
		return rowCount;
	}
	
	@SuppressWarnings("unchecked")
	public long getNullGrnStatusCount(Integer orderId) {
		
		long count=0;
		
		DetachedCriteria hql=
				DetachedCriteria.forClass(PurchaseDtl.class)
				.setProjection(Projections.projectionList()
						.add(Projections.count("orderDtlId"))
						)
				.add(Restrictions.eq("poHdrId",orderId))
				.add(Restrictions.isNull("grnStatus"));
		List<Long> list=(List<Long>) ht.findByCriteria(hql);
		if (list != null && !list.isEmpty()) {
			count=list.get(0);
		}
		
		return count;
	}

}

