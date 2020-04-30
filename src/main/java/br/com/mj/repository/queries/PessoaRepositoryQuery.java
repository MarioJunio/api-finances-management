package br.com.mj.repository.queries;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.com.mj.model.Pessoa;
import br.com.mj.repository.filter.PessoaFilter;

public interface PessoaRepositoryQuery {
	
	PageImpl<Pessoa> filtrar(PessoaFilter pessoaFilter, Pageable pageable);
	
	List<Pessoa> todos(PessoaFilter pessoaFilter);
}
