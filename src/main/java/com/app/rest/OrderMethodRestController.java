package com.app.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.OrderMethod;
import com.app.model.OrderMethod;
import com.app.service.IOrderMethodService;

@RestController
@RequestMapping("/rest/order/")
public class OrderMethodRestController {


	@Autowired
	private IOrderMethodService service;

	// get data
	@GetMapping("/all")
	public ResponseEntity<?> getAll()
	{
		ResponseEntity<?> resp=null;

		List<OrderMethod> list=service.getAllOrderMethods();

		if(list!=null && !list.isEmpty())
		{
			resp = new ResponseEntity<List<OrderMethod>>(list,HttpStatus.OK);
		}
		else
		{
			resp = new ResponseEntity<String>("No data found",HttpStatus.OK);
		}

		return resp;
	}



	/// save data 

	@PostMapping("/save")
	public ResponseEntity<String> saveData(@RequestBody OrderMethod orderMethod)
	{
		ResponseEntity<String> resp = null;

		try
		{
			Integer sid = service.saveOrderMethod(orderMethod);
			resp = new ResponseEntity<String>("saved with id = "+sid,HttpStatus.OK);
		}
		catch (Exception e) {

			resp = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}


		return resp;
	}

	// get one record
	@GetMapping("/getOne/{oid}")  // here we are using @PathVaraible to getting dynamic primitive input
	public ResponseEntity<?> getOneOrderMethod(@PathVariable Integer oid)
	{
		ResponseEntity<?> resp = null;
		try {
			OrderMethod od =service.getOrderMethodById(oid);

			if(od!=null)
			{
				resp = new ResponseEntity<OrderMethod>(od,HttpStatus.OK);				
			}
			else
			{
				resp = new ResponseEntity<String>(oid+" not found",HttpStatus.BAD_REQUEST);
			}

		}
		catch (Exception e) {
			resp = new ResponseEntity<String>("Unable to Process ",HttpStatus.INTERNAL_SERVER_ERROR);				

		}
		return resp;
	}



	// delete order by id
	@DeleteMapping("/delete/{oid}")  // here we are using @PathVaraible to getting dynamic primitive input
	public ResponseEntity<?> deleteOrderMethod(@PathVariable Integer oid)
	{
		ResponseEntity<?> resp = null;
		try {
			service.deleteOrderMethod(oid);

			resp = new ResponseEntity<String>(oid+" deleted succesfully",HttpStatus.OK);
		}
		catch (Exception e) {
			resp = new ResponseEntity<String>("Unable to Process "+oid+" not found",HttpStatus.BAD_REQUEST);

		}
		return resp;
	}


	// update order
	@PutMapping("/update")
	public ResponseEntity<?> updateOrderMethod(@RequestBody OrderMethod order)
	{
		ResponseEntity<?> resp = null;
		try {
			service.updateOrderMethod(order);
			resp = new ResponseEntity<String>(order.getOrderId() +" order updated successfully ",HttpStatus.OK);
		}
		catch (Exception e) {
			resp = new ResponseEntity<String>(order.getOrderId() +" Not found ",HttpStatus.BAD_REQUEST);
		}
		return resp;
	}



}
