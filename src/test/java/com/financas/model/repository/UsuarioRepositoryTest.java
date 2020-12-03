package com.financas.model.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.financas.model.entity.Usuario;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test") //aqui ele vai pegar de application-test.properties
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository usuarioRepository;

	@Test
	public void deveVerificarAExistenciaDeUmEmail() {

		Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
		
		usuarioRepository.save(usuario);
		
		boolean result = usuarioRepository.existsByEmail(usuario.getEmail());
		
		Assertions.assertTrue(result);
	}
}
