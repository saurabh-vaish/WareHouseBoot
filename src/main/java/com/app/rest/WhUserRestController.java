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
import com.app.model.WhUser;
import com.app.service.IWhUserService;

@RestController
@RequestMapping("/rest/whUser/")
public class WhUserRestController {


	@Autowired
	private IWhUserService service;

	// get data
	@GetMapping("/all")
	public ResponseEntity<?> getAll()
	{
		ResponseEntity<?> resp=null;

		List<WhUser> list=service.getAllWhUsers();

		if(list!=null && !list.isEmpty())
		{
			resp = new ResponseEntity<List<WhUser>>(list,HttpStatus.OK);
		}
		else
		{
			resp = new ResponseEntity<String>("No data found",HttpStatus.OK);
		}

		return resp;
	}



	/// save data 

	@PostMapping("/save")
	public ResponseEntity<String> saveData(@RequestBody WhUser whUser)
	{
		ResponseEntity<String> resp = null;

		try
		{
			Integer uid = service.saveWhUser(whUser);
			resp = new ResponseEntity<String>("saved with id = "+uid,HttpStatus.OK);
		}
		catch (Exception e) {

			resp = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}


		return resp;
	}


	// get one record
	@GetMapping("/getOne/{uid}")  // here we are using @PathVaraible to getting dynamic primitive input
	public ResponseEntity<?> getOneWhUser(@PathVariable Integer uid)
	{
		ResponseEntity<?> resp = null;
		try {
			WhUser wh =service.getWhUserById(uid);

			if(wh!=null)
			{
				resp = new ResponseEntity<WhUser>(wh,HttpStatus.OK);				
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



	// delete whUser by id
	@DeleteMapping("/delete/{uid}")  // here we are using @PathVaraible to getting dynamic primitive input
	public ResponseEntity<?> deleteWhUser(@PathVariable Integer uid)
	{
		ResponseEntity<?> resp = null;
		try {
			service.deleteWhUser(uid);

			resp = new ResponseEntity<String>(uid+" deleted succesfully",HttpStatus.OK);
		}
		catch (Exception e) {
			resp = new ResponseEntity<String>("Unable to Process "+uid+" not found",HttpStatus.BAD_REQUEST);

		}
		return resp;
	}


	// update whUser
	@PutMapping("/update")
	public ResponseEntity<?> updateWhUser(@RequestBody WhUser whUser)
	{
		ResponseEntity<?> resp = null;
		try {
			service.updateWhUser(whUser);
			resp = new ResponseEntity<String>(whUser.getWhId() +" whUser updated successfully ",HttpStatus.OK);
		}
		catch (Exception e) {
			resp = new ResponseEntity<String>(whUser.getWhId() +" Not found ",HttpStatus.BAD_REQUEST);
		}
		return resp;
	}



}
