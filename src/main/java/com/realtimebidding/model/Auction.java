package com.realtimebidding.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Auction implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	@DBRef
	private Product product;
	
	private Date startTime;
	
	private Date endTime;
	
	private Double startingAmount;
	
	@DBRef
	private Set<User> participants;
	
	private List<BidInformation> biddings;

	public Auction() {
		super();
	}
	
	public Auction(String id, Product product, Date startTime, Date endTime, Double startingAmount,
			Set<User> participants, List<BidInformation> biddings) {
		super();
		this.id = id;
		this.product = product;
		this.startTime = startTime;
		this.endTime = endTime;
		this.startingAmount = startingAmount;
		this.participants = participants;
		this.setBiddings(biddings);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Double getStartingAmount() {
		return startingAmount;
	}

	public void setStartingAmount(Double startingAmount) {
		this.startingAmount = startingAmount;
	}

	public Set<User> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<User> participants) {
		this.participants = participants;
	}

	public List<BidInformation> getBiddings() {
		return biddings;
	}

	public void setBiddings(List<BidInformation> biddings) {
		this.biddings = biddings;
	}

	@Override
	public String toString() {
		return "Auction [id=" + id + ", product=" + product + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", startingAmount=" + startingAmount + ", participants=" + participants + ", biddings=" + biddings
				+ "]";
	}
	
	
}
