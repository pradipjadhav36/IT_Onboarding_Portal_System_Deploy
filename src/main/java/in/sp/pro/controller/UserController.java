package in.sp.pro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.sp.pro.DTO.RegisterDTO;
import in.sp.pro.entities.AssetAllocation;
//import in.sp.pro.entities.AssetAllocation;
import in.sp.pro.entities.Department;
import in.sp.pro.entities.Role;
import in.sp.pro.entities.User;
import in.sp.pro.repository.AssetAllocationRepository;
import in.sp.pro.repository.DepartmentRepository;
import in.sp.pro.repository.RoleRepository;
import in.sp.pro.repository.UserRepository;
import in.sp.pro.services.DepartmentServices;
import in.sp.pro.services.RoleService;
import in.sp.pro.services.UserServices;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import org.springframework.ui.Model;

import org.springframework.security.core.Authentication;


@Controller
@RequestMapping("/user")
public class UserController {

    private final AssetAllocationRepository assetAllocationRepository;

    private final RoleController roleController;

    private final UserServices userServices;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    // ===== Constructor Injection =====
    public UserController(
            UserServices userServices,
            UserRepository userRepository,
            RoleService roleService,
            PasswordEncoder passwordEncoder, RoleController roleController, AssetAllocationRepository assetAllocationRepository) {

        this.userServices = userServices;
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.roleController = roleController;
        this.assetAllocationRepository = assetAllocationRepository;
    }
    
    
    @Autowired
    private RoleRepository roleRepository;
    
    
    @Autowired
    private DepartmentServices departmentServices;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
   
    
    
 // =========================================================
    // REGISTER PAGE
    // =========================================================
    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("user", new RegisterDTO());
        return "register";
    }
    
    
    
    
    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("user")
            RegisterDTO registerDTO,
            BindingResult result,
            Model model) {

        if(result.hasErrors()) {

            model.addAttribute(
                    "msg",
                    "Validation Failed");

            return "register";
        }

        try {

            User users = new User();

            users.setFirstName(
                    registerDTO.getFirstName());

            users.setLastName(
                    registerDTO.getLastName());

            users.setWorkEmail(
                    registerDTO.getWorkEmail());

            users.setPassword(
                    registerDTO.getPassword());

            users.setPhoneNo(
                    registerDTO.getPhoneNo());

            users.setAddress(
                    registerDTO.getAddress());
            

            users.setDateOfBirth(
                    registerDTO.getDateOfBirth());

            users.setGender(
                    registerDTO.getGender());

            userServices.registerUser(users);

            model.addAttribute(
                    "success",
                    "Registration Successful");

            return "login";

        } catch (Exception e) {

            e.printStackTrace();

            model.addAttribute(
                "msg",
                e.getMessage());

            return "register";
        }
    }
    
    
    
    // =========================================================
    // LOGIN PAGE
    // =========================================================

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }
    
     
    @PostMapping("/login")
    public String loginUser(@RequestParam("workEmail") String workEmail,
                            @RequestParam String password,
                            Model model) {

        User user = userRepository.findByWorkEmail(workEmail.trim());

        // USER NOT FOUND
        if (user == null) {

            model.addAttribute("error", "User not found");

            return "login";
        }

        // PASSWORD CHECK
        if (!passwordEncoder.matches(password, user.getPassword())) {

            model.addAttribute("error", "Invalid Password");

            return "login";
        }

       
        String role = user.getRole().getRoleName();

     
        if (role.equalsIgnoreCase("ROLE_ADMIN")) {
            return "redirect:/admin/dashboard";
        }

        else if (role.equalsIgnoreCase("ROLE_HR")) {
        	return "redirect:/user/hr/dashboard";
        }

        else if (role.equalsIgnoreCase("ROLE_MANAGER")) {
            return "redirect:/user/manager/dashboard";
        }

        return "redirect:/user/dashboard";
    }
    
    
    
    // =========================================================
    // DASHBOARD
    // =========================================================

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "dashboard";
    }
    
    
    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }
    
    
    
    
    @GetMapping("/hr/dashboard")
    public String hrDashboard(Model model,
                              Authentication authentication)
    {
    	
    	
    		List<Role> roles = roleRepository.findAll();
    	    List<Department> departments = departmentServices.findAll();

    	    model.addAttribute("roles", roles);
    	    model.addAttribute("departments", departments);
    	    
    	    

        // Agar login nahi hai
        if(authentication == null)
        {
            return "redirect:/user/login";
        }

        // Logged in email
        String email = authentication.getName();

        // Database se user fetch karo
        User user = userServices.getUserByEmail(email);

        // Logged in user model me bhejo
        model.addAttribute("loggedInUser", user);

        // Sirf employee role wale users
        List<User> employeeList =
                userServices.getAllEmployees();

        
        System.out.println(employeeList);
        model.addAttribute("employees", employeeList);
       
        return "hr_dashboard";
    }
    
    
    
    /////////////////////////////////////////////////////////
    @PostMapping("/hr/save-onboarding")
    public String saveOnboarding(

            @RequestParam Long employeeId,
            @RequestParam String fullName,
            @RequestParam String workEmail,
            @RequestParam String phoneNo,
            
            @RequestParam Long roleId,
            @RequestParam Long departmentId,
            @RequestParam String designation,
            @RequestParam String address,
            @RequestParam String joiningDate,
            RedirectAttributes redirectAttributes) {

        try {

            User user = userRepository.findById(employeeId).orElse(null);

            if (user == null) {

                redirectAttributes.addFlashAttribute(
                        "error",
                        "Employee not found");

                return "redirect:/user/hr/dashboard";
            }

            // full name split
            String[] nameParts = fullName.split(" ", 2);

            user.setFirstName(nameParts[0]);

            if (nameParts.length > 1) {
                user.setLastName(nameParts[1]);
            }

            user.setWorkEmail(workEmail);
            user.setPhoneNo(phoneNo);

            // password encode
           

            user.setDesignation(designation);

            user.setLocation(address);

            user.setJoiningDate(
                    java.time.LocalDate.parse(joiningDate));

            // role
            Role role = roleRepository.findById(roleId).orElse(null);

            
            user.setRole(role);
           
            
            // department
            Department department =
                    departmentRepository.findById(departmentId).orElse(null);

          
            user.setDepartment(department);
        

            
            
            // DEBUG
            System.out.println(user.getDepartment().getId());
            System.out.println(user.getLocation());
            
            
            
            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();

            String loggedInEmail =
                    authentication.getName();

            User loggedInUser =
                    userRepository.findByWorkEmail(loggedInEmail);

            user.setCreatedBy(
                    loggedInUser.getRole().getRoleName());
            
            
            
         // 👇 YAHI ADD KARO
            user.setOnboardingStatus("STARTED");
            
            userRepository.save(user);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Onboarding Updated Successfully");

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Failed To Update Onboarding");
        }

        return "redirect:/user/hr/dashboard";
    }
    
    

    @GetMapping("/start-onboarding/{id}")
    public String startOnboarding(@PathVariable Long id,
                                  RedirectAttributes redirectAttributes) {

        User user = userRepository.findById(id).orElse(null);

        // user not found
        if (user == null) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "User not found");

            return "redirect:/user/hr/dashboard";
        }

        // onboarding already started
        if (user.getOnboardingStatus() != null &&
            user.getOnboardingStatus().equalsIgnoreCase("STARTED")) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Onboarding already started for this user");

            return "redirect:/user/hr/dashboard";
        }

        // first time onboarding
     //   user.setOnboardingStatus("STARTED");

        //user.setOnboardingStatus("APPROVED");
        
        user.setOnboardingStatus("STARTED");
        userRepository.save(user);

        redirectAttributes.addFlashAttribute(
                "success",
                "Onboarding started successfully");

        return "redirect:/user/hr/dashboard";
    }

    
    
    
    
    /////////////////////////////////////////////
//    @GetMapping("/manager/dashboard")
//    public String dashboard(Model model,
//                            Authentication authentication) {
//
//        String email = authentication.getName();
//
//        User user = userServices.getUserByEmail(email);
//
//        model.addAttribute("loggedInUser", user);
//
//        return "manager_dashboard";
//    }
    
    
    // =========================================================
    // Manager
    // =========================================================
    @GetMapping("/manager/dashboard")
    public String dashboard(Model model, Authentication authentication) {

        if(authentication == null){
            return "redirect:/user/login";
        }

        String email = authentication.getName();

        User loggedInUser = userServices.getUserByEmail(email);

        if(loggedInUser == null){
            return "redirect:/user/login";
        }

        model.addAttribute("loggedInUser", loggedInUser);

        // Approved Users Fetch
        List<User> users =
                userServices.getManagerUsers();

        model.addAttribute("users", users);
        
        //System.out.println("Count = " + approvedUsers.size());



        //model.addAttribute("users", approvedUsers);

        return "manager_dashboard";
    }
    
    
    
    
    
    
    /////////////////////////
    @GetMapping("/manager/approved-requests")
    public String approvedUsers(Model model) {

    	List<User> users = userRepository.findByOnboardingStatusOrderByIdAsc("APPROVED");
    	model.addAttribute("users", users);

        return "manager_approved_requests";
    }
    
    
    
    @PostMapping("/manager/approve/{id}")
    public String approveEmployee(@PathVariable Long id)
    {
        User user = userRepository.findById(id).orElseThrow();

        user.setOnboardingStatus("APPROVED");
        user.setApprovedBy("MANAGER");
        userRepository.save(user);

        return "redirect:/user/manager/dashboard";
    }
    
    
    
    
    @PostMapping("/manager/reject/{id}")
    public String rejectEmployee(@PathVariable Long id)
    {
        User user = userRepository.findById(id).orElseThrow();

        user.setOnboardingStatus("RETURN_TO_HR");

        userRepository.save(user);

        return "redirect:/user/manager/dashboard";
    }
    
    
    
    
    // =========================================================
    // System_Admin
    // =========================================================
//    @GetMapping("/system-admin/dashboard")
//    public String systemAdminDashboard(Model model,
//                                       Authentication authentication) {
//
//        String email = authentication.getName();
//
//        System.out.println("Email = " + email);
//
//        User user = userRepository.findByWorkEmail(email);
//
//        System.out.println("User = " + user);
//        
//        // Approved Employees List
//        List<User> approvedEmployees = userServices.getApprovedUsers();
//
//        System.out.println("Approved Employees Count = "
//                + approvedEmployees.size());
//        
//        model.addAttribute("approvedEmployees", approvedEmployees);
//        
//        model.addAttribute("loggedInUser", user);
//
//        return "system_admin_dashboard";
//    }
    
    
    
    
    
    @GetMapping("/system-admin/dashboard")
    public String systemAdminDashboard(Model model,
                                       Authentication authentication) {

    	List<User> approvedEmployees =
    	        userServices.getApprovedUsers();

    	model.addAttribute("approvedEmployees", approvedEmployees);

        System.out.println("Approved Count = "
                + approvedEmployees.size());

        approvedEmployees.forEach(u ->
            System.out.println(
                u.getFirstName() + " -> " +
                u.getOnboardingStatus()
            )
        );

        model.addAttribute("approvedEmployees", approvedEmployees);

        return "system_admin_dashboard";
    }
    
    
    
    
    
    
    
    
    
    
    @GetMapping("/approved-employees")
    public String approvedEmployees(Model model) {

        List<User> approvedEmployees =
                userServices.getApprovedUsers();

        model.addAttribute("approvedEmployees", approvedEmployees);

        return "approved_employee_list";
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
   
    
    // =========================================================
    // USER PANEL PAGE
    // =========================================================

    @GetMapping("/user_panel")
    public String openUserPanel(Model model)
    {
    	
    	List<User> users = userRepository.findAllByOrderByIdAsc();
    	model.addAttribute("users", users);
    	 
        model.addAttribute("user", new User());

        model.addAttribute("roles",
                roleService.getAllRoles());
        
        
        model.addAttribute("departments",
                departmentServices.getAllDepartment());

        
        model.addAttribute("users",
                userServices.getAllUsers());

        return "user_panel";
    }


    
    @PostMapping("/user_panel")
    public String saveUser(@ModelAttribute User user,
                           Model model)
    {

        // ===== ROLE FETCH =====

        Role role = roleService.getRoleById(
                user.getRole().getId());

        user.setRole(role);
        
        // DEPARTMENT FETCH
        Department department =
                departmentServices.getDepartmentById(
                        user.getDepartment().getId());

        user.setDepartment(department);
        
        
        String roleName = role.getRoleName();

        // =====================================
        // ONLY ONE HR / ADMIN / MANAGER
        // =====================================

        if(roleName.equalsIgnoreCase("ROLE_HR")
                || roleName.equalsIgnoreCase("ROLE_ADMIN")
                || roleName.equalsIgnoreCase("ROLE_MANAGER"))
        {

            boolean alreadyExists =
                    userRepository.existsByRole_RoleName(roleName);

            if(alreadyExists)
            {

                model.addAttribute("msg",
                        roleName + " already exists");

                model.addAttribute("users",
                        userRepository.findAllByOrderByIdAsc());

                model.addAttribute("roles",
                        roleService.getAllRoles());

                model.addAttribute("user",
                        new User());

                return "user_panel";
            }
        }

        
       
        // =====================================
        // EMAIL CHECK
        // =====================================

        boolean emailExists =
                userRepository.existsByWorkEmail(
                        user.getWorkEmail());

        if(emailExists)
        {

            model.addAttribute("msg",
                    "Email already exists");

            model.addAttribute("users",
                    userRepository.findAllByOrderByIdAsc());

            model.addAttribute("roles",
                    roleService.getAllRoles());

            model.addAttribute("user",
                    new User());

            return "user_panel";
        }
        
  
        // =====================================
        // PHONE CHECK
        // =====================================

        boolean phoneExists =
                userRepository.existsByPhoneNo(
                        user.getPhoneNo());

        if(phoneExists)
        {

            model.addAttribute("msg",
                    "Phone Number already exists");

            model.addAttribute("users",
                    userRepository.findAllByOrderByIdAsc());

            model.addAttribute("roles",
                    roleService.getAllRoles());

            model.addAttribute("user",
                    new User());

            return "user_panel";
        }
        

        // =====================================
        // PASSWORD CHECK
        // =====================================

        boolean passwordExists =
                userRepository.existsByPassword(
                        user.getPassword());

        if(passwordExists)
        {

            model.addAttribute("msg",
                    "Password already exists");

            model.addAttribute("users",
                    userRepository.findAllByOrderByIdAsc());

            model.addAttribute("roles",
                    roleService.getAllRoles());

            model.addAttribute("user",
                    new User());

            return "user_panel";
        }

        
        // =====================================
        // PASSWORD ENCODE
        // =====================================

        user.setPassword(
                passwordEncoder.encode(
                        user.getPassword()));

        
        // =====================================
        // SAVE USER
        // =====================================

        userRepository.save(user);

        // SUCCESS MESSAGE

        model.addAttribute("msg",
                "User Registered Successfully");

        model.addAttribute("users",
                userRepository.findAllByOrderByIdAsc());

        model.addAttribute("roles",
                roleService.getAllRoles());

        model.addAttribute("user",
                new User());

        return "user_panel";
    }
    


    // =========================================================
    // UPDATE
    // =========================================================
    		
    @GetMapping("/update/{id}")
    public String updateUser(@PathVariable Long id, Model model)
    {
//    	   System.out.println("Update Method Called");
    	   
        User user = userRepository.findById(id).orElse(null);

        model.addAttribute("user", user);
     
        // ADD THIS
        model.addAttribute("roles",
                roleService.getAllRoles());
        
        
        model.addAttribute("departments",
                departmentServices.getAllDepartment());


        return "update_user";
    }
    

    
    @PostMapping("/updateSave")
    public String updateSave(@ModelAttribute User user,
                             Model model)
    {

        // FETCH OLD USER
        User existingUser =
                userRepository.findById(user.getId())
                .orElse(null);

        // ROLE FETCH
        Role role =
                roleService.getRoleById(
                        user.getRole().getId());

        user.setRole(role);

        // DEPARTMENT FETCH
        Department department =
                departmentServices.getDepartmentById(
                        user.getDepartment().getId());

        user.setDepartment(department);

        String roleName = role.getRoleName();

        // =====================================
        // ONLY ONE HR / ADMIN / MANAGER
        // =====================================

        if(roleName.equalsIgnoreCase("ROLE_HR")
                || roleName.equalsIgnoreCase("ROLE_ADMIN")
                || roleName.equalsIgnoreCase("ROLE_MANAGER"))
        {

            boolean alreadyExists =
                    userRepository
                    .existsByRole_RoleNameAndIdNot(
                            roleName,
                            user.getId());

            if(alreadyExists)
            {

                model.addAttribute("error",
                        roleName + " already exists");

                model.addAttribute("roles",
                        roleService.getAllRoles());

                model.addAttribute("departments",
                        departmentServices.getAllDepartment());

                model.addAttribute("user", user);

                return "update_user";
            }
        }

        // SAVE UPDATED USER
        userRepository.save(user);

        model.addAttribute("success",
                "User Updated Successfully");

        model.addAttribute("roles",
                roleService.getAllRoles());

        model.addAttribute("departments",
                departmentServices.getAllDepartment());

        model.addAttribute("user", user);

        return "update_user";
    }
    
    
    // =========================================================
    // D	ELETE USER ID
    // =========================================================
    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id)
    {
        System.out.println("DELETE ID = " + id);

        userRepository.deleteById(id);

        return "redirect:/user/user_panel";
    }

    
    
    
    @GetMapping("/assets/add")
    public String assetForm() {
        return "asset-form";
    }
    
    
    

    
    
    

    @PostMapping("/save")
    public String saveAsset(
            @RequestParam String employeeName,
            @RequestParam(required = false) String laptop,
            @RequestParam(required = false) String mobile,
            @RequestParam(required = false) String mouse,
            RedirectAttributes redirectAttributes) {

        AssetAllocation asset = new AssetAllocation();

        asset.setEmployeeName(employeeName);
        asset.setLaptop(laptop != null);
        asset.setMobile(mobile != null);
        asset.setMouse(mouse != null);

        assetAllocationRepository.save(asset);

        // User table update
        User user = userRepository.findAll()
                .stream()
                .filter(u -> (u.getFirstName() + " " + u.getLastName())
                        .equals(employeeName))
                .findFirst()
                .orElse(null);

        if (user != null) {

            String assets = "";

            if (laptop != null)
                assets += "Laptop ";

            if (mobile != null)
                assets += "Mobile ";

            if (mouse != null)
                assets += "Mouse ";

            user.setAllocatedAssets(assets.trim());

            // 🔥 Onboarding Complete
            user.setOnboardingStatus("COMPLETED");

            userRepository.save(user);
        }

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Asset Allocated Successfully");

        return "redirect:/user/system-admin/dashboard";
    }
  
    // =========================================================
    // LOGOUT
    // =========================================================

    @GetMapping("/logout")
    public String logout() {

        return "redirect:/user/login";
    }
}