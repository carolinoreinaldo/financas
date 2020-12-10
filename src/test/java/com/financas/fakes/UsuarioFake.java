package com.financas.fakes;

import com.financas.builders.UsuarioBuilder;
import com.financas.model.entity.Usuario;

public class UsuarioFake {

	private UsuarioFake() {}
	
	public static Usuario criarUsuarioCompleto() {
		return criarUsuarioDefault();
	}
	
	public static Usuario criarUsuarioSemId() {
		Usuario usuario = criarUsuarioDefault();
		usuario.setId(null);
		return usuario;
	}
	
	public static Usuario criarUsuarioComId(Long id) {
		Usuario usuario = criarUsuarioDefault();
		usuario.setId(id);
		return usuario;
	}
	
	private static Usuario criarUsuarioDefault() {
		return UsuarioBuilder.builder()
				.email("usuario-teste@gmail.com")
				.nome("Usuario de Teste")
				.senha("1234")
				.id(1L)
				.build();
	}
}
