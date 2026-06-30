package in.sp.pro.DTO;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterDTO {

	
	@NotBlank(message="First Name is required")
    @Pattern(regexp="^[A-Za-z]+$",message="Only alphabets allowed")
	@Size(min = 2, message = "Name must be at least 2 characters")
	private String firstName;

	@Pattern(regexp="[A-Za-z]+$",message="Only alphabets allowed")
	@NotBlank(message="Last Name is required")
	@Size(min = 2, message = "Name must be at least 2 characters")
	private String lastName;

	@Email(message = "Invalid Email Format")
    @NotBlank(message = "Email is required")
	private String workEmail;

	@NotBlank(message = "Password is required")
    @Pattern(regexp="^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&]).{8,14}$",
    message="Password must contain alphabet, number & special character (8-14 chars)")
    @Size(min = 8, message = "Password must be at least 8 characters")
	private String password;

	@Pattern(regexp="^[6-9]\\d{9}$", message="Invalid phone number")
	@NotBlank(message = "Phone Number is required")
	private String phoneNo;

	@NotBlank(message = "Address is required")
	private String address;

	@NotNull(message = "Date of Birth is required")
	private LocalDate dateOfBirth;

	@NotBlank(message = "Gender is required")
	private String gender;

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

	public String getWorkEmail() {
		return workEmail;
	}

	public void setWorkEmail(String workEmail) {
		this.workEmail = workEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
}
