package com.realtimebidding.dtos;

public class EmailRequestDto implements java.io.Serializable{
	
	private static final long serialVersionUID = -6666746590043422344L;
	
	private String email;
	
	public EmailRequestDto() {
		super();
	}

	public EmailRequestDto(String email) {
		super();
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "EmailRequestDto [email=" + email + "]";
	}

}
