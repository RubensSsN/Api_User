package br.com.pessoas.CadastroPessoasSpring.config.validacao.security;

import br.com.pessoas.CadastroPessoasSpring.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    //Pega o tempo de expiração do token no application.properties
    @Value("${cadastropessoasjpa.jwt.expiration}")
    private String expiration;

    //Pega a senha que está na application.properties
    @Value("${cadastropessoasjpa.jwt.secret}")
    private String secret;

    public String gerarToken(Authentication authenticate) {
        //Pega o usuario que está logado
        Usuario logado = (Usuario) authenticate.getPrincipal();
        //Informa a data
        Date hoje = new Date();
        //Calcula a data que o token tem que ser expirado
        Date dataExpiration = new Date(hoje.getTime() + Long.parseLong(expiration));
        //É um método que cria um objeto builder onde posso setar informações para ele construir o token.
        return Jwts.builder()
                //Informa qual é a aplicação que está gerando o token
                .setIssuer("API de cadastro de pessoas")
                //Pega o id do usuário logado para saber quem é o dono dessa token e transofrma em String para compilar
                .setSubject(logado.getId().toString())
                //Informa quando o Token foi gerado
                .setIssuedAt(hoje)
                //Informa a data que o token tem que ser expirado
                .setExpiration(dataExpiration)
                //Informa o algoritmo de criptografia e a senha da aplicação
                .signWith(SignatureAlgorithm.HS256, secret)
                //Usamos isso para compactar tudo e transoformar em String
                .compact();
    }

    // Verificar se o token que chegou como parâmetro é valido ou não.
    // Quando eu fizer essa chamada, se o token estiver válido, ele devolve o objeto, se estiver inválido ou nulo, ele joga uma exception. Eu vou fazer um try/catch, colocando o código dentro do try. Se ele rodou tudo ok e chegou na linha de baixo é porque o token está válido, retorna true, porque não quero recuperar nenhuma informação do token nesse método. Se deu alguma exception, ele vai entrar no false.
    public boolean isTokenValido(String token) {
        try {
            //Usamos o jwts para verificar o token e o parser para passar um token o setSignKey com o parametro que pega a chave de criptografia e descriptografia que é a (this.secret) e o parseClaimsJws(token) Esse método devolve o Jws claims, que é um objeto onde consigo recuperar o token e as informações que setei dentro do token.
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token); //Assim
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getIdUsuario(String token) {

        Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody(); //Devolve o objeto do token em si por causa do .getBody no final.
        //Retorna o id do usuário.
        return Long.parseLong(claims.getSubject());
    }
}
