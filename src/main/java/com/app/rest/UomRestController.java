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

import com.app.model.Uom;
import com.app.service.IUomService;

@RestController
@RequestMapping("/rest/uom/")
public class UomRestController {


	@Autowired
	private IUomService service;

	// get data
	@GetMapping("/all")
	public ResponseEntity<?> getAll()
	{
		ResponseEntity<?> resp=null;

		List<Uom> list=service.getAllUoms();

		if(list!=null && !list.isEmpty())
		{
			resp = new ResponseEntity<List<Uom>>(list,HttpStatus.OK);
		}
		else
		{
			resp = new ResponseEntity<String>("No data found",HttpStatus.OK);
		}

		return resp;
	}



	/// save data 

	@PostMapping("/save")
	public ResponseEntity<String> saveData(@RequestBody Uom uom)
	{
		ResponseEntity<String> resp = null;

		try
		{
			Integer sid = service.saveUom(uom);
			resp = new ResponseEntity<String>("saved with id = "+sid,HttpStatus.OK);
		}
		catch (Exception e) {

			resp = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}


		return resp;
	}


	// get one record
	@GetMapping("/getOne/{uid}")  // here we are using @PathVaraible to getting dynamic primitive input
	public ResponseEntity<?> getOneUom(@PathVariable Integer uid)
	{
		ResponseEntity<?> resp = null;
		try {
			Uom od =service.getUomId(uid);

			if(od!=null)
			{
				resp = new ResponseEntity<Uom>(od,HttpStatus.OK);				
			}
			else
			{
				resp = new ResponseEntity<String>(uid+" not found",HttpStatus.BAD_REQUEST);
			}

		}
		catch (Exception e) {
			resp = new ResponseEntity<String>("Unable to Process ",HttpStatus.INTERNAL_SERVER_ERROR);				

		}
		return resp;
	}



	// delete uom by id
	@DeleteMapping("/delete/{uid}")  // here we are using @PathVaraible to getting dynamic primitive input
	public ResponseEntity<?> deleteUom(@PathVariable Integer uid)
	{
		ResponseEntity<?> resp = null;
		try {
			service.deleteUom(uid);

			resp = new ResponseEntity<String>(uid+" deleted succesfully",HttpStatus.OK);
		}
		catch (Exception e) {
			resp = new ResponseEntity<String>("Unable to Process "+uid+" not found",HttpStatus.BAD_REQUEST);

		}
		return resp;
	}


	// update uom
	@PutMapping("/update")
	public ResponseEntity<?> updateUom(@RequestBody Uom uom)
	{
		ResponseEntity<?> resp = null;
		try {
			service.updateUom(uom);
			resp = new ResponseEntity<String>(uom.getId() +" uom updated successfully ",HttpStatus.OK);
		}
		catch (Exception e) {
			resp = new ResponseEntity<String>(uom.getId() +" Not found ",HttpStatus.BAD_REQUEST);
		}
		return resp;
	}



}