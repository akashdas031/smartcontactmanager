package com.smartContactMnager.Entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name="User_Table")
public class user {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@NotBlank(message="Name should not blank")
	@Size(min=2,max=20,message="please enter atleast two character")
	private String name;
	@Column(unique = true)
	@Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message="please enter valid Email")
	private String email;
	@Pattern(regexp = "(?=^.{8,}$)(?=.*\\d)(?=.*[!@#$%^&*]+)(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$",message="please enter the password combination of character number and special characters")
	private String password;
	private String role;
	private String imagePath;
	@Column(length = 500)
	private String about;
	private boolean enabled;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "users")
	private List<contact> contact=new ArrayList<>();
	
	public user() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public List<contact> getContact() {
		return contact;
	}
	public void setContact(List<contact> contact) {
		this.contact = contact;
	}
	/*
	 * @Override public String toString() { return "user [id=" + id + ", name=" +
	 * name + ", email=" + email + ", password=" + password + ", role=" + role +
	 * ", imagePath=" + imagePath + ", about=" + about + ", enabled=" + enabled +
	 * ", contact=" + contact + "]"; }
	 */
	
	
	
}
