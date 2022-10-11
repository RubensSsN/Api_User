package br.com.pessoas.CadastroPessoasSpring.repository;

import br.com.pessoas.CadastroPessoasSpring.model.Pessoas;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PessoasRepository extends JpaRepository<Pessoas, Long> {

    //Está sendo responsável pela paginação, evitando assim a aplicação ter problemas por carregar e entregar todos os dados de uma vez só
    Page<Pessoas> findByNome(String nomePessoas, Pageable paginacao); // Está aqui para quando passarmos na URL o nome da pessoa como parametro encontrar a mesma e dar o GET.

    //Está buscando o cpf no banco de dados e verificando se o mesmo existe.
    String findByCpf(String cpfPessoas);
}
