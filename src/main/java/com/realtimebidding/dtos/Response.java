package com.realtimebidding.dtos;

import java.io.Serializable;

public class Response<T> implements Serializable{

	private static final long serialVersionUID = 1L;

	private ResponseTypeEnum status;
	
	private T data;

	public Response(ResponseTypeEnum status, T data) {
		super();
		this.status = status;
		this.data = data;
	}

	public Response() {
		super();
	}

	public ResponseTypeEnum getStatus() {
		return status;
	}

	public void setStatus(ResponseTypeEnum status) {
		this.status = status;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Response [status=" + status + ", data=" + data + "]";
	}
	
}
