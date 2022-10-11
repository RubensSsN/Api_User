package br.com.pessoas.CadastroPessoasSpring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@EnableSpringDataWebSupport
@EnableCaching
public class CadastroPessoasSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(CadastroPessoasSpringApplication.class, args);
	}

}