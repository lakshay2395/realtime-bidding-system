package com.realtimebidding.dtos;

import java.io.Serializable;
import java.sql.Timestamp;

public class AuctionJoinRequestDto implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String userId;
	
	private String userName;
	
	private Timestamp joiningTime;

	public AuctionJoinRequestDto(String userId, String userName, Timestamp joiningTime) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.joiningTime = joiningTime;
	}

	public AuctionJoinRequestDto() {
		super();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Timestamp getJoiningTime() {
		return joiningTime;
	}

	public void setJoiningTime(Timestamp joiningTime) {
		this.joiningTime = joiningTime;
	}

	@Override
	public String toString() {
		return "AuctionJoinRequestDto [userId=" + userId + ", userName=" + userName + ", joiningTime=" + joiningTime
				+ "]";
	}
}
