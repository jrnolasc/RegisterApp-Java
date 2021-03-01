package edu.uark.registerapp.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.uark.registerapp.controllers.enums.QueryParameterNames;
import edu.uark.registerapp.controllers.enums.QueryParameterMessages;
import edu.uark.registerapp.controllers.enums.ViewNames;
import edu.uark.registerapp.controllers.enums.ViewModelNames;
import edu.uark.registerapp.commands.employees.ActiveEmployeeExistsQuery;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.models.api.EmployeeSignIn;

@Controller
@RequestMapping(value = "/")

public class SignInRouteController extends BaseRouteController {
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView requestingDocumentAndView(@RequestParam Map<String,String> userAndPass)
	{
		 try {
			this.activeEmployeeExistsQuery.execute();
		 } catch (NotFoundException e) {  //exception that ActiveEmployeeExistsQuery throws
			//if no employee exists should redirect to employee detail view
			return new ModelAndView(REDIRECT_PREPEND.concat(ViewNames.EMPLOYEE_DETAIL.getRoute()));
		 }
		
		ModelAndView modelAndView = this.setErrorMessageFromQueryString(new ModelAndView(ViewNames.SIGN_IN.getViewName()), userAndPass);
		if (userAndPass.containsKey(QueryParameterNames.EMPLOYEE_ID.getValue())){ //if employee exists should serve up the sign in
			modelAndView.addObject(ViewModelNames.EMPLOYEE_ID.getValue(), userAndPass.get(QueryParameterNames.EMPLOYEE_ID.getValue()));
		}
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView performSignIn (
		EmployeeSignIn employeeSignIn, 
		HttpServletRequest request
	) {

		// TODO: Use the credentials provided in the request body
		//  and the "id" property of the (HttpServletRequest)request.getSession() 
		// variable to sign in the user
		try {
			//if employee exists should serve up the sign in
			this.activeEmployeeExistsQuery.execute();
		 } catch (NotFoundException e) {  //exception that ActiveEmployeeExistsQuery throws
			//if no employee exists should redirect to employee detail view
			return new ModelAndView(REDIRECT_PREPEND.concat(ViewNames.EMPLOYEE_DETAIL.getRoute()));
		 }

		

		return new ModelAndView(
			REDIRECT_PREPEND.concat(
				ViewNames.MAIN_MENU.getRoute()));
	}

		// Properties
		@Autowired
		private ActiveEmployeeExistsQuery activeEmployeeExistsQuery;

}
