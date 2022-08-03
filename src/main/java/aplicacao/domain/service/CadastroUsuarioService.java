package aplicacao.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import aplicacao.domain.exception.EntidadeEmUsoException;
import aplicacao.domain.exception.EntidadeNaoEncontradaException;
import aplicacao.domain.model.Usuario;
import aplicacao.domain.repository.UsuarioRepository;

@Service
public class CadastroUsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public Usuario adicionar(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

	public void remover(Long usuarioId) {
		try {
			usuarioRepository.deleteById(usuarioId);

		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe um cadastro de usuário com código %d", usuarioId));

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format("Usuário de código %d não pode ser removido", usuarioId));
		}
	}
}
