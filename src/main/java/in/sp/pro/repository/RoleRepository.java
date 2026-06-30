package in.sp.pro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.sp.pro.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	List<Role> findAllByOrderByIdAsc();
	
	
	  // Duplicate check for update
    boolean existsByRoleNameIgnoreCaseAndIdNot(String roleName, Long id);

    // Duplicate check for insert
    boolean existsByRoleNameIgnoreCase(String roleName);
    
//    Role findByRoleName(String roleName);
    Optional<Role> findByRoleName(String roleName);

}