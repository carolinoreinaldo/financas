package com.financas.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.financas.exceptions.RegraNegocioException;
import com.financas.model.entity.Lancamento;
import com.financas.model.entity.Usuario;
import com.financas.model.enums.StatusLancamento;
import com.financas.model.enums.TipoLancamento;
import com.financas.model.repository.LancamentoRepository;
import com.financas.service.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {

	private LancamentoRepository lancamentoRepository;

	@Autowired
	public LancamentoServiceImpl(LancamentoRepository lancamentoRepository) {
		this.lancamentoRepository = lancamentoRepository;
	}

	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return this.lancamentoRepository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		validar(lancamento);
		return this.lancamentoRepository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		this.lancamentoRepository.delete(lancamento);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		/**
		 * O Example é um recurso do spring data para pesquisarmos por filtro no baco de
		 * dados. Ele já verifica dentro do objeto filtro o que está preenchido para
		 * poder usar na busca.
		 */
		Example<Lancamento> example = Example.of(lancamentoFiltro,
				ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING));
		return this.lancamentoRepository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		this.atualizar(lancamento);
	}

	@Override
	public Optional<Lancamento> obterPorId(Long id) {
		return this.lancamentoRepository.findById(id);
	}

	@Override
	public void validar(Lancamento lancamento) {
		if (Strings.isBlank(lancamento.getDescricao())) {
			throw new RegraNegocioException("Informe uma descrição válida.");
		}

		final Integer mes = lancamento.getMes();
		if (Objects.isNull(mes) || mes < 1 || mes > 12) {
			throw new RegraNegocioException("Informe um Mês válido.");
		}

		final Integer ano = lancamento.getAno();
		if (Objects.isNull(ano) || ano.toString().length() != 4) {
			throw new RegraNegocioException("Informe um Ano válido.");
		}

		final Usuario usuario = lancamento.getUsuario();
		if (Objects.isNull(usuario) || Objects.isNull(usuario.getId())) {
			throw new RegraNegocioException("Usuário inválido, Informe um usuário válido.");
		}

		final BigDecimal valor = lancamento.getValor();
		if (Objects.isNull(valor) || valor.compareTo(BigDecimal.ZERO) < 1) {
			throw new RegraNegocioException("Informe um valor válido.");
		}
		
		final TipoLancamento tipo = lancamento.getTipo();
		if(Objects.isNull(tipo)) {
			throw new RegraNegocioException("Informe um tipo de lançamento válido.");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal obterSaldoPorUsuario(Long idUsuario) {
		BigDecimal totalReceitas = lancamentoRepository
				.obterSaldoPorTipoLancamentoEUsuarioEStatus(
						idUsuario, 
						TipoLancamento.RECEITA,
						StatusLancamento.EFETIVADO);
		
		BigDecimal totalDespesas = lancamentoRepository
				.obterSaldoPorTipoLancamentoEUsuarioEStatus(
						idUsuario, 
						TipoLancamento.DESPESA,
						StatusLancamento.EFETIVADO);
		
		if(Objects.isNull(totalReceitas)) {
			totalReceitas = BigDecimal.ZERO;
		}
		
		if(Objects.isNull(totalDespesas)) {
			totalDespesas = BigDecimal.ZERO;
		}
		
		return totalReceitas.subtract(totalDespesas);
	}

}