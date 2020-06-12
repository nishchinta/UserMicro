package com.management.UserMS.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.UserMS.Exceptions.InactivationException;
import com.management.UserMS.Exceptions.RegistrationException;
import com.management.UserMS.Exceptions.UserException;
import com.management.UserMS.dto.BuyerDTO;
import com.management.UserMS.dto.SellerDTO;
import com.management.UserMS.entity.Seller;
import com.management.UserMS.repository.SellerRepository;
import com.management.UserMS.service.SellerService;

@RestController
@CrossOrigin
@RequestMapping(value="/api")
public class SellerController {

	private static final Logger logger = LoggerFactory.getLogger(SellerController.class);	
	@Autowired
	private SellerService sellerService;

	
	
	@PostMapping(value = "seller/register")
	public String registerSeller(@RequestBody SellerDTO sellerDTO) throws Exception {

		try {
			logger.info("Seller Registration is being done by " + sellerDTO.getName());
			sellerDTO.setIsActive(true);
			sellerService.sellerRegisterion(sellerDTO);
			return "Buyer Registration Successfull";

		} catch(Exception e) {
			throw new RegistrationException("Seller Registration Unsuccessfull, Please provide proper credentials");
		
	}
		
	}
	
	@PostMapping(value = "seller/login",consumes = MediaType.APPLICATION_JSON_VALUE)
	public String loginSeller(@RequestBody SellerDTO sellerDTO){
		try {
			boolean Status= sellerService.sellerLogin(sellerDTO);
			if (Status) {
				return "Login Successfull";
			}
			else {
				return "Login Unsuccessfull; Give Proper Credentials"; 
			}
		} catch (Exception e) {
			return " Login unsuccessfull, check your credentials and try again";
		}
	}
	
	@PostMapping(value = "seller/inactivate",consumes = MediaType.APPLICATION_JSON_VALUE)
	public String inactivateSeller(@RequestBody SellerDTO sellerDTO) throws UserException{
			try {
				boolean Status = sellerService.inactivateSeller(sellerDTO);
				if(Status) {
				return "Inactivation Successfull";
				}
				else {
					return "Inactivation Unsuccessfull";
				}

			} catch (Exception e) {
				throw new InactivationException(" Account Inactivation Unsuccessful. Give correct credentials");
			}
	}
	
	@DeleteMapping(value="/removeseller/{sellerid}", consumes= MediaType.APPLICATION_JSON_VALUE)
	public String removeseller(@PathVariable Integer sellerid) {
		logger.info("Request for order removal by seller {}", sellerid);
		sellerService.removeseller(sellerid);
		return "Deleted successfully";
		
		
	} 
	
	
}
