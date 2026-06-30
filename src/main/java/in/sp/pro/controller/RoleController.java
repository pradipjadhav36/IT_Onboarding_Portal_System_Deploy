package in.sp.pro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import in.sp.pro.entities.Role;
import in.sp.pro.services.RoleService;
import in.sp.pro.services.UserServices;

@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserServices userServices;

    // 👉 Role Panel Open
    @GetMapping("/role_panel")
    public String openRolePanel(Model model) {
        model.addAttribute("roleList", roleService.getAllRoles());
        model.addAttribute("userList", userServices.getAllUsers());
        return "role_panel";
    }


    // 👉 Add new form
    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("role", new Role());
        return "role-form"; // ✔ match with file name
    }

    
    // 👉 Edit (Update)
    @GetMapping("/editRole/{id}")
    public String editRole(@PathVariable Long id, Model model) {
        Role role = roleService.getRoleById(id);
        model.addAttribute("role", role);
        return "role-form"; // ✔ same form reuse
    }
    
    
    // 👉 Save (Create + Update)
    @PostMapping("/saveRole")
    public String saveRoleForm(@ModelAttribute Role role, Model model) {
        //roleService.saveRole(role);
        //return "redirect:/role/role_panel";
    	
    		String message = roleService.saveRole(role);
    		
    		if(message.equals("Role already exists")) {
    			 model.addAttribute("error", message);
    			 model.addAttribute("role", role);
    			 
    			 return "role-form";
    		}
    		
    		return "redirect:/role/role_panel";
    }

    
    
    // 👉 READ (LIST)
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("roles", roleService.getAllRoles()); // ✅ fixed
        return "role-list";
    }

    // 👉 EDIT
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
    	    Role role = roleService.getRoleById(id);
    	    model.addAttribute("role", role);   
        return "role-form";
    }

    // 👉 DELETE
    @GetMapping("/deleteRole/{id}")
    public String delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return "redirect:/role/role_panel";
    }
}