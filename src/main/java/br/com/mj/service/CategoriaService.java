package br.com.mj.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.mj.model.Categoria;
import br.com.mj.repository.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;

	public void excluir(Long codigo) {
		categoriaRepository.delete(buscarPorCodigo(codigo));
	}

	public Categoria buscarPorCodigo(Long codigo) {
		Optional<Categoria> opCategoria = categoriaRepository.findById(codigo);

		if (!opCategoria.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}

		return opCategoria.get();
	}

}
