package com.management.UserMS.service;

import java.util.regex.Pattern;

import javax.naming.InvalidNameException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.management.UserMS.Exceptions.EmailAlreadyExistsException;
import com.management.UserMS.Exceptions.InvalidPasswordException;
import com.management.UserMS.Exceptions.PhoneNumberAlreadyExistsException2;
import com.management.UserMS.Exceptions.RegistrationException;
import com.management.UserMS.Exceptions.UserException;
import com.management.UserMS.Validator.SellerValidator;
import com.management.UserMS.Controller.SellerController;
import com.management.UserMS.dto.BuyerDTO;
import com.management.UserMS.dto.SellerDTO;
import com.management.UserMS.entity.Buyer;
import com.management.UserMS.entity.Seller;
import com.management.UserMS.repository.SellerRepository;
@Service
public class SellerService {
	private static final Logger logger = LoggerFactory.getLogger(SellerController.class);
	@Autowired
	SellerRepository sellerRepo;
	
	@Autowired
	SellerService sellerService;

	
	
	public String sellerRegisterion(SellerDTO sellerDTO) throws UserException {
		try {
		logger.info("Registration request for seller {}", sellerDTO);
		validateRedundancy(sellerDTO);
		SellerValidator.validateSellerDetails(sellerDTO);
		Seller se=sellerDTO.createEntity();
		sellerRepo.save(se);
		return("Seller Registered Successfully");
		}catch(Exception e) {
			throw new RegistrationException("Registration Unsuccessfull, Please give correct Credentials");
		}	
	}
	
	public void validateRedundancy(SellerDTO sellerDTO) throws Exception {
		logger.info("Existing Data are being checked");
		if(!EmailIdAlreadyExists(sellerDTO.getEmail()))
			throw new EmailAlreadyExistsException("Email Already exists, try with another email");
		if(!PhoneNumberAlreadyExists(sellerDTO.getPhoneNumber()))
			throw new PhoneNumberAlreadyExistsException2("Phone Number Already exists");

		
	}
	

	private boolean EmailIdAlreadyExists(String email) {
		Seller seller=sellerRepo.findByEmail(email);
		if (seller!=null)
			return false;
		return true;
	}

	private boolean PhoneNumberAlreadyExists(String phoneNumber) {
		Seller seller = sellerRepo.findByPhoneNumber(phoneNumber);
		if (seller!=null)
			return false;
		return true;
	}
	
	public boolean sellerLogin(SellerDTO sellerDTO) throws Exception {

		Seller seller = sellerRepo.findByEmail(sellerDTO.getEmail());
		if (seller != null) {
			if(seller.getEmail().equals(sellerDTO.getEmail()) && seller.getPassword().equals(sellerDTO.getPassword())){
				return true;
			} else {
				throw new InvalidPasswordException("BuyerLogin:Invalid Password");
			}
		}
		return false;
	}
	
	public boolean inactivateSeller(SellerDTO sellerDTO) throws UserException {

		Seller seller = sellerRepo.findByEmail(sellerDTO.getEmail());
		if (seller != null) {
			if(seller.getEmail().equals(sellerDTO.getEmail()) && seller.getPassword().equals(sellerDTO.getPassword())){
			seller.setIsActive(false);
			sellerRepo.save(seller);
			return true;
		} else {
			throw new InvalidPasswordException("Please provide correct credentials") ;
		}}
			return false;
		
		}
	public void removeseller(Integer sellerid) {
		//Optional<Buyer> buyer=buyerRepo.findByBuyerId(sellerid);
		sellerRepo.deleteById(sellerid);
	}
	
}