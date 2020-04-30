package br.com.mj.resource;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.mj.model.Pessoa;
import br.com.mj.repository.filter.PessoaFilter;
import br.com.mj.service.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource extends Resource {

	@Autowired
	private PessoaService pessoaService;

	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	@GetMapping
	public ResponseEntity<PageImpl<Pessoa>> buscar(PessoaFilter pessoaFilter, Pageable pageable) {
		PageImpl<Pessoa> pagePessoas = pessoaService.filtrar(pessoaFilter, pageable);
		return pagePessoas.getContent().isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pagePessoas);
	}

	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	@GetMapping("/todos")
	public ResponseEntity<List<Pessoa>> buscarTodos(PessoaFilter pessoaFilter) {
		List<Pessoa> pessoas = pessoaService.todos(pessoaFilter);
		return pessoas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pessoas);
	}
	
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	@PostMapping
	public ResponseEntity<Pessoa> nova(@Valid @RequestBody Pessoa pessoa) {
		pessoa = pessoaService.salvar(pessoa);
		return ResponseEntity.created(getLocationResource(pessoa.getCodigo())).body(pessoa);
	}

	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	@GetMapping("/{codigo}")
	public ResponseEntity<Pessoa> buscarPorCodigo(@PathVariable Long codigo) {
		return ResponseEntity.ok(pessoaService.buscar(codigo));
	}

	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	@PutMapping("/{codigo}")
	public ResponseEntity<Pessoa> atualizar(@PathVariable Long codigo, @Valid @RequestBody Pessoa pessoa) {
		return ResponseEntity.ok(pessoaService.atualizar(codigo, pessoa));
	}

	@PreAuthorize("hasAuthority('ROLE_REMOVER_PESSOA') and #oauth2.hasScope('write')")
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long codigo) {
		pessoaService.excluir(codigo);
	}

	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('write')")
	@GetMapping("/ativar/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable Long codigo) {
		pessoaService.ativar(codigo);
	}
}
