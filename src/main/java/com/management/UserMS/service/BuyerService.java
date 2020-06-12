package com.management.UserMS.service;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.UserMS.Exceptions.EmailAlreadyExistsException;
import com.management.UserMS.Exceptions.InactivationException;
import com.management.UserMS.Exceptions.InvalidPasswordException;
import com.management.UserMS.Exceptions.PhoneNumberAlreadyExistsException2;
import com.management.UserMS.Exceptions.UserException;
import com.management.UserMS.Validator.BuyerValidator;
//import com.management.UserMS.Validator.BuyerValidator;
//import com.management.UserMS.Validator.BuyerValidator;
import com.management.UserMS.dto.BuyerDTO;
import com.management.UserMS.dto.CartDTO;
import com.management.UserMS.dto.ProductDTO;
import com.management.UserMS.dto.SellerDTO;
import com.management.UserMS.dto.WishlistDTO;
import com.management.UserMS.entity.Buyer;
import com.management.UserMS.entity.Cart;
import com.management.UserMS.entity.Seller;
import com.management.UserMS.entity.Wishlist;
import com.management.UserMS.repository.BuyerRepository;
import com.management.UserMS.repository.CartRepository;
import com.management.UserMS.repository.WishlistRepository;
@Service
public class BuyerService {
	private static final Logger logger = LoggerFactory.getLogger(BuyerService.class);
	@Autowired
	BuyerRepository buyerRepo;
	
	@Autowired
	WishlistRepository wishlistRepo;
	
	@Autowired
	CartRepository cartRepo;
	
	
	
	public String registerBuyer(BuyerDTO buyerDTO) throws Exception {
		
		try {
		logger.info("Registration request for user {}", buyerDTO);
		validateRedundancy(buyerDTO);
		BuyerValidator.validateBuyerDetails(buyerDTO);
		Buyer be=buyerDTO.createEntity();
		buyerRepo.save(be);
		return("Buyer Registered Successfully");
		}catch(Exception e) {
			throw new Exception("Registration Unsuccessfull, Please give correct Credentials");
		}
	}
	
	public void validateRedundancy(BuyerDTO buyerDTO) throws UserException {
		logger.info("Existing Data are being checked");
		if(!EmailIdAlreadyExists(buyerDTO.getEmail()))
			throw new EmailAlreadyExistsException("Email Already exists, try with another email");
		if(!PhoneNumberAlreadyExists(buyerDTO.getPhoneNumber()))
			throw new PhoneNumberAlreadyExistsException2("Phone Number Already exists");

		
	}
	private boolean EmailIdAlreadyExists(String email) {
		Buyer buyer=buyerRepo.findByEmail(email);
		if (buyer!=null)
			return false;
		return true;
	}

	private boolean PhoneNumberAlreadyExists(String phoneNumber) {
		Buyer buyer=buyerRepo.findByPhoneNumber(phoneNumber);
		if (buyer!=null)
			return false;
		return true;
	}	

	public boolean buyerLogin(BuyerDTO buyerDTO) throws Exception {

		Buyer buyer = buyerRepo.findByEmail(buyerDTO.getEmail());
		if (buyer != null) {
			if(buyer.getEmail().equals(buyerDTO.getEmail()) && buyer.getPassword().equals(buyerDTO.getPassword())) {
			return true;
			} else {
				throw new InvalidPasswordException("BuyerLogin:Invalid Password");
			}
		}
		return false;
	}

	public boolean inactivateBuyer(BuyerDTO buyerDTO) throws UserException {

		Buyer buyer = buyerRepo.findByEmail(buyerDTO.getEmail());
		if (buyer != null) {
			if(buyer.getEmail().equals(buyerDTO.getEmail()) && buyer.getPassword().equals(buyerDTO.getPassword())) {
			buyer.setIsActive(false);
			buyerRepo.save(buyer);
			return true;
		} else {
			throw new InvalidPasswordException("Please provide correct credentials ");
		}
	}
			return false;
		}
	
	
	public int getRewardPoints(int buyerId) {
		System.out.println("BuyerId"+buyerId);
		Buyer buyer=buyerRepo.findByBuyerId(buyerId);
		return buyer.getRewardPoints();

		
	}
	
	public void updateRewardPoint(int buyerId, int point) {
		Buyer buyer =buyerRepo.findByBuyerId(buyerId);
		if (buyer!=null){
		buyer.setRewardPoints(point);
		buyerRepo.save(buyer);
		}else {
			System.out.println("Invalid BuyerId");
		}
		
	}
//	public void updateBuyerPrivilege(String email, boolean privilege) throws Exception {
//		Buyer buyer= buyerRepo.findByEmail(email);
//		if(buyer!=null){
//			if(buyer.getIsPrivileged()) {
//				if(!privilege) {
//					buyer.setIsPrivileged(privilege);
//				    buyerRepo.save(buyer);
//					
//				}
//				else {
//				throw new Exception("Buyer is already privileged");
//				}
//			}
//			else if(!(buyer.getIsPrivileged())) {
//				if(privilege) {
//					if(buyer.getRewardPoints()<10000) {
//						throw new Exception("Insufficient Reward Points");
//					}
//					else {
//						buyer.setIsPrivileged(privilege);
//						buyer.setRewardPoints(buyer.getRewardPoints()-10000);
//						buyerRepo.save(buyer);
//					}	
//				}
//				else {
//					
//					throw new Exception("Not Privileged");
//					
//				}
//			}
//		buyer.setIsPrivileged(privilege);
//	    buyerRepo.save(buyer);
//		}
//		else {
//			throw new Exception("Invalid Email");
//		}
//		
//			
//	}
	
public boolean IsPrivileged(int buyerId) {
		
		Buyer buyer= buyerRepo.findByBuyerId(buyerId);
		
		if((buyer.getIsPrivileged())==false) {
			
			return false;
		}
		else {
			
			return true;
		}
		
	}
	public void removebuyer(Integer buyerid) {
		//Optional<Buyer> buyer=buyerRepo.findByBuyerId(buyerid);
		buyerRepo.deleteById(buyerid);
	}
	
	public void addtowishlist(ProductDTO prodDTO) {
		System.out.println("in service");
		WishlistDTO wishlist=new WishlistDTO();
		wishlist.setBuyerid(543);
		wishlist.setProdid(prodDTO.getProdid());
		
	
			WishlistDTO wo= new WishlistDTO();
			Wishlist we= wishlist.createEntity();
			wishlistRepo.save(we);

		
	}
	
	public void addtocart(CartDTO cartDTO) {
		System.out.println("in service");
		CartDTO cart1=new CartDTO();
		cart1.setBuyerid(500);
		cart1.setProdid(cartDTO.getProdid());
		cart1.setQuantity(cartDTO.getQuantity());
		Cart cart2= cart1.createEntity();
		cartRepo.save(cart2);

		
	}
	public void delete(Integer buyerid) {
		
		wishlistRepo.deleteById(buyerid);
		
	}
}
