package in.sp.pro.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import in.sp.pro.controller.UserController;
import in.sp.pro.entities.Department;
import in.sp.pro.repository.DepartmentRepository;

@Service
public class DepartmentServices {

	@Autowired
    private final DepartmentRepository departmentRepository;
	
    public DepartmentServices(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }
	
    
    // ✅ FIXED METHOD NAME
    public List<Department> findAll() {
        return departmentRepository.findAllByOrderByIdAsc();
    }
    
    
	
	//Fetch all Departments
	public List<Department> getAllDepartment(){
		return  departmentRepository.findAllByOrderByIdAsc();
	}
	
	//save department
	public String saveDepartment(Department department) {
		 //departmentRepository.save(department);
		
		//insert case
		if(department.getId() == null) {
			boolean exists = departmentRepository.existsByNameIgnoreCase( department.getName());
			
			if(exists) {
				return " Department already exists";
			}
			
			
		}	else {
				//update case
				boolean exists = departmentRepository.existsByNameIgnoreCaseAndIdNot(department.getName(), department.getId());
				
				
				if(exists) {
					return "Department already exists";
				}
			}
			
			departmentRepository.save(department);
			
			return "Saved successfully";
		}
	
	
	
	
    // 👉 Get by ID
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    // 👉 Delete
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
	
}
