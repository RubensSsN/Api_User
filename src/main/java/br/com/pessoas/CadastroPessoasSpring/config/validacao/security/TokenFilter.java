package br.com.pessoas.CadastroPessoasSpring.config.validacao.security;

import br.com.pessoas.CadastroPessoasSpring.model.Usuario;
import br.com.pessoas.CadastroPessoasSpring.repository.UsuarioRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Faz a filtragem do token do usuario e pega este token e leva para a validação a cada requisição feita pelo user por causa de ser uma autenticação STATELESS
public class TokenFilter  extends OncePerRequestFilter {

    private TokenService tokenService;

    private UsuarioRepository repository;

    public TokenFilter(TokenService tokenService, UsuarioRepository repository) {
        this.tokenService = tokenService;
        this.repository = repository;
    }

    @Override
    //Está filtrando o token cujo o usuário atual está passando pelo request.
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //Recupera o token para realizar a autenticação, pois a cada requisição é necessário já que escolhemos ser stateless
        String token = recuperarToken(request);
        //Recebe um token e retorna um boolean dizendo se está valido o token ou não.
        boolean valido = tokenService.isTokenValido(token);
        //Se o token tiver valido ele vai autenticar o usuario
        if(valido) {
            autenticarCliente(token);
        }
        //Serve para falar para o spring que já foi rodado o que deveria e para seguir o fluxo da requisição
        filterChain.doFilter(request, response);
    }

    //Método que autentica o cliente que passou o token válido.
    private void autenticarCliente(String token) {
        //Pega o id do usuario de dentro do token e assim torna possível pegarmos o objeto (usuário) de dentro do banco de dados
        Long idUsuario = tokenService.getIdUsuario(token);
        //Retorna o objeto usuario pelo id
        Usuario usuario = repository.findById(idUsuario).get();
        //Pegando os dados do usuario e os perfis de acesso dele.
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        //Considera que o usuário está autenticado.
        SecurityContextHolder.getContext().setAuthentication(authentication /* O (authentication) Informa os dados do usuario a ser considerado! */);
    }

    private String recuperarToken(HttpServletRequest request) {
        //Pega o token por via do cabeçalho Authorization e verifica se está vazio, nulo ou não vem no método Bearer de autenticação.
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
            return null;
        }
        //Se o token estiver correto ele retorna o token da 7 casa em diante para tirar o "Bearer ".
        return token.substring(7, token.length());
    }
}
