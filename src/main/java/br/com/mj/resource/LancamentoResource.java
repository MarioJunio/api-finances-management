package br.com.mj.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mj.dto.LancamentoResumidoDTO;
import br.com.mj.exception.PessoaInativaOuInexistenteException;
import br.com.mj.exception.handler.HandlerError;
import br.com.mj.model.Lancamento;
import br.com.mj.repository.filter.LancamentoFilter;
import br.com.mj.service.LancamentoService;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource extends Resource {

	@Autowired
	private LancamentoService lancamentoService;

	@Autowired
	private MessageSource messages;

	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	@GetMapping
	public ResponseEntity<PageImpl<Lancamento>> todos(LancamentoFilter filter, Pageable pageable) {
		PageImpl<Lancamento> lancamentos = lancamentoService.buscarTodos(filter, pageable);
		return lancamentos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(lancamentos);
	}

	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	@GetMapping("/resumido")
	public ResponseEntity<PageImpl<LancamentoResumidoDTO>> todosResumido(LancamentoFilter filter, Pageable pageable) {
		PageImpl<LancamentoResumidoDTO> lancamentos = lancamentoService.buscarResumido(filter, pageable);
		return lancamentos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(lancamentos);
	}

	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	@GetMapping("/{codigo}")
	public ResponseEntity<Lancamento> buscar(@PathVariable Long codigo) {
		return ResponseEntity.ok(lancamentoService.buscar(codigo));
	}

	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	@PostMapping
	public ResponseEntity<Lancamento> novo(@Valid @RequestBody Lancamento lancamento) {
		System.out.println(lancamento);
		lancamento = lancamentoService.novo(lancamento);
		return ResponseEntity.created(getLocationResource(lancamento.getCodigo())).body(lancamento);
	}
	
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	@PutMapping("/{codigo}")
	public ResponseEntity<Lancamento> atualizar(@PathVariable Long codigo, @RequestBody Lancamento lancamento) {
		lancamento = lancamentoService.atualizar(codigo, lancamento);
		return ResponseEntity.ok(lancamento);
	}

	@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('write')")
	@DeleteMapping("/{codigo}")
	public void excluir(@PathVariable Long codigo) {
		Lancamento lancamento = lancamentoService.buscar(codigo);
		lancamentoService.delete(lancamento);
	}

	@ExceptionHandler(PessoaInativaOuInexistenteException.class)
	public ResponseEntity<Object> handlerPessoaInativaOuInexistenteException(PessoaInativaOuInexistenteException ex) {
		String mensagemUsuario = messages.getMessage("requisicao.pessoa-inativa-inexistente", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();

		return ResponseEntity.badRequest().body(new HandlerError(mensagemUsuario, mensagemDesenvolvedor));
	}
}
