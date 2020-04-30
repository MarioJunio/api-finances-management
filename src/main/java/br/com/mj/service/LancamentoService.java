package br.com.mj.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.mj.dto.LancamentoResumidoDTO;
import br.com.mj.exception.PessoaInativaOuInexistenteException;
import br.com.mj.model.Lancamento;
import br.com.mj.model.Pessoa;
import br.com.mj.repository.LancamentoRepository;
import br.com.mj.repository.PessoaRepository;
import br.com.mj.repository.filter.LancamentoFilter;
import br.com.mj.service.util.NullAwareBeanUtilsBean;

@Service
public class LancamentoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	public PageImpl<Lancamento> buscarTodos(LancamentoFilter filter, Pageable pageable) {
		return lancamentoRepository.filtrar(filter, pageable);
	}

	public PageImpl<LancamentoResumidoDTO> buscarResumido(LancamentoFilter filter, Pageable pageable) {
		return lancamentoRepository.filtrarResumido(filter, pageable);
	}

	public Lancamento buscar(Long codigo) {
		Optional<Lancamento> opLancamento = lancamentoRepository.findById(codigo);

		if (!opLancamento.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}

		return opLancamento.get();
	}

	public Lancamento novo(Lancamento lancamento) {
		Optional<Pessoa> opPessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo());

		// se a pessoa não existir ou estiver inativa sera lancado a excecao
		if (!opPessoa.isPresent() || !opPessoa.get().getAtivo()) {
			throw new PessoaInativaOuInexistenteException();
		}

		return lancamentoRepository.save(lancamento);
	}

	public void delete(Lancamento lancamento) {
		lancamentoRepository.delete(lancamento);
	}

	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		Lancamento original = buscar(codigo);

		try {
			// copia as propriedades
			new NullAwareBeanUtilsBean().copyProperties(original, lancamento);

			// salva o lancamento atualizado
			original = lancamentoRepository.save(original);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return original;
	}
}
