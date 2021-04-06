package edu.uark.registerapp.controllers;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.controllers.enums.ViewModelNames;
import edu.uark.registerapp.controllers.enums.ViewNames;
import edu.uark.registerapp.models.api.Employee;
import edu.uark.registerapp.models.entities.ActiveUserEntity;
import edu.uark.registerapp.commands.employees.ActiveEmployeeExistsQuery;

@Controller
@RequestMapping(value = "/employeeDetail")
public class EmployeeDetailRouteController extends BaseRouteController {
    @RequestMapping(method = RequestMethod.GET)
	public ModelAndView start(
	 	@RequestParam final Map<String, String> queryParameters,
	 	final HttpServletRequest request
	 ) {
		//if employee exists
		if (this.activeUserExists()){
			//if no active user for current session
			if (!(this.getCurrentUser(request).isPresent())) 
				//redirects to sign in
				return this.buildInvalidSessionResponse(); 
			else 
				//else redirects to main menu and displays error
				return this.buildNoPermissionsResponse();
		}
		//queries new employee details
		return this.buildStartResponse(!(this.activeUserExists()), (new UUID(0, 0)), queryParameters);
	}

    @RequestMapping(value = "/{employeeId}", method = RequestMethod.GET)
	public ModelAndView startWithEmployee(
		@PathVariable final UUID employeeId,
		@RequestParam final Map<String, String> queryParameters,
		final HttpServletRequest request
	) {
		//if no active user for current session
		if (!(this.getCurrentUser(request).isPresent())) {
			//redirects to sign in page
			return this.buildInvalidSessionResponse();
		} else if (!this.isElevatedUser(this.getCurrentUser(request).get())) {
			//if current user is not elevated, redirect to main menu
			return this.buildNoPermissionsResponse();
		}
		//Query employee details for the employee ID path variable
		return this.buildStartResponse(false, employeeId, queryParameters);
	}

	// Helper methods
	private boolean activeUserExists() {
		// Helper method to determine if any active users Exist
		try {
			//if executes, user exists
			this.activeEmployeeExistsQuery.execute();
			return true;
		} catch (final NotFoundException e) {
			//if exception is thrown, user does not exist
			return false;
		}
	}

	private ModelAndView buildStartResponse(
		final boolean isInitialEmployee,
		final UUID employeeId,
		final Map<String, String> queryParameters
	) {
		ModelAndView modelAndView =
			this.setErrorMessageFromQueryString(
				new ModelAndView(ViewNames.EMPLOYEE_DETAIL.getViewName()),
				queryParameters);

		//if employee id is equal to the initial
		if (employeeId.equals(new UUID(0, 0))) {
			//add employee id/set as initial employee
			modelAndView.addObject(
				ViewModelNames.EMPLOYEE_ID.getValue(), (new Employee()).setIsInitialEmployee(isInitialEmployee)
				);
		} else {
			try {
				//add employee id
				modelAndView.addObject(
					ViewModelNames.EMPLOYEE_ID.getValue(),
					this.employeeQuery.setId(employeeId).execute().setIsInitialEmployee(isInitialEmployee)
					);
			} catch (final Exception e) {
				//if exception, display error message and add as new employee
				modelAndView.addObject(
					ViewModelNames.ERROR_MESSAGE.getValue(),
					e.getMessage());
				modelAndView.addObject(
					ViewModelNames.EMPLOYEE_ID.getValue(),
					(new Employee()).setIsInitialEmployee(isInitialEmployee));
			}
		}
		
		return modelAndView;
		}

	// Properties
	@Autowired
	private ActiveEmployeeExistsQuery employeeQuery;
	
	@Autowired
	private ActiveEmployeeExistsQuery activeEmployeeExistsQuery;
}
