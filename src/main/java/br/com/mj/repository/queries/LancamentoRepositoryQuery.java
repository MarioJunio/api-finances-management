package br.com.mj.repository.queries;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.com.mj.dto.LancamentoResumidoDTO;
import br.com.mj.model.Lancamento;
import br.com.mj.repository.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {

	public PageImpl<Lancamento> filtrar(LancamentoFilter filter, Pageable pageable);
	
	public PageImpl<LancamentoResumidoDTO> filtrarResumido(LancamentoFilter filter, Pageable pageable);

}
