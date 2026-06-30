package in.sp.pro.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "users")
//@SequenceGenerator(
//    name = "user_seq_generator",
//    sequenceName = "user_sequence",
//    allocationSize = 1
//)
public class User {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "user_seq_generator"
    )
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "dob")
    private LocalDate dateOfBirth;

    private String gender;

    @NotBlank
    @Email
    @Column(name = "work_email", unique = true, nullable = false)
    private String workEmail;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$")
    @Column(name = "phone_no", nullable = false)
    private String phoneNo;

    private String address;

    private String designation;

    private String location;
    
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    
  //  private String department;

    @Column(name = "created_by")
    private String createdBy = "Admin";

    @Column(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(name = "deactivated_by")
    private Integer deactivatedBy;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "image_status")
    private String imageStatus; // SUCCESS, FAILED, PENDING

    @NotBlank
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&]).+$",
        message = "Password must contain letter, number and special character"
    )
    @Size(min = 8, max = 100)
    @Column(length = 255, nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "onboarding_status")
    private String onboardingStatus;
    
    
    
    private String approvedBy;
    
    
    private String allocatedAssets;
    
    // =========================
    // GETTERS AND SETTERS
    // =========================

    
    
    
    public String getApprovedBy() {
		return approvedBy;
	}

	public String getAllocatedAssets() {
		return allocatedAssets;
	}

	public void setAllocatedAssets(String allocatedAssets) {
		this.allocatedAssets = allocatedAssets;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWorkEmail() {
        return workEmail;
    }

    public void setWorkEmail(String workEmail) {
        this.workEmail = workEmail;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getDeactivatedBy() {
        return deactivatedBy;
    }

    public void setDeactivatedBy(Integer deactivatedBy) {
        this.deactivatedBy = deactivatedBy;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(String imageStatus) {
        this.imageStatus = imageStatus;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

	public String getOnboardingStatus() {
		return onboardingStatus;
	}

	public void setOnboardingStatus(String onboardingStatus) {
		this.onboardingStatus = onboardingStatus;
	}
    
    
    
    
}