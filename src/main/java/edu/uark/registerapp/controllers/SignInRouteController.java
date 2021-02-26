package edu.uark.registerapp.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.uark.registerapp.models.api.*;
import edu.uark.registerapp.controllers.enums.ViewNames;

@Controller
@RequestMapping(value = "/")

public class SignInRouteController extends BaseRouteController {
	// @RequestMapping(method = RequestMethod.GET)
	// public ModelAndView requestingDocumentAndView(
	// 	// @RequestParam Map<String,String> userAndPass
	// ){
	// 	// try {
	
	// 	// } catch (final Exception e) {
		
	// 	// }
	// 	ModelAndView modelAndView = null;
	// 	return modelAndView;
	// 	//return "Parameters are " + userAndPass.entrySet();
	// }

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView performSignIn (
		EmployeeSignIn employeeSignIn, 
		HttpServletRequest request
	) {

		// TODO: Use the credentials provided in the request body
		//  and the "id" property of the (HttpServletRequest)request.getSession() variable
		//  to sign in the user

		return new ModelAndView(
			REDIRECT_PREPEND.concat(
				ViewNames.MAIN_MENU.getRoute()));
	}

}
