package br.com.mj.security.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.mj.model.Usuario;
import br.com.mj.repository.UsuarioRepository;
import br.com.mj.security.user.UsuarioSistema;

@Service
public class HibernateUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Optional<Usuario> opUsuario = usuarioRepository.findByEmail(email);
		Usuario usuario = opUsuario.orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));

		return new UsuarioSistema(usuario, getPermissoes(usuario));
	}

	private List<SimpleGrantedAuthority> getPermissoes(Usuario usuario) {
		return usuario.getPermissoes().stream().map(p -> new SimpleGrantedAuthority(p.getDescricao())).collect(Collectors.toList());
	}
}
