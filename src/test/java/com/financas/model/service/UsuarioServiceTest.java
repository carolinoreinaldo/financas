package com.financas.model.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.financas.BaseTeste;
import com.financas.exceptions.RegraNegocioException;
import com.financas.model.entity.Usuario;
import com.financas.model.repository.UsuarioRepository;
import com.financas.service.UsuarioService;

public class UsuarioServiceTest extends BaseTeste {

	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {

		this.usuarioRepository.deleteAll();
		
		this.usuarioService.validarEmail("email@email.com");
	}

	@Test(expected = RegraNegocioException.class)
	public void deveRetornarErroQuandoExistirEmailCadastrado() {
		
		this.usuarioRepository.deleteAll();
		
		final String email = "teste@teste.com";
		
		Usuario usuario = Usuario.builder().nome("usuario").email(email).build();
		
		this.usuarioRepository.save(usuario);
		
		this.usuarioService.validarEmail(email);
	}
}
