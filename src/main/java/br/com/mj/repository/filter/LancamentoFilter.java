package br.com.mj.repository.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.mj.model.TipoLancamento;

public class LancamentoFilter {

	private String descricao;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataVencimentoDe;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataVencimentoAte;
	
	private TipoLancamento tipoLancamento;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataPagamentoDe;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataPagamentoAte;
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getDataVencimentoDe() {
		return dataVencimentoDe;
	}

	public void setDataVencimentoDe(LocalDate dataVencimentoDe) {
		this.dataVencimentoDe = dataVencimentoDe;
	}

	public LocalDate getDataVencimentoAte() {
		return dataVencimentoAte;
	}

	public void setDataVencimentoAte(LocalDate dataVencimentoAte) {
		this.dataVencimentoAte = dataVencimentoAte;
	}

	public TipoLancamento getTipoLancamento() {
		return tipoLancamento;
	}

	public void setTipoLancamento(TipoLancamento tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

	public LocalDate getDataPagamentoDe() {
		return dataPagamentoDe;
	}

	public void setDataPagamentoDe(LocalDate dataPagamentoDe) {
		this.dataPagamentoDe = dataPagamentoDe;
	}

	public LocalDate getDataPagamentoAte() {
		return dataPagamentoAte;
	}

	public void setDataPagamentoAte(LocalDate dataPagamentoAte) {
		this.dataPagamentoAte = dataPagamentoAte;
	}

}
