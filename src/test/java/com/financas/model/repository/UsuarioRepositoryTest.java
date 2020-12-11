package com.financas.model.repository;

import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.financas.BaseTeste;
import com.financas.builders.UsuarioBuilder;
import com.financas.model.entity.Usuario;

public class UsuarioRepositoryTest extends BaseTeste {

	@Autowired
	UsuarioRepository usuarioRepository;

	/*
	 * EntityManager que o Spring fornece para nós manipularmos os dados no banco de
	 * dados.
	 * 
	 * Importente : Esse é um entityManager de teste
	 */
	@Autowired
	TestEntityManager entityManager;

	@Test
	public void deveVerificarAExistenciaDeUmEmail() {

		Usuario usuario = UsuarioBuilder.builder().nome("usuario").email("usuario@email.com").build();

		entityManager.persist(usuario);

		boolean result = usuarioRepository.existsByEmail(usuario.getEmail());

		Assertions.assertTrue(result);
	}

	@Test
	public void deveRetornarFalsoQuandoNaoHouveUsuarioCadastradoComOEmail() {

		Usuario usuario = UsuarioBuilder.builder().nome("usuario").email("usuario@email.com").build();

		entityManager.persist(usuario);

		boolean result = usuarioRepository.existsByEmail("outroEmail@gmail.com");

		Assertions.assertFalse(result);
	}

	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		// cenário
		Usuario usuario = UsuarioBuilder.builder().nome("usuario").email("usuario@email.com").build();

		// Ação
		Usuario usuarioSalvo = entityManager.persist(usuario);

		// teste
		Assertions.assertTrue(usuarioSalvo.getId() != null);
	}

	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		
		final String email = "usuario@gmail.com";
		// cenário
		Usuario usuario = UsuarioBuilder.builder().nome("usuario").email(email).build();

		// Ação
		entityManager.persist(usuario);
		Optional<Usuario> usuarioSalvo = this.usuarioRepository.findByEmail(email);
		// teste
		Assertions.assertTrue(usuarioSalvo.isPresent());
	}
	
	@Test
	public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase() {

		final String email = "usuario@gmail.com";

		// Ação
		Optional<Usuario> usuarioSalvo = this.usuarioRepository.findByEmail(email);

		// teste
		Assertions.assertFalse(usuarioSalvo.isPresent());
	}

}