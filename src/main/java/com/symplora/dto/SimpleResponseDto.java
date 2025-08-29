package com.symplora.dto;

public class SimpleResponseDto {
	private String message;

	public SimpleResponseDto() {
	}

	public SimpleResponseDto(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
