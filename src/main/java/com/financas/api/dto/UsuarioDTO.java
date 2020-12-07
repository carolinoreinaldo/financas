package com.financas.api.dto;

import com.financas.model.entity.Usuario;

public class UsuarioDTO {

	private String email;
	private String nome;
	private String senha;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public Usuario converterParaUsuario() {
		Usuario usuario = new Usuario();
		usuario.setEmail(getEmail());
		usuario.setNome(getNome());
		usuario.setSenha(getSenha());
		return usuario;
	}
}
