package edu.uark.registerapp.commands.employees;
import edu.uark.registerapp.models.repositories.EmployeeRepository;
import edu.uark.registerapp.commands.VoidCommandInterface;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service //brings in use of the Service packet
public class ActiveEmployeeExistsQuery implements VoidCommandInterface{
    @Autowired //This eliminates the need for getters and setters.
    private EmployeeRepository eR;

    @Override // override, indicates the subclass is replacing inherited behavior. 
    public void execute() {       //execute is from the voidCommandInterface, called when other things like it are called?  
        boolean empFound = this.eR.existsByIsActive(true);
        if(!empFound){
            throw new NotFoundException("Employee"); //this string may change             
        } 
    } 
}
