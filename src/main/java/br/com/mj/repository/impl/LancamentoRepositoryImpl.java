package br.com.mj.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.com.mj.dto.LancamentoResumidoDTO;
import br.com.mj.model.Lancamento;
import br.com.mj.model.Lancamento_;
import br.com.mj.repository.filter.LancamentoFilter;
import br.com.mj.repository.queries.LancamentoRepositoryQuery;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager em;

	@Override
	public PageImpl<Lancamento> filtrar(LancamentoFilter filter, Pageable pageable) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Lancamento> cq = cb.createQuery(Lancamento.class);
		Root<Lancamento> root = cq.from(Lancamento.class);

		List<Predicate> listPredicates = getFiltro(filter, cb, root);
		Predicate[] predicates = listPredicates.toArray(new Predicate[listPredicates.size()]);

		CriteriaQuery<Lancamento> where = cq.select(root).where(predicates);

		TypedQuery<Lancamento> tq = em.createQuery(where);

		// adiciona a paginacao a criteria
		tq.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		tq.setMaxResults(pageable.getPageSize());

		return new PageImpl<>(tq.getResultList(), pageable, getTotal(predicates));
	}

	@Override
	public PageImpl<LancamentoResumidoDTO> filtrarResumido(LancamentoFilter filter, Pageable pageable) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<LancamentoResumidoDTO> cq = cb.createQuery(LancamentoResumidoDTO.class);
		Root<Lancamento> root = cq.from(Lancamento.class);

		CriteriaQuery<LancamentoResumidoDTO> select = cq
				.select(cb.construct(LancamentoResumidoDTO.class, root.get(Lancamento_.CODIGO), root.get(Lancamento_.DESCRICAO),
						root.get(Lancamento_.DATA_VENCIMENTO), root.get(Lancamento_.DATA_PAGAMENTO), root.get(Lancamento_.VALOR)));

		List<Predicate> listPredicates = getFiltro(filter, cb, root);
		Predicate[] predicates = listPredicates.toArray(new Predicate[listPredicates.size()]);

		CriteriaQuery<LancamentoResumidoDTO> query = select.where(predicates);

		TypedQuery<LancamentoResumidoDTO> tq = em.createQuery(query);

		// adiciona a paginacao a criteria
		tq.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		tq.setMaxResults(pageable.getPageSize());

		return new PageImpl<LancamentoResumidoDTO>(tq.getResultList(), pageable, getTotal(predicates));

	}

	private Long getTotal(Predicate[] predicates) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Lancamento> root = cq.from(Lancamento.class);

		cq.where(predicates);
		cq.select(cb.count(root));

		return em.createQuery(cq).getSingleResult();
	}

	private List<Predicate> getFiltro(LancamentoFilter filter, CriteriaBuilder cb, Root<Lancamento> root) {

		List<Predicate> predicates = new ArrayList<>();

		if (StringUtils.isNotEmpty(filter.getDescricao())) {
			predicates.add(cb.like(cb.lower(root.get(Lancamento_.DESCRICAO)), String.format("%%%s%%", filter.getDescricao().toLowerCase())));
		}

		if (filter.getDataVencimentoDe() != null) {
			predicates.add(cb.greaterThanOrEqualTo(root.get(Lancamento_.DATA_VENCIMENTO), filter.getDataVencimentoDe()));
		}

		if (filter.getDataVencimentoAte() != null) {
			predicates.add(cb.lessThanOrEqualTo(root.get(Lancamento_.DATA_VENCIMENTO), filter.getDataVencimentoAte()));
		}

		if (filter.getTipoLancamento() != null) {
			predicates.add(cb.equal(root.get(Lancamento_.TIPO_LANCAMENTO), filter.getTipoLancamento()));
		}

		if (filter.getDataPagamentoDe() != null) {
			predicates.add(cb.greaterThanOrEqualTo(root.get(Lancamento_.DATA_PAGAMENTO), filter.getDataPagamentoDe()));
		}

		if (filter.getDataPagamentoAte() != null) {
			predicates.add(cb.lessThanOrEqualTo(root.get(Lancamento_.DATA_PAGAMENTO), filter.getDataPagamentoAte()));
		}

		return predicates;
	}

}
