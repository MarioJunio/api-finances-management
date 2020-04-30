package br.com.mj.event;

import org.springframework.context.ApplicationEvent;

import br.com.mj.model.Pessoa;

public class PessoaSalvaEvent extends ApplicationEvent {

	private Pessoa pessoa;

	public PessoaSalvaEvent(Pessoa pessoa) {
		super(pessoa);
		this.pessoa = pessoa;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

}
