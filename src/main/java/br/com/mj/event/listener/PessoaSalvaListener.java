package br.com.mj.event.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import br.com.mj.event.PessoaSalvaEvent;

@Component
public class PessoaSalvaListener implements ApplicationListener<PessoaSalvaEvent> {

	@Override
	public void onApplicationEvent(PessoaSalvaEvent event) {
		System.out.println("Pessoa salva");
	}

}
