package com.financas.api.resource;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.financas.api.dto.AtualizaStatusDTO;
import com.financas.api.dto.LancamentoDTO;
import com.financas.api.dto.OptionDTO;
import com.financas.exceptions.RegraNegocioException;
import com.financas.model.entity.Lancamento;
import com.financas.model.entity.Usuario;
import com.financas.model.enums.StatusLancamento;
import com.financas.model.enums.TipoLancamento;
import com.financas.service.LancamentoService;
import com.financas.service.UsuarioService;

@RestController
@RequestMapping("api/lancamentos")
public class LancamentoResource {

	private static final String USUARIO_NAO_ENCONTRADO_PARA_ID = "Usuário não encontrado para o ID informado";
	private static final String LANCAMENTO_NAO_ENCONTRADO = "Lançamento não encontrado na base de Dados.";
	private static final String USUARIO_ID_NULL = "O campo usuario não pode ser nulo";
	private static final String ERRO_AO_ATUALIZAR_STATUS = "Não foi possível atualizar o status do lançamento, envie um status válido";
	
	private final LancamentoService lancamentoService;
	private final UsuarioService usuarioService;

	@Autowired
	public LancamentoResource(LancamentoService lancamentoService, UsuarioService usuarioService) {
		this.lancamentoService = lancamentoService;
		this.usuarioService = usuarioService;
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}/atualiza-status")
	public ResponseEntity atualizarStatus( 
			@PathVariable("id") Long id , 
			@RequestBody AtualizaStatusDTO atualizaStatusDto) {
		
		return lancamentoService.obterPorId(id).map(lancamentoBd -> {
			StatusLancamento novoStatus = StatusLancamento.valueOf(atualizaStatusDto.getStatus());
			if(Objects.isNull(novoStatus)) {
				return badRequest(ERRO_AO_ATUALIZAR_STATUS);
			} 
			
			try {
				lancamentoBd.setStatus(novoStatus);
				Lancamento lancamentoAtualizado = lancamentoService.atualizar(lancamentoBd);
				return ResponseEntity.ok(lancamentoAtualizado);
				
			} catch(RegraNegocioException e) {
				return badRequest(e.getMessage());
			}
		}).orElseGet( () -> badRequest(LANCAMENTO_NAO_ENCONTRADO));
	}

	@SuppressWarnings("rawtypes")
	@GetMapping
	public ResponseEntity buscar(
			@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano,
			@RequestParam(value = "usuario", required = false) Long idUsuario){
		
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		
		if(Objects.isNull(idUsuario)) {
			return badRequest(USUARIO_ID_NULL);
		}
		
		Optional<Usuario> usuarioEncontrado = usuarioService.obterPorId(idUsuario);
		if(!usuarioEncontrado.isPresent()) {
			return badRequest(USUARIO_NAO_ENCONTRADO_PARA_ID);
		} else {
			lancamentoFiltro.setUsuario(usuarioEncontrado.get());
		}
		
		List<Lancamento> lancamentosEncontrados = lancamentoService.buscar(lancamentoFiltro);
		
		return responseOk(lancamentosEncontrados);
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("{id}")
	public ResponseEntity deletar( @PathVariable Long id ) {
		
		return lancamentoService.obterPorId(id).map( lancamento -> {
			lancamentoService.deletar(lancamento);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet( () -> new ResponseEntity(LANCAMENTO_NAO_ENCONTRADO, HttpStatus.BAD_REQUEST));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("{id}")
	public ResponseEntity<? extends Object> atualizar(@PathVariable Long id, @RequestBody LancamentoDTO lancamentoDto) {
		try {
			return lancamentoService.obterPorId(id).map(entity -> {
				Lancamento lancamento = converteParaLancamento(lancamentoDto);
				lancamento.setId(id);
				Lancamento lancamentoAtualizado = lancamentoService.atualizar(lancamento);
				return ResponseEntity.ok(lancamentoAtualizado);
			}).orElseGet( () -> new ResponseEntity(LANCAMENTO_NAO_ENCONTRADO, HttpStatus.BAD_REQUEST));
			
		} catch (RegraNegocioException e) {
			return badRequest(e.getMessage());
		}

	}

	@PostMapping
	public ResponseEntity<? extends Object> salvar(@RequestBody LancamentoDTO lancamentoDto) {
		try {
			Lancamento lancamento = this.lancamentoService.salvar(converteParaLancamento(lancamentoDto));
			return new ResponseEntity<Lancamento>(lancamento, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	private Lancamento converteParaLancamento(LancamentoDTO dto) {
		Lancamento lancamento = new Lancamento();
		lancamento.setId(dto.getId());
		lancamento.setAno(dto.getAno());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setMes(dto.getMes());
		
		if(Objects.nonNull(dto.getStatus())) {
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		}
		
		if(Objects.nonNull(dto.getTipo())) {
			lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		}
		lancamento.setValor(dto.getValor());

		Usuario usuarioEncontrado = usuarioService.obterPorId(dto.getUsuario())
				.orElseThrow(() -> new RegraNegocioException(USUARIO_NAO_ENCONTRADO_PARA_ID));

		lancamento.setUsuario(usuarioEncontrado);
		return lancamento;
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping("/tipos-lancamento")
	public ResponseEntity obterTipos(){
		
		return responseOk(TipoLancamento.values());
		
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping("/meses")
	public ResponseEntity obterMeses(){
		
		final List<OptionDTO> meses = Arrays.asList(
			new OptionDTO("Janeiro", 1),
			new OptionDTO("Fevereiro", 2),
			new OptionDTO("Março", 3),
			new OptionDTO("Abril", 4),
			new OptionDTO("Maio", 5),
			new OptionDTO("Junho", 6),
			new OptionDTO("Julho", 7),
			new OptionDTO("Agosto", 8),
			new OptionDTO("Setembro", 9),
			new OptionDTO("Outubro", 10),
			new OptionDTO("Novembro", 12),
			new OptionDTO("Dezembro", 12)
		);
		return responseOk(meses);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("{lancamentoId}")
	public ResponseEntity obterLancamento( @PathVariable("lancamentoId") Long lancamentoId) {
		Optional<Lancamento> lancamento = this.lancamentoService.obterPorId(lancamentoId);
		
		return lancamento
				.map(lan -> new ResponseEntity(lan.getLancamentoDTO(), HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity(HttpStatus.NOT_FOUND));
			
	}
	
	private ResponseEntity<? extends Object> badRequest(String msg) {
		return ResponseEntity.badRequest().body(msg);
	}
	
	private ResponseEntity<? extends Object> responseOk(Object obj) {
		return ResponseEntity.ok(obj);
	}

}