package com.app.service;

import java.util.List;
import java.util.Map;

import com.app.model.Purchase;
import com.app.model.PurchaseDtl;

public interface IPurchaseService {

	public Integer savePurchase(Purchase purchase);
	public void updatePurchase(Purchase purchase);
	public void deletePurchase(Integer id);
	public Purchase getPurchaseById(Integer id);
	public List<Purchase> getAllPurchases();
	
	public boolean isOrderCodeExits(String ocode);
	
	public void deletePurchaseDtlById(Integer orderDtlId);
	public Map<Integer, String> getInvoicedPurchaseOrders(String status);
	
	//child class operartions
	public PurchaseDtl getPurchaseDtlsById(Integer orderDtlId);
	public void updatePurchaseDtls(PurchaseDtl purchaseDtl);
	public int updateAllPurchaseDtlsStatus(String grnStatus,Integer poHdrId);
	public long getNullGrnStatusCount(Integer orderId);

}
