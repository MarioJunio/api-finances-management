package br.com.mj.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.com.mj.model.Endereco_;
import br.com.mj.model.Pessoa;
import br.com.mj.model.Pessoa_;
import br.com.mj.repository.filter.PessoaFilter;
import br.com.mj.repository.queries.PessoaRepositoryQuery;

public class PessoaRepositoryImpl implements PessoaRepositoryQuery {

	@Autowired
	private EntityManager em;

	@Override
	public List<Pessoa> todos(PessoaFilter pessoaFilter) {
		FiltrarTodosHolder holder = filtrarTodos(pessoaFilter);
		return holder.tq.getResultList();
	}

	@Override
	public PageImpl<Pessoa> filtrar(PessoaFilter pessoaFilter, Pageable pageable) {
		FiltrarTodosHolder holder = filtrarTodos(pessoaFilter);
		holder.tq.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		holder.tq.setMaxResults(pageable.getPageSize());

		return new PageImpl<Pessoa>(holder.tq.getResultList(), pageable, count(pessoaFilter, holder.predsArray));
	}

	private FiltrarTodosHolder filtrarTodos(PessoaFilter pessoaFilter) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Pessoa> cq = cb.createQuery(Pessoa.class);
		Root<Pessoa> root = cq.from(Pessoa.class);

		List<Predicate> predicates = getFiltro(pessoaFilter, cb, root);
		Predicate[] predsArray = predicates.toArray(new Predicate[predicates.size()]);

		// adiciona os filtros
		CriteriaQuery<Pessoa> where = cq.where(predsArray);

		// cria query para realizar a busca
		TypedQuery<Pessoa> tq = em.createQuery(where);

		// holder para retornar os objetos utilizados
		FiltrarTodosHolder holder = new FiltrarTodosHolder();
		holder.tq = tq;
		holder.predsArray = predsArray;

		return holder;

	}

	private long count(PessoaFilter pessoaFilter, Predicate[] predicates) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Pessoa> root = cq.from(Pessoa.class);

		// adiciona os filtros
		cq.where(predicates);
		cq.select(cb.count(root));

		return em.createQuery(cq).getSingleResult();
	}

	private List<Predicate> getFiltro(PessoaFilter pessoaFilter, CriteriaBuilder cb, Root<Pessoa> root) {
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (StringUtils.isNotEmpty(pessoaFilter.getNome())) {
			predicates.add(cb.like(cb.lower(root.get(Pessoa_.NOME)), String.format("%%%s%%", pessoaFilter.getNome().toLowerCase())));
		}

		if (pessoaFilter.getAtivo() != null) {
			predicates.add(cb.equal(root.get(Pessoa_.ATIVO), pessoaFilter.getAtivo()));
		}

		if (StringUtils.isNotEmpty(pessoaFilter.getLogradouro())) {
			predicates.add(cb.like(cb.lower(root.get(Pessoa_.ENDERECO).get(Endereco_.LOGRADOURO)),
					String.format("%%%s%%", pessoaFilter.getLogradouro().toLowerCase())));
		}

		if (pessoaFilter.getNumero() != null) {
			predicates.add(cb.equal(root.get(Pessoa_.ENDERECO).get(Endereco_.NUMERO), pessoaFilter.getNumero()));
		}

		if (StringUtils.isNotEmpty(pessoaFilter.getBairro())) {
			predicates.add(cb.like(cb.lower(root.get(Pessoa_.ENDERECO).get(Endereco_.BAIRRO)),
					String.format("%%%s%%", pessoaFilter.getBairro().toLowerCase())));
		}

		if (StringUtils.isNotEmpty(pessoaFilter.getCep())) {
			predicates.add(cb.equal(root.get(Pessoa_.ENDERECO).get(Endereco_.CEP), pessoaFilter.getCep()));
		}

		if (StringUtils.isNotEmpty(pessoaFilter.getCidade())) {
			predicates.add(cb.like(cb.lower(root.get(Pessoa_.ENDERECO).get(Endereco_.CIDADE)),
					String.format("%%%s%%", pessoaFilter.getCidade().toLowerCase())));
		}

		if (StringUtils.isNotEmpty(pessoaFilter.getEstado())) {
			predicates.add(cb.like(cb.lower(root.get(Pessoa_.ENDERECO).get(Endereco_.ESTADO)),
					String.format("%%%s%%", pessoaFilter.getEstado().toLowerCase())));
		}

		if (StringUtils.isNotEmpty(pessoaFilter.getComplemento())) {
			predicates.add(cb.like(cb.lower(root.get(Pessoa_.ENDERECO).get(Endereco_.COMPLEMENTO)),
					String.format("%%%s%%", pessoaFilter.getComplemento().toLowerCase())));
		}

		return predicates;
	}

	static class FiltrarTodosHolder {
		TypedQuery<Pessoa> tq;
		Predicate[] predsArray;
	}

}
