package aplicacao.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import aplicacao.domain.exception.EntidadeEmUsoException;
import aplicacao.domain.exception.EntidadeNaoEncontradaException;
import aplicacao.domain.model.Usuario;
import aplicacao.domain.repository.UsuarioRepository;
import aplicacao.domain.service.CadastroUsuarioService;

@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;

	@GetMapping
	public List<Usuario> listar() {
		return usuarioRepository.findAll();
	}

	@GetMapping("/{usuarioEmail}")
	public ResponseEntity<Usuario> buscar(@PathVariable String usuarioEmail) {
		Optional<Usuario> usuario = usuarioRepository.findByEmail(usuarioEmail);
		if (usuario.isPresent()) {
			return ResponseEntity.ok(usuario.get());
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Usuario adicionar(@RequestBody Usuario usuario) {
		return cadastroUsuarioService.adicionar(usuario);
	}

	@PutMapping("/{usuarioId}")
	public ResponseEntity<Usuario> atualizar(@PathVariable Long usuarioId, @RequestBody Usuario usuario) {
		Optional<Usuario> usuarioAtual = usuarioRepository.findById(usuarioId);

		if (usuarioAtual.isPresent()) {
			BeanUtils.copyProperties(usuario, usuarioAtual.get(), "id");

			Usuario usuarioSalvo = usuarioRepository.save(usuarioAtual.get());
			return ResponseEntity.ok(usuarioSalvo);
		}

		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{usuarioId}")
	public ResponseEntity<?> remover(@PathVariable Long usuarioId) {
		try {
			cadastroUsuarioService.remover(usuarioId);	
			return ResponseEntity.noContent().build();
			
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
			
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(e.getMessage());
		}
	}

}
