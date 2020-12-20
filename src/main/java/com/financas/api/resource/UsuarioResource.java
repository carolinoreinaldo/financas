package com.financas.api.resource;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financas.api.dto.UsuarioDTO;
import com.financas.exceptions.ErroAutenticacaoException;
import com.financas.exceptions.RegraNegocioException;
import com.financas.model.entity.Usuario;
import com.financas.service.LancamentoService;
import com.financas.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

	private UsuarioService usuarioService;
	private LancamentoService lancamentoService;

	@Autowired
	public UsuarioResource(UsuarioService usuarioService, LancamentoService lancamentoService) {
		this.usuarioService = usuarioService;
		this.lancamentoService = lancamentoService;
	}

	@PostMapping
	public ResponseEntity<Object> salvar(@RequestBody UsuarioDTO usuarioDto) {

		Usuario usuario = usuarioDto.converterParaUsuario();
		try {
			Usuario usuarioSalvo = this.usuarioService.salvarUsuario(usuario);
			return new ResponseEntity<Object>(usuarioSalvo, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/autenticar")
	public ResponseEntity<Object> autenticar(@RequestBody UsuarioDTO usuarioDto) {
		try {
			Usuario usuarioAutenticado = usuarioService.autenticar(usuarioDto.getEmail(), usuarioDto.getSenha());
			return ResponseEntity.ok(usuarioAutenticado);
		} catch (ErroAutenticacaoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("{id}/saldo")
	public ResponseEntity<? extends Object> obterSaldo(@PathVariable("id") Long usuarioId) {
		Optional<Usuario> usuarioEnconetrado = usuarioService.obterPorId(usuarioId);
		if(!usuarioEnconetrado.isPresent()) {
			final String msg = "Usuário não encontrado para o ID informado. Informe um usuário válido";
			return new ResponseEntity(msg, HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(lancamentoService.obterSaldoPorUsuario(usuarioId));
	}
}
