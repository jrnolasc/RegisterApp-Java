package edu.uark.registerapp.commands.employees;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uark.registerapp.commands.ResultCommandInterface;
import edu.uark.registerapp.commands.employees.helpers.EmployeeHelper;
import edu.uark.registerapp.commands.exceptions.UnprocessableEntityException;
import edu.uark.registerapp.models.api.Employee;
import edu.uark.registerapp.models.entities.EmployeeEntity;
import edu.uark.registerapp.models.enums.EmployeeClassification;
import edu.uark.registerapp.models.repositories.EmployeeRepository;

@Service
public class EmployeeCreateCommand implements ResultCommandInterface<Employee>{

    public EmployeeCreateCommand(){
        this.initialEmployee = false;
    }
    
    private Employee apiEmployee;
    public Employee getApiEmployee(){
        return this.apiEmployee;
    }
    private boolean initialEmployee;
    public boolean getIsInitialEmployee(){
        return this.initialEmployee;
    }

    @Autowired
    private EmployeeRepository empRepo;
    
    private void validateEmployee(){
        if (StringUtils.isBlank(this.apiEmployee.getFirstName())){
            throw new UnprocessableEntityException("First Name empty");
        }
        if (StringUtils.isBlank(this.apiEmployee.getLastName())){
            throw new UnprocessableEntityException("Last Name empty");
        }
        if (StringUtils.isBlank(this.apiEmployee.getPassword())){
            throw new UnprocessableEntityException("Password is empty");
        }

        if(!this.getIsInitialEmployee() && (EmployeeClassification.map(
            this.apiEmployee.getClassification()) == EmployeeClassification.NOT_DEFINED)){
                throw new UnprocessableEntityException("Classification not defined");
        }
    }
    @Override
    public Employee execute(){
        this.validateEmployee();
        
        if(this.initialEmployee){
            this.apiEmployee.setClassification(EmployeeClassification.GENERAL_MANAGER.getClassification());
        }

        EmployeeEntity empEntity = this.empRepo.save(new EmployeeEntity(this.apiEmployee));

        this.apiEmployee.setId(empEntity.getId());
        //the password is blank on purpsose;
        this.apiEmployee.setPassword("");
        this.apiEmployee.setEmployeeId(empEntity.getEmployeeId());
        this.apiEmployee.setCreatedOn(empEntity.getCreatedOn());
        this.apiEmployee.setEmployeeId(EmployeeHelper.padEmployeeId(empEntity.getEmployeeId()));

        return this.apiEmployee;
    }

    public EmployeeCreateCommand setInitialEmployee(boolean initialEmployee){
        this.initialEmployee = initialEmployee;
        return this;
    }
    
    
    
}
