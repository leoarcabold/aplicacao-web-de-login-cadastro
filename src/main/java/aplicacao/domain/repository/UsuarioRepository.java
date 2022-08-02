package aplicacao.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aplicacao.domain.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	List<Usuario> findTodosByNomeContaining(String nome);

	Optional<Usuario> findByEmail(String email);

	boolean existsByNome(String email);
}
