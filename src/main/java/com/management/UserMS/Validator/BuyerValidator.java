package com.management.UserMS.Validator;

import java.util.regex.Pattern;

import javax.naming.InvalidNameException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.management.UserMS.Exceptions.InvalidEmailIdException;
import com.management.UserMS.Exceptions.InvalidPasswordException;
import com.management.UserMS.Exceptions.InvalidPhoneNumberException;
import com.management.UserMS.Validator.BuyerValidator;
import com.management.UserMS.dto.BuyerDTO;
import com.management.UserMS.service.BuyerService;

@Service
public class BuyerValidator{
private static final Logger logger = LoggerFactory.getLogger(BuyerService.class);


public static void validateBuyerDetails(BuyerDTO buyerDTO) throws Exception {

		logger.info("Buyer details are being validated");
		if(!isValidName(buyerDTO.getName()))
			throw new InvalidNameException("BuyerRegistration: Invalid Name");
		if(!isValidEmail(buyerDTO.getEmail()))
			throw new InvalidEmailIdException("BuyerRegistration: Invalid Email");
		if(!isValidPhoneNumber(buyerDTO.getPhoneNumber()))
			throw new InvalidPhoneNumberException("BuyerRegistration:Invalid Phone number");
		if(!isvalidPassword(buyerDTO.getPassword()))
			throw new InvalidPasswordException("BuyerRegistration: Invalid Password");
	
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
