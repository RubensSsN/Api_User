package br.com.pessoas.CadastroPessoasSpring.config.validacao.security;

import br.com.pessoas.CadastroPessoasSpring.model.Usuario;
import br.com.pessoas.CadastroPessoasSpring.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    // A senha no spring é feita a consulta por mémoria, então só precisamos passar o email que ele já busca a senha na memória
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Puxa o email que foi informado pela pessoa
        Optional<Usuario> usuario = repository.findByEmail(username);
        // Se o username(email) informado pela pessoa for encontrado pega no Banco de dados e retorna o mesmo.
        if (usuario.isPresent()) {
            return usuario.get();
        }
        throw new UsernameNotFoundException("Usuário não encontrado!");
    }
}
