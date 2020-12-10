package com.financas.api.resource;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financas.BaseResourceTest;
import com.financas.api.dto.UsuarioDTO;
import com.financas.builders.UsuarioBuilder;
import com.financas.builders.UsuarioDtoBuilder;
import com.financas.exceptions.ErroAutenticacaoException;
import com.financas.exceptions.RegraNegocioException;
import com.financas.model.entity.Usuario;
import com.financas.service.LancamentoService;
import com.financas.service.UsuarioService;

@WebMvcTest( controllers = UsuarioResource.class)
public class UsuarioResourceTest extends BaseResourceTest {

	private static final String API_USUARIO = "/api/usuarios";
	private MediaType MEDIA_TYPE_JSON = MediaType.APPLICATION_JSON;
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UsuarioService usuarioService;
	
	@MockBean
	private LancamentoService lancamentoService;
	
	@Test
	public void deveAutenticarUmUsuario() throws Exception {
		//cenario
		final String email = "usuario@teste.com";
		final String senha = "1234";
		final String nome = "usuario de teste";
		
		final UsuarioDTO usuarioDto = UsuarioDtoBuilder
				.builder()
				.email(email)
				.nome(nome)
				.senha(senha)
				.build();
		
		final Usuario usuario = UsuarioBuilder
				.builder()
				.id(1L)
				.email(email)
				.senha(senha)
				.build();
		
		Mockito.when(usuarioService.autenticar(email, senha) ).thenReturn(usuario);
		
		String json = new ObjectMapper().writeValueAsString(usuarioDto);
		
		//monta a requisicao
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.post( API_USUARIO.concat("/autenticar") )
			.accept( MEDIA_TYPE_JSON )
			.contentType( MEDIA_TYPE_JSON )
			.content(json);
		
		//realiza a requisicao
		mvc.perform(request)
		.andExpect( MockMvcResultMatchers.status().isOk() )
		.andExpect( MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
		.andExpect( MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
		.andExpect( MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));
	}
	
	@Test
	public void deveRetornarErroAoAutenticarUmUsuario() throws Exception {
		//cenario
		final String email = "usuario@teste.com";
		final String senha = "1234";
		final String nome = "usuario de teste";
		
		final UsuarioDTO usuarioDto = UsuarioDtoBuilder
				.builder()
				.email(email)
				.nome(nome)
				.senha(senha)
				.build();
		
		Mockito.when(usuarioService.autenticar(email, senha) ).thenThrow(ErroAutenticacaoException.class);
		
		String json = new ObjectMapper().writeValueAsString(usuarioDto);
		
		//monta a requisicao
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.post( API_USUARIO.concat("/autenticar") )
			.accept( MEDIA_TYPE_JSON )
			.contentType( MEDIA_TYPE_JSON )
			.content(json);
		
		//realiza a requisicao
		mvc.perform(request)
			.andExpect( MockMvcResultMatchers.status().isBadRequest() );
	}
	
	@Test
	public void deveSalvarUsuarioComSucesso() throws Exception {
		//cenario
		final String email = "usuario@teste.com";
		final String senha = "1234";
		final String nome = "usuario de teste";
		final Long id = 1L;
		
		final UsuarioDTO usuarioDto = UsuarioDtoBuilder
				.builder()
				.email(email)
				.nome(nome)
				.senha(senha)
				.build();
		
		final Usuario usuario = UsuarioBuilder
				.builder()
				.email(email)
				.senha(senha)
				.nome(nome)
				.build();
		
		final Usuario usuarioSalvo = usuario;
		usuarioSalvo.setId(id);
		
		Mockito.when( usuarioService.salvarUsuario(Mockito.any(Usuario.class)) ).thenReturn(usuarioSalvo);
		
		String json = new ObjectMapper().writeValueAsString(usuarioDto);
		
		//monta a requisicao
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.post( API_USUARIO )
			.accept( MEDIA_TYPE_JSON )
			.contentType( MEDIA_TYPE_JSON )
			.content(json);
		
		//realiza a requisicao
		mvc.perform(request)
		.andExpect( MockMvcResultMatchers.status().isCreated() )
		.andExpect( MockMvcResultMatchers.jsonPath("id").value(id))
		.andExpect( MockMvcResultMatchers.jsonPath("nome").value(nome))
		.andExpect( MockMvcResultMatchers.jsonPath("email").value(email));
	}
	
	@Test
	public void deveLancarErroAoSalvarUsuario() throws Exception {
		//cenario
		final String email = "usuario@teste.com";
		final String senha = "1234";
		final String nome = "usuario de teste";
		final Long id = 1L;
		
		final UsuarioDTO usuarioDto = UsuarioDtoBuilder
				.builder()
				.email(email)
				.nome(nome)
				.senha(senha)
				.build();
		
		final Usuario usuario = UsuarioBuilder
				.builder()
				.email(email)
				.senha(senha)
				.nome(nome)
				.build();
		
		final Usuario usuarioSalvo = usuario;
		usuarioSalvo.setId(id);
		
		Mockito.when( usuarioService.salvarUsuario(Mockito.any(Usuario.class)) ).thenThrow(RegraNegocioException.class);
		
		String json = new ObjectMapper().writeValueAsString(usuarioDto);
		
		//monta a requisicao
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.post( API_USUARIO )
			.accept( MEDIA_TYPE_JSON )
			.contentType( MEDIA_TYPE_JSON )
			.content(json);
		
		//realiza a requisicao
		mvc.perform(request)
		.andExpect( MockMvcResultMatchers.status().isBadRequest() );
	}
}
