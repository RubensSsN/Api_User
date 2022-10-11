package br.com.pessoas.CadastroPessoasSpring.Controller;


import br.com.pessoas.CadastroPessoasSpring.Controller.dto.TokenDTO;
import br.com.pessoas.CadastroPessoasSpring.Controller.form.LoginForm;
import br.com.pessoas.CadastroPessoasSpring.config.validacao.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Profile("prod")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenDTO> autenticar(@RequestBody @Valid LoginForm form) {
        //Está pegando os dados do login e convertendo para não precisarmos dar new no controller.
        UsernamePasswordAuthenticationToken dadosLogin = form.converter();
        try {
            //Está fazendo a autenticação
            Authentication authentication = authManager.authenticate(dadosLogin);
            //Gerando o Token da pessoa autenticada que dura um tempo limitado
            String token = tokenService.gerarToken(authentication);
            //Retorna ok (Cod 200) se der tudo certo
            return ResponseEntity.ok(new TokenDTO(token, "Bearer"));

        }catch (AuthenticationException e) {
            return ResponseEntity.badRequest().build();
        }

    }

}
