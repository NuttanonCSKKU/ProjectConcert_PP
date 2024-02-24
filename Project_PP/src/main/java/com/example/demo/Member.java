package com.example.demo;

import jakarta.persistence.*;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idmember;
    private String number;
    private String name;
    private String telephone;
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idaddress") // Specify the inverse side
    private Address address;

    public Member() {
    	this.idmember = 0;
		this.number = "";
		this.name = "";
		this.telephone = "";
		this.email = "";
    }

    public Member(int idmember, String number, String name, String telephone, String email, Address address) {
        this.idmember = idmember;
        this.number = number;
        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.address = address;
    }

	public int getIdmember() {
		return idmember;
	}

	public void setIdmember(int idmember) {
		this.idmember = idmember;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
