package in.sp.pro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.sp.pro.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	 
	User findByWorkEmail(String workEmail);
	 
	 User findTopByOrderByIdDesc(); // 🔥 IMPORTANT
	 
	 boolean existsByWorkEmail(String workEmail);
	 
	 long countByRoleId(Long roleId);
	 
	 
	 List<User> findAllByOrderByIdAsc();
	 
	 
	 boolean existsByRole_RoleName(String roleName);

	 User findByRoleRoleName(String roleName);
	 
	 
	 boolean existsByPhoneNo(String phoneNo);

	 boolean existsByPassword(String password);
	 
	 
	 boolean existsById(Long id);
	 
	 boolean existsByRole(String role);
	 
	 //List<User> findByRole_RoleName(String roleName);
	 
	 
	 boolean existsByIdAndOnboardingStatus(Long id, String onboardingStatus);
	 
	 //List<User> findByOnboardingStatus(String onboardingStatus);
	 List<User> findByOnboardingStatusOrderByIdAsc(String onboardingStatus);
	 
	 boolean existsByRole_RoleNameAndIdNot(
		        String roleName,
		        Long id);
	 
	 User findByFirstNameAndLastNameAndPhoneNo(
		        String firstName,
		        String lastName,
		        String phoneNo);
	
	 

	   // List<User> findByRole_RoleName(String roleName);
	 
	 List<User> findByRole_RoleNameOrderByIdAsc(String roleName);
	    
	 
	    User findByFirstNameAndLastName(
	            String firstName,
	            String lastName);
	   
}
