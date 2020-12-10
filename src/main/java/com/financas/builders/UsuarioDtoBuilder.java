package com.financas.builders;

import com.financas.api.dto.UsuarioDTO;

public class UsuarioDtoBuilder {

	private UsuarioDTO usuarioDTO;
	
	private UsuarioDtoBuilder() {
		this.usuarioDTO = new UsuarioDTO();
	}
	
	public static UsuarioDtoBuilder builder() {
		return new UsuarioDtoBuilder();
	}
	
	public UsuarioDtoBuilder nome(String nome) {
		this.usuarioDTO.setNome(nome);
		return this;
	}
	
	public UsuarioDtoBuilder email(String email) {
		this.usuarioDTO.setEmail(email);
		return this;
	}
	
	public UsuarioDtoBuilder senha(String senha) {
		this.usuarioDTO.setSenha(senha);
		return this;
	}
	
	public UsuarioDTO build() {
		return this.usuarioDTO;
	}
}
