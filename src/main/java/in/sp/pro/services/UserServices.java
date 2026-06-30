package in.sp.pro.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.sp.pro.entities.AssetAllocation;
import in.sp.pro.entities.Role;
import in.sp.pro.entities.User;
import in.sp.pro.repository.AssetAllocationRepository;
import in.sp.pro.repository.RoleRepository;
import in.sp.pro.repository.UserRepository;
import in.sp.pro.repository.UserRepository;

@Service
public class UserServices {

	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepository roleRepository;
	
	
	@Autowired
	private AssetAllocationRepository assetAllocationRepository;
	
	public User registerUser(User user) {
		
		boolean adminExists = userRepository.existsByRole_RoleName("ROLE_ADMIN");

		if (adminExists) {
	        throw new RuntimeException("ROLE_ADMIN already exists");
	    }
		
		
	    user.setPassword(
	        passwordEncoder.encode(
	            user.getPassword()
	        )
	    );

	    Role role = roleRepository
	            .findByRoleName("ROLE_ADMIN")
	            .orElseThrow(() ->
	                new RuntimeException(
	                    "Role not found"));

	    user.setRole(role);

	    return userRepository.save(user);
	}
	
	
	//login validation process
	public User loginUser(String email, String password) {
		 User user = userRepository.findByWorkEmail(email);
		 
		 if(user != null && user.getPassword().equals(password)) {
			 return user; // valid
		 }
		 
		 return null; //invalid
	}
	
	//Data fatch process
	public List<User> getAllUsers()
	{
	    return userRepository.findAll();
	}
	
	
	 // 🔥 ADD THIS METHOD
    public User getLatestUser() {
        return userRepository.findTopByOrderByIdDesc();
    }


     
    public String saveUser(User user)
    {
    	
    	List<User> users = userRepository.findAll();
    	
        String roleName =
                user.getRole().getRoleName();

        // CHECK ADMIN
        if(roleName.equalsIgnoreCase("ROLE_ADMIN"))
        {
            boolean adminExists =
                    userRepository.existsByRole_RoleName("ROLE_ADMIN");

            if(adminExists)
            {
                return "ROLE_ADMIN already exists";
            }
        }

        // CHECK HR
        if(roleName.equalsIgnoreCase("ROLE_HR"))
        {
            boolean hrExists =
                    userRepository.existsByRole_RoleName("ROLE_HR");

            if(hrExists)
            {
                return "ROLE_HR already exists";
            }
        }

        // CHECK MANAGER
        if(roleName.equalsIgnoreCase("ROLE_MANAGER"))
        {
            boolean managerExists =
                    userRepository.existsByRole_RoleName("ROLE_MANAGER");

            if(managerExists)
            {
                return "ROLE_MANAGER already exists";
            }
        }

        userRepository.save(user);

        return "User Registered Successfully";
    }
    
    
 
    
    // NEW METHOD
    public User getUserByEmail(String email)
    {
        return userRepository.findByWorkEmail(email);
    }
    
    
    
    public List<User> getAllEmployees()
    {
        return userRepository.findByRole_RoleNameOrderByIdAsc("ROLE_EMPLOYEE");
    }
    
    /////////////////////////////////
    public User getLoggedInUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        return userRepository.findByWorkEmail(username);
    }
    
    
    
    
    ///////////////////
//    public List<User> getApprovedUsers() {
//        return userRepository.findByOnboardingStatus("STARTED");
//    }
    
    //System Admin Dashboard:
    public List<User> getApprovedUsers() {
        return userRepository.findByOnboardingStatusOrderByIdAsc("APPROVED");
    }
    
    
    
    
//    public List<User> getSystemAdminUsers() {
//        return userRepository.findByOnboardingStatus("APPROVED");
//    }
    
    //Manager Dashboard:
    public List<User> getManagerUsers() {
        return userRepository.findByOnboardingStatusOrderByIdAsc("STARTED");
    }
    
    public User findByWorkEmail(String workEmail) {
        return userRepository.findByWorkEmail(workEmail);
    }
    
    
    public AssetAllocation saveAsset(AssetAllocation asset) {
        return assetAllocationRepository.save(asset);
    }
    

}

