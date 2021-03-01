package edu.uark.registerapp.commands.activeUsers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.uark.registerapp.commands.VoidCommandInterface;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.models.entities.ActiveUserEntity;
import edu.uark.registerapp.models.repositories.ActiveUserRepository;

@Service
public class ActiveUserDeleteCommand implements VoidCommandInterface {  
    @Autowired
    private ActiveUserRepository aR;
    private String sKey; //session key
    //Properties
    //Current session key    
    //public String getSesKey(){
    //    return this.sKey;
    //}

    public String getSessionKey() {
		return this.sKey;
	}

	public ActiveUserDeleteCommand setSessionKey(final String sKey) {
    	this.sKey = sKey;
    	return this;
    }
    //ValidateActiveUserCommand has set session key, so I'm adding it here just in case
    public ActiveUserDeleteCommand setSesKey(String sKey){
        this.sKey = sKey;
        return this;
    }
    //In transaction use @Transactional here
        //Query the activeuser table for a record with provided session key
            //use the existing ActiveUserRepository.findBySessionKey()
    //Validate the incoming Employee request object     
    @Transactional
    @Override
    public void execute(){
        Optional<ActiveUserEntity> activeUser = this.aR.findBySessionKey(this.sKey);
        boolean statusCheck = activeUser.isPresent();
        // thes isPresent() is associated with Optional<>
        //So that is able to check is there is anyting in the Optional instance.

        //Delete the activeuser record
            //use the existing ActiveUserRepository.delete(
        if(statusCheck){
            this.aR.delete(activeUser.get());    //should delete the user        
        } 
        else{
            throw new NotFoundException("Employee");
        }
    }
  

    

}
