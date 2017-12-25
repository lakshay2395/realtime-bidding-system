package com.realtimebidding.dtos;

import java.io.Serializable;

public class AuctionBidRequestDto implements Serializable{
	
	private static final long serialVersionUID = 7197065609469596779L;

	private Double bidAmount;
	
	private String userId;

	public AuctionBidRequestDto(Double bidAmount, String userId) {
		super();
		this.bidAmount = bidAmount;
		this.userId = userId;
	}

	public AuctionBidRequestDto() {
		super();
	}

	public Double getBidAmount() {
		return bidAmount;
	}

	public void setBidAmount(Double bidAmount) {
		this.bidAmount = bidAmount;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "AuctionBidRequestDto [bidAmount=" + bidAmount + ", userId=" + userId + "]";
	}

}
