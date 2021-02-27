package edu.uark.registerapp.commands.employees;

import edu.uark.registerapp.commands.ResultCommandInterface;
import edu.uark.registerapp.commands.employees.helpers.EmployeeHelper;
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
        return new Employee(this.signInEmp()); // add function for signing in employee        
    }
    //validate incoming Employee request object
        //Employee ID should not be blank and should be a number
        //Password should not be blank
    public void validateEmpObj(){ 
        String userId = this.empSignIn.employeeId;
        String password = this.empSignIn.password;
        if (userId.length() <= 0){
            throw new UnprocessableEntityException("ID");
        }
        if (password.length() <= 0){
            throw new UnprocessableEntityException("Password");
        }
    }

    @Transactional
    private EmployeeEntity signInEmp() {
        int tempId = Integer.parseInt(this.empSignIn.employeeId); // turns string into int
        final Optional<EmployeeEntity> empEntity = this.eRepo.findByEmployeeId(tempId);
        if(!empEntity.isPresent()){
            throw new UnprocessableEntityException("Id not found");
        }        
        byte empSignInPassword[] = EmployeeHelper.hashPassword(this.empSignIn.password);
        if(!Arrays.equals(empEntity.get().getPassword(),empSignInPassword)) {
            throw new UnauthorizedException();
        }
        final Optional<ActiveUserEntity> actUser = this.actRepo.findByEmployeeId(empEntity.get().getId());
        //checking to see if user exists
        if(!actUser.isPresent()){
            //if not found, make new active user
            ActiveUserEntity newUser = new ActiveUserEntity();
            newUser.setSessionKey(this.sessionId);
            newUser.setEmployeeId(empEntity.get().getId());
            newUser.setClassification(empEntity.get().getClassification());
            newUser.setName(empEntity.get().getFirstName().concat(" ").concat(empEntity.get().getLastName()));
            this.actRepo.save(newUser);
        }
        else{ 
            //if found, update session key
            this.actRepo.save(actUser.get().setSessionKey(this.sessionId));
        }

    return empEntity.get();
    }
    public String sessionId;
    public EmployeeSignIn empSignIn;// may look at this being private
    @Autowired
    private EmployeeRepository eRepo;
    @Autowired
    private ActiveUserRepository actRepo;
}
