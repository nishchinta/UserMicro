package com.management.UserMS.Validator;

import java.util.regex.Pattern;

import javax.naming.InvalidNameException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.management.UserMS.Exceptions.InvalidEmailIdException;
import com.management.UserMS.Exceptions.InvalidPasswordException;
import com.management.UserMS.Exceptions.InvalidPhoneNumberException;
import com.management.UserMS.Validator.SellerValidator;
import com.management.UserMS.dto.BuyerDTO;
import com.management.UserMS.dto.SellerDTO;
import com.management.UserMS.service.BuyerService;

@Service
public class SellerValidator{
private static final Logger logger = LoggerFactory.getLogger(BuyerService.class);


public static void validateSellerDetails(SellerDTO sellerDTO) throws Exception {

		logger.info("Seller details are being validated");
		if(!isValidName(sellerDTO.getName()))
			throw new InvalidNameException("SellerRegistration: Invalid Name");
		if(!isValidEmail(sellerDTO.getEmail()))
			throw new InvalidEmailIdException("SellerRegistration: Invalid Email");
		if(!isValidPhoneNumber(sellerDTO.getPhoneNumber()))
			throw new InvalidPhoneNumberException("SellerRegistration:Invalid Phone number");
		if(!isvalidPassword(sellerDTO.getPassword()))
			throw new InvalidPasswordException("SellerRegistration: Invalid Password");
	}

	public static boolean isvalidPassword(String password) {
		return Pattern.matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{7,20}$",password);
	}

	public static boolean isValidPhoneNumber(String phoneNumber) {
		// TODO Auto-generated method stub
		return Pattern.matches("^\\d{10}$", phoneNumber);
	}

	public static boolean isValidEmail(String email) {
		// TODO Auto-generated method stub
		return Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$",email);
	}

	public static boolean isValidName(String name) {
		// TODO Auto-generated method stub
		return Pattern.matches("^[a-zA-Z]+[-a-zA-Z\\s]+([-a-zA-Z]+)$", name);
	}


}
