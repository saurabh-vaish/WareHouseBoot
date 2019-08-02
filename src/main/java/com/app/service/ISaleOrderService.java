package com.app.service;

import java.util.List;
import java.util.Map;

import com.app.model.SaleOrder;
import com.app.model.SalesDetails;

public interface ISaleOrderService {

	public Integer saveSaleOrder(SaleOrder saleOrder);
	public void updateSaleOrder(SaleOrder saleOrder);
	public void deleteSaleOrder(Integer id);
	public SaleOrder getSaleOrderById(Integer id);
	public List<SaleOrder> getAllSaleOrders();
	
	
	public boolean isOrderCodeExits(String ocode);
	
	public void deleteSalesDetailsById(Integer salesDtlsId);
	public Map<Integer, String> getInvoicedSaleOrders(String status);
	
	//child class operationd
	public SalesDetails getSalesDetailsById(Integer salesDtlsId);
	public void updateSalesDetails(SalesDetails salesDetails);
	public Integer updateAllSalesDetailsStatus(String shipSatus,Integer soHoId);
	public long getNullShippingStatusCount(Integer saleOrderId);

}
