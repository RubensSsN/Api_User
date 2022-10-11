package br.com.pessoas.CadastroPessoasSpring.repository;

import br.com.pessoas.CadastroPessoasSpring.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    //Método para encontrar o email do usuario
    Optional<Usuario> findByEmail(String email);

}
