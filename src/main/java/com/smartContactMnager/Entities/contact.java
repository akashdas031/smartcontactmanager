package com.smartContactMnager.Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="ContactDetails")
public class contact {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int cid;
	@NotBlank(message="Name must contains some character")
	@Size(min=3,max=20,message="Minimum 3 characters allowed")
	private String cname;
	@NotBlank(message="This Field must contains some character")
	@Size(min=3,max=20,message="Minimum 3 characters allowed")
	private String secondName;
	private String work;
	@Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message="Enter valid email")
	private String cemail;
	@Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$" ,message="Please Enter valid Contact Number")
	private String phone;
	
	private String cImagePath;
	@Column(length=50000)
	private String description;
	
	@ManyToOne
	@JsonIgnore
	private user users;
	
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getCemail() {
		return cemail;
	}
	public void setCemail(String cemail) {
		this.cemail = cemail;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getcImagePath() {
		return cImagePath;
	}
	public void setcImagePath(String cImagePath) {
		this.cImagePath = cImagePath;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public user getUsers() {
		return users;
	}
	public void setUsers(user users) {
		this.users = users;
	}
	@Override
	public String toString() {
		return "contact [cid=" + cid + ", cname=" + cname + ", secondName=" + secondName + ", work=" + work
				+ ", cemail=" + cemail + ", phone=" + phone + ", cImagePath=" + cImagePath + ", description="
				+ description + ", users=" + users + "]";
	}
	
	
}
