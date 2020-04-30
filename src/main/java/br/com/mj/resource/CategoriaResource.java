package br.com.mj.resource;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.mj.model.Categoria;
import br.com.mj.repository.CategoriaRepository;
import br.com.mj.service.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource extends Resource {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private CategoriaService categoriaService;

	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	@GetMapping()
	public List<Categoria> listar() {
		return categoriaRepository.findAll();
	}

	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')")
	@PostMapping()
	public ResponseEntity<Categoria> nova(@RequestBody @Valid Categoria categoria) {
		categoria = categoriaRepository.save(categoria);
		return ResponseEntity.created(getLocationResource(categoria.getCodigo())).body(categoria);
	}

	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	@GetMapping("/{codigo}")
	public ResponseEntity<Categoria> buscar(@PathVariable Long codigo) {
		return ResponseEntity.ok(categoriaService.buscarPorCodigo(codigo));
	}

	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')")
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long codigo) {
		categoriaService.excluir(codigo);
	}

}
