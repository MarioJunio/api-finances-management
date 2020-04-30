package br.com.mj.resource;

import java.net.URI;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public abstract class Resource {

	protected URI getLocationResource(Long codigo) {
		return ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}").buildAndExpand(codigo).toUri();
	}

}
