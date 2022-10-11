package br.com.pessoas.CadastroPessoasSpring.config.validacao.security;

import br.com.pessoas.CadastroPessoasSpring.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Cria o método de segurança que por estar @Configuration ele será lido ao iniciar a aplicação
@EnableWebSecurity
@Configuration
@Profile("dev")
public class DevSecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //Cuida das requisiçoes de autorização.
        http.authorizeRequests()
                //Está permitindo com que todos mesmo sem login consigam listar com ou sem filtro.
                .antMatchers(HttpMethod.GET, "/**").permitAll();
        // Retorna para a url requerida se a pessoa for autenticada
        return http.build();
    }

}
