package edu.uark.registerapp.commands.employees;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import edu.uark.registerapp.commands.ResultCommandInterface;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.commands.exceptions.UnprocessableEntityException;
import edu.uark.registerapp.models.api.Employee;
import edu.uark.registerapp.models.entities.EmployeeEntity;
import edu.uark.registerapp.models.enums.EmployeeClassification;
import edu.uark.registerapp.models.repositories.EmployeeRepository;


public class EmployeeUpdateCommand implements ResultCommandInterface<Employee> {
    private Employee apiEmployee;
    public Employee getApiEmployee(){
        return this.apiEmployee;
    }
    public EmployeeUpdateCommand setApiEmployee(Employee apiEmployee){
        this.apiEmployee = apiEmployee;
        return this;
    }
    private UUID employeeId;
    public UUID getEmployeeId(){
        return this.employeeId;
    }
    public EmployeeUpdateCommand setEmployeeId(UUID employeeId){
        this.employeeId = employeeId;
        return this;
    }
    @Autowired
    private EmployeeRepository empRepo;
    
    //the heavy lifting
    @Override
    public Employee execute(){
        this.validateEmployee();
        this.updateEmployee();
        return this.apiEmployee;
    }

    //checks to ensure the fields are not empty
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
        if((EmployeeClassification.map(this.apiEmployee.getClassification()) == EmployeeClassification.NOT_DEFINED)){
                throw new UnprocessableEntityException("Classification not defined");
        }
    }

    @Transactional
    private void updateEmployee(){
        Optional<EmployeeEntity> searchEmpEntity = this.empRepo.findById(this.employeeId);

        if(!searchEmpEntity.isPresent()){
            throw new NotFoundException("Employee ID not found.");
        }

        this.apiEmployee = searchEmpEntity.get().synchronize(this.apiEmployee);
        this.empRepo.save(searchEmpEntity.get());
    }
    
}
