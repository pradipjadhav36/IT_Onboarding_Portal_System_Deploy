package in.sp.pro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.sp.pro.entities.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
	
	List<Department> findAllByOrderByIdAsc();
	
	
	boolean existsByNameIgnoreCase(String name);

	boolean existsByNameIgnoreCaseAndIdNot(
	        String name,
	        Long id);

}
