package in.sp.pro.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.sp.pro.entities.Role;
import in.sp.pro.repository.RoleRepository;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // 👉 Fetch all roles
    public List<Role> getAllRoles() {
        return roleRepository.findAllByOrderByIdAsc();
    }

    // 👉 Save role
    public String saveRole(Role role) {
       // return roleRepository.save(role);
    	
    	
    		//insert case
    	if(role.getId() == null) {
    		
    		boolean exists = roleRepository.existsByRoleNameIgnoreCase(role.getRoleName());
    		
    		if(exists) {
    			return "Role already exists";
    		}
    	   }else {
    		   //update case
    		   
    		   boolean exists = roleRepository.existsByRoleNameIgnoreCaseAndIdNot(role.getRoleName(), role.getId());
    		   
    		   
    		   if(exists) {
    			   return "Role already exists";
    		   }
    	   }
    	
    			roleRepository.save(role);
    			
    			return "Saved Sucessfully";
    }

    // 👉 Get by id
    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
    }

    // 👉 Delete role
    public void deleteRole(Long id) {
        if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id);
        }
    }
}