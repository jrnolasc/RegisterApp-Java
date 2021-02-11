package edu.uark.registerapp.commands.employees.helpers;
import org.apache.commons.lang3.StringUtils;

import edu.uark.registerapp.models.repositories.EmployeeRepository;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.models.entities.EmployeeEntity;
import edu.uark.registerapp.models.api.Employee;

public class ActiveEmployeeExistsQuery {
    public Boolean EmployeeSearch() {       //my name, not the projects name. could change
        Employee emp; // how do i access the database info with the employee data? document?
        int found = 0;
        for(int i = 0; i < emp.length; i++) { // trying to have it cycle through the Employee list
            if(emp[i].getIsActive()){
                found++;
            }
        }        
        //need to use this somewhere EmployeeRepository.existsByIsActive();
        if(found > 0){
            return true;
        }
        else
            throw new NotFoundException("Employee"); //this string may change

    }
}
