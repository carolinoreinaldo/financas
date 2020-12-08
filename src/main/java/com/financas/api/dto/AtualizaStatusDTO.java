package com.financas.api.dto;

public class AtualizaStatusDTO {

	private String status;

	public AtualizaStatusDTO(String status) {
		this.status = status;
	}

	public AtualizaStatusDTO() {
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}