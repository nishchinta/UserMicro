package com.management.UserMS.Controller;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.management.UserMS.Exceptions.InactivationException;
import com.management.UserMS.Exceptions.InvalidEmailIdException;
import com.management.UserMS.Exceptions.RegistrationException;
import com.management.UserMS.Exceptions.UserException;
//import com.management.UserMS.Validator.BuyerValidator;
import com.management.UserMS.dto.BuyerDTO;
import com.management.UserMS.dto.CartDTO;
import com.management.UserMS.dto.ProductDTO;
import com.management.UserMS.dto.SellerDTO;
import com.management.UserMS.entity.Buyer;
import com.management.UserMS.service.BuyerService;

@RestController
@CrossOrigin
@RequestMapping(value="/api")
public class BuyerController {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	BuyerService buyerService;
	
	@Autowired
    RestTemplate template;

	

	@PostMapping(value="buyer/register",  consumes = MediaType.APPLICATION_JSON_VALUE)
	public String registerUser(@RequestBody BuyerDTO buyerDTO) throws UserException {
		try {
		logger.info("Registration request for buyer by {}", buyerDTO.getEmail()) ;
		buyerDTO.setIsActive(true);
		buyerDTO.setIsPrivileged(false);
		buyerDTO.setRewardPoints(0);
		buyerService.registerBuyer(buyerDTO);
		return "Buyer Registration Successfull";
	}catch(Exception e) {
		throw new RegistrationException("Buyer Registration Unsuccessfull, Please provide proper credentials");
	}
	}

	@PostMapping(value = "buyer/login",consumes = MediaType.APPLICATION_JSON_VALUE)
	public String loginBuyer(@RequestBody BuyerDTO buyerDTO) throws LoginException {
		try {
			boolean Status= buyerService.buyerLogin(buyerDTO);
			if (Status) {
				return "Login Successfull";
			}
			else {
				return "Login Unsuccessfull; Give Proper Credentials"; 
			}
		} catch (Exception e) {
			throw new LoginException("Login Unsuccessfull");
		}
	}


	@PostMapping(value = "buyer/inactivate",consumes = MediaType.APPLICATION_JSON_VALUE)
	public String inactivateSeller(@RequestBody BuyerDTO buyerDTO) throws UserException{
			try {
				boolean Status = buyerService.inactivateBuyer(buyerDTO);
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
	
	@GetMapping(value = "rewardPoint/{buyerId}")
	public int getRewardPoints(@PathVariable int buyerId) {
		System.out.println("BuyerID is:"+ buyerId);
		int Points = buyerService.getRewardPoints(buyerId);
		return Points;
	
	}
	
	@PutMapping(value = "rewardPoint/update/{buyerId}/{point}")
	public void updateRewardPoint(@PathVariable int buyerId,@PathVariable int point) {
	
		buyerService.updateRewardPoint(buyerId, point);

	}
	
	@GetMapping(value = "buyer/isPrivilege/{buyerId}")
	public boolean isBuyerPrivileged(@PathVariable int buyerId) {
		System.out.println("inside buyer privilege");
		return buyerService.IsPrivileged(buyerId);
	}
	@DeleteMapping(value="/removebuyer/{buyerid}", consumes= MediaType.APPLICATION_JSON_VALUE)
	public String removebuyer(@PathVariable Integer buyerid) {
		logger.info("Request for order removal by buyer {}", buyerid);
		buyerService.removebuyer(buyerid);
		return "Deleted successfully";
		
		
	} 
	@PostMapping(value="/add/wishlist/{prodid}/{productname}",  consumes = MediaType.APPLICATION_JSON_VALUE)
	public String addtowishlist(@PathVariable Integer prodid,@PathVariable String productname) {
		try {
			logger.info("Add products to wishlist request {}", productname);
			ProductDTO prodDTO=template.getForObject("http://PRODUCTMS"+"/products/productid/"+prodid,ProductDTO.class);
			buyerService.addtowishlist(prodDTO);
			return "Added successfully";
		
		}catch(Exception e) {
			return "Unable to add";
		}
		}
	
	@PostMapping(value="/add/cart/{prodid}/{productname}/{quantity}",  consumes = MediaType.APPLICATION_JSON_VALUE)
	public String addtocart(@PathVariable Integer prodid,@PathVariable String productname, @PathVariable Integer quantity) {
		try {
			logger.info("Add products to order request {}", productname);
			ProductDTO prodDTO=template.getForObject("http://PRODUCTMS"+"/products/productid/"+prodid,ProductDTO.class);
			CartDTO cart=new CartDTO();
			cart.setProdid(prodDTO.getProdid());
			cart.setQuantity(quantity);
			buyerService.addtocart(cart);
			return "Added successfully";
		
		}catch(Exception e) {
			return "Unable to add";
		}
		}
	
	@DeleteMapping(value="/delete/{buyerid}")
	public String delete(@PathVariable Integer buyerid) {
		try {
			buyerService.delete(buyerid);
			return "deleted successfully";
		}catch(Exception e) {
			return "Unable to delete";
		}
		
	}
}

