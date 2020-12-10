package com.financas.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;

import com.financas.BaseTeste;
import com.financas.exceptions.RegraNegocioException;
import com.financas.model.entity.Lancamento;
import com.financas.model.enums.StatusLancamento;
import com.financas.model.repository.LancamentoRepository;
import com.financas.model.repository.LancamentoRepositoryTest;
import com.financas.service.impl.LancamentoServiceImpl;

public class LancamentoServiceTestWithMock extends BaseTeste {

	@SpyBean
	private LancamentoServiceImpl lancamentoService;
	
	@MockBean LancamentoRepository lancamentoRepository;
	
	@Test
	public void deveSalvarUmLancamento() {
		Lancamento lancamentoParaSalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doNothing().when(lancamentoService).validar(lancamentoParaSalvar);
		
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		
		final Long id = 1L;
		final Integer ano = 2013;
		final Integer mes = 5;
		final String descricao = "descrição para ser testada";
		
		lancamentoSalvo.setId(1L);
		lancamentoSalvo.setAno(ano);
		lancamentoSalvo.setMes(mes);
		lancamentoSalvo.setDescricao(descricao);
		
		Mockito.when(lancamentoRepository.save(lancamentoParaSalvar)).thenReturn(lancamentoSalvo);
		
		Lancamento lancamentoParaTestar = this.lancamentoService.salvar(lancamentoParaSalvar);
		
		Assertions.assertSame(descricao, lancamentoParaTestar.getDescricao());
		Assertions.assertSame(mes, lancamentoParaTestar.getMes());
		Assertions.assertSame(ano, lancamentoParaTestar.getAno());
		Assertions.assertSame(id, lancamentoParaTestar.getId());
	}
	
	@Test
	public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamentoComDefaultID();

		Mockito.doNothing().when(lancamentoService).validar(lancamento);
		
		Mockito.when(lancamentoRepository.save(lancamento)).thenReturn(lancamento);

		this.lancamentoService.atualizar(lancamento);
		
		Mockito.verify(lancamentoRepository, Mockito.times(1)).save(lancamento);
	}
	
	@Test
	public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();

		Assertions.assertThrows(NullPointerException.class, () -> {
			this.lancamentoService.atualizar(lancamento);
		});
		
		Mockito.verify(lancamentoRepository, Mockito.never()).save(lancamento);
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamentoComDefaultID();
		
		this.lancamentoService.deletar(lancamento);
		
		Mockito.verify(lancamentoRepository, Mockito.times(1)).delete(lancamento);
	}
	
	@Test
	public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		
		Assertions.assertThrows(NullPointerException.class, () -> {
			this.lancamentoService.deletar(lancamento);
		});
		
		Mockito.verify(lancamentoRepository, Mockito.never()).delete(lancamento);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void deveFiltrarLancamentos() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamentoComDefaultID();
		
		List<Lancamento> lista = new ArrayList<Lancamento>();
		lista.add(lancamento);
		
		Mockito.when(lancamentoRepository.findAll(Mockito.any(Example.class))).thenReturn(lista);
		
		List<Lancamento> resultado = lancamentoService.buscar(lancamento);
		
		Assertions.assertTrue(resultado.size() > 0);
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		
		Lancamento lancamentoParaSalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doNothing().when(lancamentoService).validar(lancamentoParaSalvar);
	}
	
	@Test
	public void deveAtualizarStatus() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamentoComDefaultID();
		
		Mockito.doReturn(lancamento).when(lancamentoService).atualizar(lancamento);
		
		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;

		lancamentoService.atualizarStatus(lancamento, novoStatus);
		
		Assertions.assertSame(lancamento.getStatus(), novoStatus);
		Mockito.verify(lancamentoService).atualizar(lancamento);
	}
	
	@Test
	public void deveObterUmLancamentoPorID() {
		//cenário
		final Long id = 1L;
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamentoComID(id);
		
		Mockito.when(lancamentoRepository.findById(id)).thenReturn(Optional.of(lancamento));
		
		//execução
		Optional<Lancamento> resultado = lancamentoService.obterPorId(id);
		
		Assertions.assertTrue(resultado.isPresent());
	}
	
	@Test
	public void deveRetornarVazioQuandoOLancamentoNaoExiste() {
		//cenário
		final Long id = 1L;
		
		Mockito.when(lancamentoRepository.findById(id)).thenReturn(Optional.empty());
		
		//execução
		Optional<Lancamento> resultado = lancamentoService.obterPorId(id);
		
		Assertions.assertTrue(resultado.isEmpty());
	}
	
	@Test
	public void deveValidarSeDescricaoNula() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamentoComDefaultID();
		
		lancamento.setDescricao(null);
		
		final String msgErroEsperada = "Informe uma descrição válida.";
		
		RegraNegocioException erro = Assertions.assertThrows(RegraNegocioException.class, () -> {
			lancamentoService.salvar(lancamento);
		});
		
		Assertions.assertSame(msgErroEsperada, erro.getMessage());
	}
	
	@Test
	public void deveValidarSeDescricaoEmBranco() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamentoComDefaultID();
		lancamento.setDescricao(Strings.EMPTY);
		final String msgErroEsperada = "Informe uma descrição válida.";
		testePadraoDeValidacaoAoSalvar(msgErroEsperada, lancamento);
	}
	
	@Test
	public void deveValidarSeMesNulo() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamentoComDefaultID();
		lancamento.setMes(null);
		final String msgErroEsperada = "Informe um Mês válido.";
		testePadraoDeValidacaoAoSalvar(msgErroEsperada, lancamento);
	}
	
	@Test
	public void deveValidarSeMesMaiorQue12() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamentoComDefaultID();
		lancamento.setMes(13);
		final String msgErroEsperada = "Informe um Mês válido.";
		testePadraoDeValidacaoAoSalvar(msgErroEsperada, lancamento);
	}
	
	@Test
	public void deveValidarSeAnoNulo() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamentoComDefaultID();
		lancamento.setAno(null);
		final String msgErroEsperada = "Informe um Ano válido.";
		testePadraoDeValidacaoAoSalvar(msgErroEsperada, lancamento);
	}
	
	@Test
	public void deveValidarSeAnoNaoTem4Digitos() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamentoComDefaultID();
		lancamento.setAno(123);
		final String msgErroEsperada = "Informe um Ano válido.";
		testePadraoDeValidacaoAoSalvar(msgErroEsperada, lancamento);
	}
	
	@Test
	public void deveValidarSeMesMenorQue1() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamentoComDefaultID();
		lancamento.setMes(0);
		final String msgErroEsperada = "Informe um Mês válido.";
		testePadraoDeValidacaoAoSalvar(msgErroEsperada, lancamento);
	}
	
	@Test
	public void deveValidarSeUsuarioNulo() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamentoComDefaultID();
		lancamento.setUsuario(null);
		final String msgErroEsperada = "Usuário inválido, Informe um usuário válido.";
		testePadraoDeValidacaoAoSalvar(msgErroEsperada, lancamento);
	}
	
	@Test
	public void deveValidarSeUsuarioNaoTemId() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamentoComIDEUsuarioId();
		lancamento.getUsuario().setId(null);
		final String msgErroEsperada = "Usuário inválido, Informe um usuário válido.";
		testePadraoDeValidacaoAoSalvar(msgErroEsperada, lancamento);
	}
	
	@Test
	public void deveValidarSeValorNulo() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamentoComIDEUsuarioId();
		lancamento.setValor(null);
		final String msgErroEsperada = "Informe um valor válido.";
		testePadraoDeValidacaoAoSalvar(msgErroEsperada, lancamento);
	}
	
	@Test
	public void deveValidarSeValorMenorQueZero() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamentoComIDEUsuarioId();
		lancamento.setValor(new BigDecimal(-1));
		final String msgErroEsperada = "Informe um valor válido.";
		testePadraoDeValidacaoAoSalvar(msgErroEsperada, lancamento);
	}
	
	@Test
	public void deveValidarSeValorSeTipoNulo() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamentoComIDEUsuarioId();
		lancamento.setTipo(null);
		final String msgErroEsperada = "Informe um tipo de lançamento válido.";
		testePadraoDeValidacaoAoSalvar(msgErroEsperada, lancamento);
	}


	
	private void testePadraoDeValidacaoAoSalvar(final String msgErroEsperada, Lancamento lancamento) {
		RegraNegocioException erro = Assertions.assertThrows(RegraNegocioException.class, () -> {
			lancamentoService.salvar(lancamento);
		});
		
		Assertions.assertSame(msgErroEsperada, erro.getMessage());
	}
}