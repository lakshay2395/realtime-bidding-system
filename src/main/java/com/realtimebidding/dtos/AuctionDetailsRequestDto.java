package com.realtimebidding.dtos;

import java.io.Serializable;
import java.sql.Timestamp;

public class AuctionDetailsRequestDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private String userId;
	
	private Timestamp requestTime;

	public AuctionDetailsRequestDto(String userId, Timestamp requestTime) {
		super();
		this.userId = userId;
		this.requestTime = requestTime;
	}

	public AuctionDetailsRequestDto() {
		super();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Timestamp getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Timestamp requestTime) {
		this.requestTime = requestTime;
	}

	@Override
	public String toString() {
		return "AuctionDetailsRequestDto [userId=" + userId + ", requestTime=" + requestTime + "]";
	}

}
