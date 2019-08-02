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

import com.app.model.ShipmentType;
import com.app.service.IShipmentTypeService;


@RestController   // for rest services . It has @ResponseBody implemented inside
// @ResponseBody      // no need to write
@RequestMapping("/rest/shipment/")
public class ShipmentTypeRestController {


	@Autowired
	private IShipmentTypeService service;


	// getting data 
	@GetMapping("/all")
	public ResponseEntity<?> getAll()    // ? bcs data type decided at runtime
	{
		ResponseEntity<?> resp=null;

		// fetching data 
		List<ShipmentType> list = service.getAllShipmentTypes();

		if(list!=null && !list.isEmpty())
		{
			resp = new ResponseEntity<List<ShipmentType>>(list,HttpStatus.OK);   // ganerating httpResponse
		}
		else
		{
			resp = new ResponseEntity<String>("No data found",HttpStatus.OK);
		}
		return resp;

	}


	// save data to db
	@PostMapping("/save")
	public ResponseEntity<String> saveData(@RequestBody  ShipmentType shipmentType)
	{
		ResponseEntity<String> resp=null;


		try {
			Integer sid = service.saveShipmentType(shipmentType);
			resp = new ResponseEntity<String>("saved data with id "+sid,HttpStatus.OK);
		}
		catch (Exception e) {

			resp = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}

		return resp;
	}



	// get one record
	@GetMapping("/getOne/{sid}")  // here we are using @PathVaraible to getting dynamic primitive input
	public ResponseEntity<?> getOneShipment(@PathVariable Integer sid)
	{
		ResponseEntity<?> resp = null;
		try {
			ShipmentType st =service.getShipmentTypeById(sid);

			if(st!=null)
			{
				resp = new ResponseEntity<ShipmentType>(st,HttpStatus.OK);				
			}
			else
			{
				resp = new ResponseEntity<String>(sid+" not found",HttpStatus.BAD_REQUEST);
			}

		}
		catch (Exception e) {
			resp = new ResponseEntity<String>("Unable to Process ",HttpStatus.INTERNAL_SERVER_ERROR);				

		}
		return resp;
	}



	// delete shipment by id
	@DeleteMapping("/delete/{sid}")  // here we are using @PathVaraible to getting dynamic primitive input
	public ResponseEntity<?> deleteShipment(@PathVariable Integer sid)
	{
		ResponseEntity<?> resp = null;
		try {
			service.deleteShipmentType(sid);

			resp = new ResponseEntity<String>(sid+" deleted succesfully",HttpStatus.OK);
		}
		catch (Exception e) {
			resp = new ResponseEntity<String>("Unable to Process "+sid+" not found",HttpStatus.BAD_REQUEST);

		}
		return resp;
	}


	// update shipment 
	@PutMapping("/update")
	public ResponseEntity<?> updateShipment(@RequestBody ShipmentType shipmentType)
	{
		ResponseEntity<?> resp = null;
		try {
			service.updateShipmentType(shipmentType.getShipmentId(),shipmentType);
			resp = new ResponseEntity<String>(shipmentType.getShipmentId()+" shipment updated successfully ",HttpStatus.OK);
		}
		catch (Exception e) {
			resp = new ResponseEntity<String>(shipmentType.getShipmentId()+" Not found ",HttpStatus.BAD_REQUEST);
		}
		return resp;
	}



}
