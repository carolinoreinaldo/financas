package com.financas.builders;

import java.math.BigDecimal;

import com.financas.api.dto.LancamentoDTO;
import com.financas.model.enums.StatusLancamento;
import com.financas.model.enums.TipoLancamento;

public class LancamentoDTOBuilder {

	private LancamentoDTO lancamentoDto;

	private LancamentoDTOBuilder() {
		this.lancamentoDto = new LancamentoDTO();
	}

	public static LancamentoDTOBuilder newInstance() {
		return new LancamentoDTOBuilder();
	}

	public LancamentoDTOBuilder id(Long id) {
		this.lancamentoDto.setId(id);
		return this;
	}

	public LancamentoDTOBuilder descricao(String descricao) {
		this.lancamentoDto.setDescricao(descricao);
		return this;
	}

	public LancamentoDTOBuilder tipo(TipoLancamento tipo) {
		this.lancamentoDto.setTipo(tipo.name());
		return this;
	}

	public LancamentoDTOBuilder mes(Integer mes) {
		this.lancamentoDto.setMes(mes);
		return this;
	}

	public LancamentoDTOBuilder status(StatusLancamento status) {
		this.lancamentoDto.setStatus(status.name());
		return this;
	}

	public LancamentoDTOBuilder valor(BigDecimal valor) {
		this.lancamentoDto.setValor(valor);
		return this;
	}

	public LancamentoDTOBuilder usuario(Long usuarioId) {
		this.lancamentoDto.setUsuario(usuarioId);
		return this;
	}
	
	public LancamentoDTOBuilder ano(Integer ano) {
		this.lancamentoDto.setAno(ano);
		return this;
	}

	public LancamentoDTO build() {
		return this.lancamentoDto;
	}
}