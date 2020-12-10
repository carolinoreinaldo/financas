package com.financas.fakes;

import com.financas.api.dto.UsuarioDTO;
import com.financas.builders.UsuarioDtoBuilder;

public class UsuarioDtoFake {

	public static UsuarioDTO gerar() {
		return UsuarioDtoBuilder
				.builder()
				.email("usuario@teste.com.br")
				.senha("1234")
				.nome("Usuario de Teste")
				.build();
	}
}
