package in.sp.pro.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		 return new BCryptPasswordEncoder();
	}
	
	
	 @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http)
	            throws Exception {

	        http
	            .csrf(csrf -> csrf.disable())

	            .authorizeHttpRequests(auth -> auth
	                .requestMatchers(
	                    "/user/register",
	                    "/user/login",
	                    "/user/dashboard",
	                    "/user/logout",
	                   
	                    "/role/role_panel",
	                    "/role/editRole/{id}",
	                    "/role/saveRole",
	                    "/role/deleteRole/{id}",
	                    
	                    "/department/department_panel",
	                    "/department/edit/{id}",
	                    "/department/saveDepartment",
	                    "/department/deleteDepartment/{id}",
	                    
	                    "/user/user_panel",
	                    "/user/update/**",
	                    "/user/updateSave",
	                    "/user/deleteUser/**",
	                    "/css/**",
	                    "/js/**"
	                ).permitAll()
	                
	                
	        
	                // USER PANEL (ONLY IF LOGGED IN OR TEST MODE)
	                .requestMatchers("/user/user_panel").permitAll()

	             // HR DASHBOARD
	                .requestMatchers("/user/hr/**")
	               // .permitAll()
	                .hasRole("HR")
	                
	                
	             // ADMIN DASHBOARD
	                .requestMatchers("/admin/**")
	                .hasRole("ADMIN")
	                
	                
	             // Manager DASHBOARD
	                .requestMatchers("/user/manager/**")
	                .hasRole("MANAGER")
	                
	                
	             // System Admin DASHBOARD
	             // System Admin DASHBOARD
	                .requestMatchers("/user/system-admin/**")
	                .hasAuthority("ROLE_SYSTEM_ADMIN")
	                
	                
	                // ANY REQUEST
	                .anyRequest()
	                .authenticated()
	                
	      
	            )
	        
	        
	            .formLogin(form -> form
	            	    .loginPage("/user/login")
	            	    .usernameParameter("workEmail")

	            	    .successHandler((request, response, authentication) -> {

	            	        String role = authentication.getAuthorities()
	            	                .iterator()
	            	                .next()
	            	                .getAuthority();
	            	        
	            	        System.out.println("Role = " + role); // 👈 Yaha add karo
	            	        

	            	        if(role.equals("ROLE_HR"))
	            	        {
	            	            response.sendRedirect("/user/hr/dashboard");
	            	        }

	            	        else if(role.equals("ROLE_ADMIN"))
	            	        {
	            	            response.sendRedirect("/user/dashboard");
	            	        }

	            	        else if(role.equals("ROLE_MANAGER"))
	            	        {
	            	            response.sendRedirect("/user/manager/dashboard");
	            	        }
	            	        
	            	        else if(role.equals("ROLE_SYSTEM_ADMIN"))
	            	        {
	            	            response.sendRedirect("/user/system-admin/dashboard");
	            	        }
	            	        
	            	        else
	            	        {
	            	            response.sendRedirect("/user/dashboard");
	            	        }
	            	        
	            	    })

	            	    .permitAll()
	            	);
	        

	        return http.build();
	    }	
}
