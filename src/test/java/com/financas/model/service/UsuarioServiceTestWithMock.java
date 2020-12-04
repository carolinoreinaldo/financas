package com.financas.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.financas.BaseTeste;
import com.financas.exceptions.RegraNegocioException;
import com.financas.model.entity.Usuario;
import com.financas.model.repository.UsuarioRepository;
import com.financas.service.UsuarioService;
import com.financas.service.impl.UsuarioServiceImpl;

public class UsuarioServiceTestWithMock extends BaseTeste {

	UsuarioService usuarioService;
	UsuarioRepository usuarioRepository;

	@BeforeEach
	public void setUp() {
		this.usuarioRepository = Mockito.mock(UsuarioRepository.class);
		this.usuarioService = new UsuarioServiceImpl(usuarioRepository);
	}
	
	//@Test(expected = Test.None.class)
	@Test
	public void deveValidarEmail() {

		Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		Assertions.assertDoesNotThrow( () -> {
			this.usuarioService.validarEmail("email@email.com");
		});
	}

	//@Test(expected = RegraNegocioException.class)
	@Test
	public void deveRetornarErroQuandoExistirEmailCadastrado() {
		
		final String email = "teste@teste.com";
		
		Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			usuarioService.validarEmail(email);
		});
			
	}
}
