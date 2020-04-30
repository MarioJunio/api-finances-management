package br.com.mj.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.mj.event.PessoaSalvaEvent;
import br.com.mj.model.Pessoa;
import br.com.mj.repository.PessoaRepository;
import br.com.mj.repository.filter.PessoaFilter;

@Service
@Transactional
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	public List<Pessoa> todos(PessoaFilter pessoaFilter) {
		return pessoaRepository.todos(pessoaFilter);
	}

	public Pessoa salvar(Pessoa pessoa) {
		pessoa = pessoaRepository.save(pessoa);

		PessoaSalvaEvent pessoaSalvaEvent = new PessoaSalvaEvent(pessoa);
		eventPublisher.publishEvent(pessoaSalvaEvent);

		return pessoa;
	}

	public Pessoa atualizar(Long codigo, Pessoa pessoa) {
		Pessoa pessoaOriginal = buscar(codigo);
		BeanUtils.copyProperties(pessoa, pessoaOriginal);

		return salvar(pessoaOriginal);
	}

	public Pessoa buscar(Long codigo) {
		Optional<Pessoa> opPessoa = pessoaRepository.findById(codigo);

		if (!opPessoa.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}

		return opPessoa.get();
	}

	public void excluir(Long codigo) {
		pessoaRepository.deleteById(codigo);
	}

	public PageImpl<Pessoa> filtrar(PessoaFilter pessoaFilter, Pageable pageable) {
		return pessoaRepository.filtrar(pessoaFilter, pageable);
	}

	public void ativar(Long id) {
		Pessoa pessoa = buscar(id);
		pessoa.setAtivo(!pessoa.getAtivo());
	}


}
