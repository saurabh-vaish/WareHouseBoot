package com.app.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.model.SaleOrder;
import com.app.model.SalesDetails;
import com.app.repo.SaleOrderRepository;
import com.app.repo.SaleOrderRepository.ViewE;
import com.app.repo.SalesDetailsRepository;
import com.app.service.ISaleOrderService;

@Service
public class SaleOrderServiceImpl implements ISaleOrderService {

	@Autowired
	private SaleOrderRepository repo;

	@Autowired
	private SalesDetailsRepository srepo;
	
	@Transactional
	public Integer saveSaleOrder(SaleOrder saleOrder) {
		return repo.save(saleOrder).getSaleId();
	}

	@Transactional
	public void updateSaleOrder(SaleOrder saleOrder) {
		repo.save(saleOrder).getSaleId();

	}

	@Transactional
	public void deleteSaleOrder(Integer id) {
		repo.deleteById(id);

	}

	@Transactional(readOnly=true)
	public SaleOrder getSaleOrderById(Integer id) {
		Optional<SaleOrder> list = repo.findById(id);
		return list.isPresent()?list.get():null;
	}

	
	@Transactional(readOnly=true)
	public List<SaleOrder> getAllSaleOrders() {
		return repo.findAll();
	}

	
	
	@Transactional(readOnly=true)
	public boolean isOrderCodeExits(String ocode) {
	
		List<SaleOrder> list= repo.findByOrderCode(ocode);
		
		return (list.isEmpty() || list==null)?false:true;
	}
	
	
	@Transactional
	public void deleteSalesDetailsById(Integer salesDtlsId) {
		srepo.deleteById(salesDtlsId); 					// child table row by child table repository
	}
	
	@Transactional(readOnly=true)
	public Map<Integer, String> getInvoicedSaleOrders(String status) {
		
		List<ViewE> list = repo.findInvoiceSaleOrderByStatus(status);
		
		return list.stream().collect(Collectors.toMap(ViewE::getSaleId, ViewE::getOrderCode));
	}

	@Transactional(readOnly=true)
	public SalesDetails getSalesDetailsById(Integer salesDtlsId) {
		
		Optional<SalesDetails> list = srepo.findById(salesDtlsId);
		return list.isPresent()?list.get():null;
	}

	@Transactional
	public void updateSalesDetails(SalesDetails salesDetails) {
		srepo.save(salesDetails).getSalesDtlsId();
	}

	@Transactional
	public Integer updateAllSalesDetailsStatus(String shipSatus, Integer soHoId) {
		return srepo.updateSalesDetailsBasedOnStatus(shipSatus, soHoId);
	}

	
	@Transactional(readOnly=true)
	public long getNullShippingStatusCount(Integer saleOrderId) {
	
		return srepo.findNullShippingStatusCount(saleOrderId);
	}

	
}

