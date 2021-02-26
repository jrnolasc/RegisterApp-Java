package edu.uark.registerapp.commands.employees;

import edu.uark.registerapp.commands.ResultCommandInterface;
import edu.uark.registerapp.commands.exceptions.UnauthorizedException;
import edu.uark.registerapp.commands.exceptions.UnprocessableEntityException;
import edu.uark.registerapp.models.api.Employee;
import edu.uark.registerapp.models.api.EmployeeSignIn;
import edu.uark.registerapp.models.entities.ActiveUserEntity;
import edu.uark.registerapp.models.entities.EmployeeEntity;
import edu.uark.registerapp.models.repositories.ActiveUserRepository;
import edu.uark.registerapp.models.repositories.EmployeeRepository;

import java.util.Arrays;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeSignInCommand implements ResultCommandInterface<Employee> {
    @Override
    public Employee execute(){
        this.validateEmpObj();
        return new Employee(this.SignInEmp()); // add function for signing in employee        
    }
    //validate incoming Employee request object
        //Employee ID should not be blank and should be a number
        //Password should not be blank
    public void validateEmpObj(){ 
        String userId = this.empSignIn.getEmployeeId();// these may change based on what they are named
        String password = this.empSignIn.getPassword();// same here. comes from EmployeeSignIn.java
        if (userId.length() <= 0){
            throw new UnprocessableEntityException("ID");
        }
        if (password.length() <= 0){
            throw new UnprocessableEntityException("Password");
        }
    }
    //Query the employee by their employee ID
        //Use EmployeeRepository.queryByEmployeeId() method
        //verify the employee exists
        //verify the password from the request and the database match
            // use Array.equals()
    //Transaction - Use the @Transactional annotation
        //Query the activeuser for a record with the employee ID
        //If exists
            //update entitys sessionKey property with the current session key
            //use the existing ActiveUserRepository.save() 
        //Else
            //create new activeuser record in the database
                //set session key
                //set the necessary employee details
                //use the existing ActiveUserRepository.save()
    @Transactional
    private EmployeeEntity signInEmp() {
        final Optional<EmployeeEntity> empEntity = this.eRepo.findByEmployeeId(this.empSignIn.getEmployeeId());//again the .getEmployeeId may change
        if(!empEntity.isPresent()){
            throw new UnprocessableEntityException("Id not found");
        }
        //the .getpassword in empSignIn may change once i get the name for it
        if(!Arrays.equals(empEntity.get().getPassword(),this.empSignIn.getPassword())){
            throw new UnauthorizedException();
        }
        final Optional<ActiveUserEntity> actUser = this.actRepo.findByEmployeeId(empEntity.get().getId());
        //checking to see if user exists
        if(!actUser.isPresent()){
            //if not found
            ActiveUserEntity newUser = new ActiveUserEntity();
            newUser.setSessionKey(this.sessionId);
            newUser.setEmployeeId(empEntity.get().getId());
            newUser.setClassification(empEntity.get().getClassification());
            newUser.setName(empEntity.get().getFirstName().concat(" ").concat(empEntity.get().getLastName()));
            this.actRepo.save(newUser);
        }
        else{ 
            //if found
            this.actRepo.save(actUser.get().setSessionKey(this.sessionId))
        }

    
    }
    public String sessionId;
    public EmployeeSignIn empSignIn;// may look at this being private
    @Autowired
    private EmployeeRepository eRepo;
    @Autowired
    private ActiveUserRepository actRepo;
}
