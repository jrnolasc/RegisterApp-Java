package edu.uark.registerapp.models.api;

public class EmployeeSignIn {
    public String employeeId;
    public String password;

    public EmployeeSignIn() {  //constructor
        //initialize variables
        this.employeeId = "";    
        this.password = "";    
    }

    //get and set employeeId;
    public String getEmployeeId() {
    	return this.employeeId;
    }
    public EmployeeSignIn setEmployeeId(final String employeeId) {
		this.employeeId = employeeId;
		return this;
	}

    //get and set password;
    public String getPassword() {
    	return this.password;
    }
    public EmployeeSignIn setPassword(final String password) {
		this.password = password;
		return this;
	}

}
