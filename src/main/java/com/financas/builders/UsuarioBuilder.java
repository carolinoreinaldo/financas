package com.financas.builders;

import com.financas.model.entity.Usuario;

public class UsuarioBuilder {

	private Usuario usuario;

	private UsuarioBuilder() {
		this.usuario = new Usuario();
	}

	public static UsuarioBuilder builder() {
		return new UsuarioBuilder();
	}

	public UsuarioBuilder nome(String nome) {
		this.usuario.setNome(nome);
		return this;
	}

	public UsuarioBuilder email(String email) {
		this.usuario.setEmail(email);
		return this;
	}

	public UsuarioBuilder id(Long id) {
		this.usuario.setId(id);
		return this;
	}

	public UsuarioBuilder senha(String senha) {
		this.usuario.setSenha(senha);
		return this;
	}

	public Usuario build() {
		return this.usuario;
	}

}
