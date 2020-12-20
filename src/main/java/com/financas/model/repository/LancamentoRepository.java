package com.financas.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.financas.model.entity.Lancamento;
import com.financas.model.enums.StatusLancamento;
import com.financas.model.enums.TipoLancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

	/*
	 * JPQL
	 */
	@Query( value = ""  
			+ " select "
			+ "		sum(l.valor) "
			+ " from Lancamento l "
			+ " join l.usuario u "
			+ " where u.id = :idUsuario "
			+ " and l.tipo = :tipo "
			+ " and l.status = :status "
			+ " group by u ")
	BigDecimal obterSaldoPorTipoLancamentoEUsuarioEStatus( 
			@Param("idUsuario") Long idUsuario, 
			@Param("tipo") TipoLancamento tipoLancamento, 
			@Param("status") StatusLancamento status );
}
