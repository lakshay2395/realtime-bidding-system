package com.realtimebidding.dtos;

import java.io.Serializable;
import java.sql.Timestamp;

public class AuctionMessageDto<T> implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private T content;
	
	private AuctionMessageTypeEnum messageType;
	
	private String userId;
	
	private Timestamp messageTime;

	public AuctionMessageDto(T content, AuctionMessageTypeEnum messageType, String userId, Timestamp messageTime) {
		super();
		this.content = content;
		this.setMessageType(messageType);
		this.userId = userId;
		this.messageTime = messageTime;
	}

	public AuctionMessageDto() {
		super();
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Timestamp getMessageTime() {
		return messageTime;
	}
	
	public void setMessageTime(Timestamp messageTime) {
		this.messageTime = messageTime;
	}

	public AuctionMessageTypeEnum getMessageType() {
		return messageType;
	}

	public void setMessageType(AuctionMessageTypeEnum messageType) {
		this.messageType = messageType;
	}

	@Override
	public String toString() {
		return "AuctionMessageDto [content=" + content + ", messageType=" + messageType + ", userId=" + userId
				+ ", messageTime=" + messageTime + "]";
	}
	
}
