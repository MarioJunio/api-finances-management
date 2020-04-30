package br.com.mj;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorSenha {
	
	public static void main(String[] args) {
		BCryptPasswordEncoder hasheador = new BCryptPasswordEncoder();
		
		System.out.println(hasheador.encode("teste"));
	}
	
}
