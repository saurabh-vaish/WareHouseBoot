package com.app.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.model.Purchase;
import com.app.model.PurchaseDtl;
import com.app.repo.PurchaseDtlRepository;
import com.app.repo.PurchaseRepository;
import com.app.repo.PurchaseRepository.ViewD;
import com.app.service.IPurchaseService;

@Service
public class PurchaseServiceImpl implements IPurchaseService {

	@Autowired
	private PurchaseRepository repo;
	
	@Autowired
	private PurchaseDtlRepository prepo;
	
	@Transactional
	public Integer savePurchase(Purchase purchase) {
		return repo.save(purchase).getPurId();
	}

	@Transactional
	public void updatePurchase(Purchase purchase) {
		repo.save(purchase).getPurId();

	}

	@Transactional
	public void deletePurchase(Integer id) {
		repo.deleteById(id);

	}

	@Transactional(readOnly=true)
	public Purchase getPurchaseById(Integer id) {
		Optional<Purchase> list = repo.findById(id);
		return list.isPresent()?list.get():null;
	}

	@Transactional(readOnly=true)
	public List<Purchase> getAllPurchases() {
		return repo.findAll();
	}
	
	@Transactional(readOnly=true)
	public boolean isOrderCodeExits(String ocode) {
		List<Purchase> list = repo.findByOrderCode(ocode);
		return (list.isEmpty() || list==null)?false:true;
	}

	@Transactional
	public void deletePurchaseDtlById(Integer orderDtlId) {
		prepo.deleteById(orderDtlId);						// child table row of Purchase delete
	}

	
	@Transactional(readOnly=true)
	public Map<Integer, String> getInvoicedPurchaseOrders(String status) {
	
		List<ViewD> list = repo.findInvoicePurchseOrderByStatus(status);
		
		return list.stream().collect(Collectors.toMap(ViewD::getPurId, ViewD::getOrderCode));
	}
	

	@Transactional(readOnly=true)
	public PurchaseDtl getPurchaseDtlsById(Integer orderDtlId) {
		
		Optional<PurchaseDtl> list= prepo.findById(orderDtlId);
		
		return list.isPresent()?list.get():null;
	}

	
	@Transactional
	public void updatePurchaseDtls(PurchaseDtl purchaseDtl) {
		prepo.save(purchaseDtl).getOrderDtlId();
	}

	@Transactional
	public int updateAllPurchaseDtlsStatus(String grnStatus,Integer poHdrId) {
		return prepo.updatePurchaseDtlBasedOnStatus(grnStatus, poHdrId);
	}

	@Transactional
	public long getNullGrnStatusCount(Integer orderId) {
		return prepo.findNullGrnStatusCount(orderId);
	}

}
