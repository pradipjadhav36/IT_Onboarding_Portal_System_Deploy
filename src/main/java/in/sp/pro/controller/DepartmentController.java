package in.sp.pro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import in.sp.pro.entities.Department;
import in.sp.pro.services.DepartmentServices;
import in.sp.pro.services.UserServices;

@Controller
@RequestMapping("/department")
public class DepartmentController {
	
    @Autowired
    private DepartmentServices departmentServices;
    
    @Autowired
    private UserServices userServices;
    
   
    //Department panel open
    @GetMapping("/department_panel")
    public String openDeptPanel(Model model) {

        // Dept fetch
        model.addAttribute("departmentList", departmentServices.getAllDepartment());

        // User fetch
        model.addAttribute("userList", userServices.getAllUsers());

        return "department_panel"; // NO redirect
    }
    
    
    
    // Save Department  + Assign to user
    @PostMapping("/saveDepartment")
    public String saveDepartment(@ModelAttribute Department dept, Model model) {
    			//departmentServices.saveDepartment(dept);
    			//return "redirect:/department/department_panel";
    	
    	
    			String message = departmentServices.saveDepartment(dept);
    			
    			if(message.equals("Department already exists")) {
    				
    				model.addAttribute("error", message);
    				model.addAttribute("department", dept);
    				
    				
    				return "department-form";
    			}
    			
    			return "redirect:/department/department_panel";
    }
    
    
    
    //Add Form
    @GetMapping("/new")
    public String showForm(Model model) {
    		model.addAttribute("department", new Department());
    		return "department-form";
    }
    
    
    
    //Edit
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
    		Department dept = departmentServices.getDepartmentById(id);
    		model.addAttribute("department", dept);
    		return "department-form";
    		
    }
    
    
    //Delete
    @GetMapping("/deleteDepartment/{id}")
    public String delete(@PathVariable Long id) {
    		departmentServices.deleteDepartment(id);
    		return "redirect:/department/department_panel";
    }
}
