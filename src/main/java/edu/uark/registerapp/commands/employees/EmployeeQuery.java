package edu.uark.registerapp.commands.employees;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.uark.registerapp.commands.exceptions.NotFoundException;

import edu.uark.registerapp.commands.ResultCommandInterface;
import edu.uark.registerapp.models.api.Employee;
import edu.uark.registerapp.models.entities.EmployeeEntity;
import edu.uark.registerapp.models.repositories.EmployeeRepository;
import net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional;

@Service
public class EmployeeQuery implements ResultCommandInterface<Employee>{
    @Autowired
    private EmployeeRepository eRepo;

    public UUID employeeId;
    
    @Override
    public Employee execute() {
        UUID empID = this.employeeId;
        //this may change, had to add java.util to optional to work. remove if needed. leaving the import
        java.util.Optional<EmployeeEntity> empEntity = this.eRepo.findById(empID);

        if(empEntity.isPresent()){
            return new Employee(empEntity.get());
        }
        else{
            throw new NotFoundException("Employee");
        }

    }

    public UUID getEmpID(){
        return this.employeeId;
    }
    
    public EmployeeQuery setId(UUID empID){
        this.employeeId = empID;
        return this;
    }
}
