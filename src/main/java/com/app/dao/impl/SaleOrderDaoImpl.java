package com.app.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.app.dao.ISaleOrderDao;
import com.app.model.SaleOrder;
import com.app.model.SalesDetails;
import com.app.util.AppCollectionUtil;

//@Repository
public class SaleOrderDaoImpl implements ISaleOrderDao {

	@Autowired
	private HibernateTemplate ht;
	
	@Override
	public Integer saveSaleOrder(SaleOrder saleOrder) {
		return (Integer) ht.save(saleOrder);
	}

	@Override
	public void updateSaleOrder(SaleOrder saleOrder) {
		ht.update(saleOrder);
	}

	@Override
	public void deleteSaleOrder(Integer id) {
		ht.delete(new SaleOrder(id));
	}

	@Override
	public SaleOrder getSaleOrderById(Integer id) {
		return ht.get(SaleOrder.class, id);
	}

	@Override
	public List<SaleOrder> getAllSaleOrders() {
		return ht.loadAll(SaleOrder.class);
	}
	
		
	@SuppressWarnings("unchecked")
	@Override
	public boolean isOrderCodeExits(String ocode) {

		// hql = select count(orderCode) from com.app.model.SaleOrder
		
		List<Long> list = (List<Long>) ht.findByCriteria(			// return Primitive if one column selected
					DetachedCriteria.forClass(SaleOrder.class)
					.setProjection(
							Projections.projectionList()
							.add(Projections.count("orderCode"))
							)
					.add(Restrictions.eq("orderCode", ocode))
				);
		System.out.println(list.get(0));
		return (list.get(0)==0?false:true);  // if duplicate there then other than 0 else 0
	}
	
	public void deleteSalesDetailsById(Integer salesDtlsId) {
		ht.delete(new SalesDetails(salesDtlsId));
	}

	@SuppressWarnings("unchecked")
	public Map<Integer, String> getInvoicedSaleOrders(String status) {

		DetachedCriteria hql = 
				DetachedCriteria.forClass(SaleOrder.class)
				.setProjection(Projections.projectionList()
						.add(Projections.property("saleId"))
						.add(Projections.property("orderCode"))
						)
				.add(Restrictions.eq("status", status));

		return AppCollectionUtil.toMap((List<Object[]>) ht.findByCriteria(hql));
	}

	public SalesDetails getSalesDetailsById(Integer salesDtlsId) {
		return ht.get(SalesDetails.class, salesDtlsId);
	}

	public void updateSalesDetails(SalesDetails salesDetails) {
		ht.update(salesDetails);
	}

	@SuppressWarnings("deprecation")
	public int updateAllSalesDetailsStatus(String shipSatus, Integer soHoId) {
		String hql = "update "+SalesDetails.class.getName()
				+ " set shipSatus=? "
				+ " where shipSatus is null and soHoId=?";
		int rowCount=ht.bulkUpdate(hql, shipSatus,soHoId);
		return rowCount;
	}

	@SuppressWarnings("unchecked")
	public long getNullShippingStatusCount(Integer saleOrderId) {
		long count=0;

		DetachedCriteria hql=
				DetachedCriteria.forClass(SalesDetails.class)
				.setProjection(Projections.projectionList()
						.add(Projections.count("salesDtlsId"))
						)
				
				.add(Restrictions.eq("soHoId",saleOrderId))
				.add(Restrictions.isNull("shipSatus"));
		List<Long> list=(List<Long>) ht.findByCriteria(hql);
		
		if (list != null && !list.isEmpty()) {
			count=list.get(0);
		}

		return count;
	}

}
