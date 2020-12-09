package com.financas.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.financas.BaseTeste;
import com.financas.model.entity.Lancamento;
import com.financas.model.entity.Usuario;
import com.financas.model.enums.StatusLancamento;
import com.financas.model.enums.TipoLancamento;

public class LancamentoRepositoryTest extends BaseTeste {

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void deveSalvarUmLancamento() {
		Lancamento lan = criarLancamento();
		
		Lancamento lancamentoSalvo = this.lancamentoRepository.save(lan);
		Assertions.assertNotNull(lancamentoSalvo);
		Assertions.assertNotNull(lancamentoSalvo.getId());
		
		Optional<Lancamento> lancamentoEncontrado = this.lancamentoRepository.findById(lancamentoSalvo.getId());
		Assertions.assertTrue(lancamentoEncontrado.isPresent());
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		Lancamento novoLancamento = criarLancamento();
		Lancamento lancamentoSalvo = entityManager.persist(novoLancamento);
		
		Lancamento lancamentoEncontrado = this.entityManager.find(Lancamento.class, lancamentoSalvo.getId());
		
		lancamentoRepository.delete(lancamentoEncontrado);
		
		Lancamento lancamentoDeletado = this.entityManager.find(Lancamento.class, lancamentoSalvo.getId());
		
		Assertions.assertNull(lancamentoDeletado);
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		Lancamento novoLancamento = criarLancamento();
		Lancamento lancamentoSalvo = entityManager.persist(novoLancamento);
		
		Lancamento lancamentoEncontrado = this.entityManager.find(Lancamento.class, lancamentoSalvo.getId());
		
		final String novaDescricao = "descricão alterada";
		final BigDecimal novoValor = new BigDecimal(100);
		final Integer novoMes = 2;
		final Integer novoAno = 2010;
		
		lancamentoEncontrado.setDescricao(novaDescricao);
		lancamentoEncontrado.setValor(novoValor);
		lancamentoEncontrado.setMes(novoMes);
		lancamentoEncontrado.setAno(novoAno);
		
		this.lancamentoRepository.save(lancamentoEncontrado);
		
		Lancamento lancamentoAtualizado = this.entityManager.find(Lancamento.class, lancamentoEncontrado.getId());
		
		Assertions.assertSame(novaDescricao, lancamentoAtualizado.getDescricao());
		Assertions.assertSame(novoValor, lancamentoAtualizado.getValor());
		Assertions.assertSame(novoMes, lancamentoAtualizado.getMes());
		Assertions.assertSame(novoAno, lancamentoAtualizado.getAno());
	}
	
	@Test
	public void deveBuscarUmLancamentoPorId() {
		Lancamento novoLancamento = criarLancamento();
		
		Lancamento lancamentoSalvo = this.entityManager.persist(novoLancamento);
		
		Optional<Lancamento> lancamentoEncontrado = this.lancamentoRepository.findById(lancamentoSalvo.getId());
		
		Assertions.assertTrue(lancamentoEncontrado.isPresent());
	}
	
	public static Lancamento criarLancamento() {
		Lancamento lan = new Lancamento();
		lan.setAno(2020);
		lan.setDataCadastro(LocalDate.now());
		lan.setDescricao("lançamento de teste");
		lan.setMes(10);
		lan.setStatus(StatusLancamento.PENDENTE);
		lan.setTipo(TipoLancamento.DESPESA);
		lan.setValor(new BigDecimal(200));
		
		Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
		lan.setUsuario(usuario);
		return lan;
	}
	
	public static Lancamento criarLancamentoComDefaultID() {
		Lancamento lancamento = criarLancamento();
		lancamento.setId(1L);
		return lancamento;
	}
	
	public static Lancamento criarLancamentoComIDEUsuarioId() {
		Lancamento lancamento = criarLancamento();
		lancamento.setId(1L);
		lancamento.getUsuario().setId(1L);
		return lancamento;
	}
	
	public static Lancamento criarLancamentoComID(Long id) {
		if(Objects.isNull(id)) {
			throw new RuntimeException("id não pode ser nulo");
		}
		Lancamento lancamento = criarLancamento();
		lancamento.setId(id);
		return lancamento;
	}
}
