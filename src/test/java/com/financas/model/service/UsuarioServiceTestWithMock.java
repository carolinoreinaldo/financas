package com.financas.model.service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.financas.BaseTeste;
import com.financas.exceptions.ErroAutenticacaoException;
import com.financas.exceptions.RegraNegocioException;
import com.financas.model.entity.Usuario;
import com.financas.model.repository.UsuarioRepository;
import com.financas.service.impl.UsuarioServiceImpl;

public class UsuarioServiceTestWithMock extends BaseTeste {

	/*
	 * Spy significa espião em inglês.
	 * 
	 * Anotar esse service com @SpyBean significa que esse service vai ser um
	 * service normal só que, se quisermos mockar algum método desse service nós
	 * conseguimos também.
	 * 
	 * Repare que para usar o @SpyBean, precisamos trocar a interface pela classe de
	 * implementação Impl
	 */
	@SpyBean
	UsuarioServiceImpl usuarioService;

	@MockBean
	UsuarioRepository usuarioRepository;

	@Test
	public void deveAutenticarUsuarioComSucesso() {
		final String email = "teste@teste.com";
		final String senha = "teste";
		final Usuario usuario = Usuario.builder().email(email).senha(senha).id(1L).build();

		Mockito.when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

		Assertions.assertDoesNotThrow(() -> {
			this.usuarioService.autenticar(email, senha);
		});
	}

	@Test
	public void deveLancarExcecaoAoAutenticarUsuarioPorQueEmailEstaErrado() {
		final String emailErrado = "emailErrado@teste.com";

		Mockito.when(usuarioRepository.findByEmail(emailErrado)).thenReturn(Optional.empty());

		ErroAutenticacaoException assertThrows = Assertions.assertThrows(ErroAutenticacaoException.class, () -> {
			this.usuarioService.autenticar(emailErrado, "qualquerSenha");
		});

		Assertions.assertSame("Usuário não encontrado", assertThrows.getMessage());
	}

	@Test
	public void deveLancarExcecaoAoAutenticarUsuarioPorQueSenhaEstaErrada() {
		final String email = "teste@teste.com";
		final String senha = "teste";
		final String senhaErrada = "senhaErrada";

		final Usuario usuario = Usuario.builder().email(email).senha(senha).id(1L).build();

		Mockito.when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

		ErroAutenticacaoException assertThrows = Assertions.assertThrows(ErroAutenticacaoException.class, () -> {
			this.usuarioService.autenticar(email, senhaErrada);
		});

		Assertions.assertSame("Senha inválida", assertThrows.getMessage());
	}

	@Test
	public void deveValidarEmail() {

		Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(false);

		Assertions.assertDoesNotThrow(() -> {
			this.usuarioService.validarEmail("email@email.com");
		});
	}

	@Test
	public void deveRetornarErroQuandoExistirEmailCadastrado() {

		final String email = "teste@teste.com";

		Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

		Assertions.assertThrows(RegraNegocioException.class, () -> {
			usuarioService.validarEmail(email);
		});
	}

	// teste do comportamento do método que salva usuário
	@Test
	public void deveSalvarUsuario() {

		Mockito.doNothing().when(usuarioService).validarEmail(Mockito.anyString());

		final String email = "teste@teste.com";
		final String senha = "teste";

		final Usuario usuario = Usuario.builder().email(email).senha(senha).build();

		Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

		Usuario usuarioSalvo = usuarioService.salvarUsuario(new Usuario());

		Assertions.assertSame(usuario.getId(), usuarioSalvo.getId());
		Assertions.assertSame(usuario.getEmail(), usuarioSalvo.getEmail());
		Assertions.assertSame(usuario.getSenha(), usuarioSalvo.getSenha());

	}

	@Test
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {

		final String email = "teste@teste.com";

		final Usuario usuario = Usuario.builder().email(email).build();

		Mockito.doThrow(RegraNegocioException.class).when(usuarioService).validarEmail(email);
		
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			usuarioService.salvarUsuario(usuario);
		});
		
		/*
		 * Aqui a combinação Mockito.verify e Mockito.never verificam se
		 * alguma ve o usuarioRepository voi chamado
		 */
		Mockito.verify( usuarioRepository, Mockito.never()).save(usuario);

	}
}
