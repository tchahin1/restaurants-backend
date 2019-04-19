package com.example.restaurants.data.commons;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Response {

	private Boolean success;
	private String message;
	private Object data;

	public Response() {
	}

	public Response(String message) {
		this.message = message;
	}

	public Response(Boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public Response(Boolean success, String message, Object data) {
		this.success = success;
		this.message = message;
		this.data = data;
	}
}
