package br.com.pessoas.CadastroPessoasSpring.config.validacao.security;

import br.com.pessoas.CadastroPessoasSpring.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
@Profile("prod")
public class SecurityConfiguration{

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Bean
    //Método que contem a configuração de autentificação.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //Cuida das requisiçoes de autorização.
        http.authorizeRequests()
                //Está permitindo com que todos mesmo sem login consigam listar com ou sem filtro.
                .antMatchers(HttpMethod.GET, "/pessoas").permitAll()
                .antMatchers(HttpMethod.GET, "/pessoas").permitAll()
                .antMatchers(HttpMethod.GET, "/pessoas/*").permitAll()
                .antMatchers(HttpMethod.POST, "/auth").permitAll()
                .antMatchers(HttpMethod.GET, "/").permitAll()
                // Se dispararmos uma requisição GET para "/actuator", ele vai devolver um JSON com algumas informações sobre a aplicação.
                .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                //Permitindo acesso aos end-points da documentação.
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                //Informa que só quem pode dar o delete em alguma pessoa é um MODERADOR.
                .antMatchers(HttpMethod.DELETE, "/pessoas/*").hasRole("MODERADOR")
                //Informa que qualquer outra requisição fora as outras citadas acima precisam de autenticação.
                .anyRequest().authenticated()
                //Desabilita o csrf, pois é bom para testes e com o meio de segurança que estamos usando não é nessário deixa-lo ativado.
                .and().csrf().disable()
                //Está informando que o spring não é para ter sessões para cada usuario e sim que vai ser STATELESS para não ocupar muita memória
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //Adiciona o filtro que tem que ser feito antes de rodar que no caso o tokenfilter vai rodar antes da autenticação do usuario sempre que acontecer cada requisição.
                .and().addFilterBefore(new TokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class);
        // Retorna para a url requerida se a pessoa for autenticada
        return http.build();
    }

    //Pega a senha e transforma ela em uma senha criptografada
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Configuração dos recursos estáticos, permite que esses end-points sejam acessados
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");
    }

}
