package br.com.pessoas.CadastroPessoasSpring.Controller.dto;

import br.com.pessoas.CadastroPessoasSpring.model.Pessoas;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PessoaDTO {
    private Long id;
    private String nome;
    private int idade;
    private LocalDateTime dataCadastro;
    private String endereco;
    private String cpf;

    public PessoaDTO(Pessoas pessoas) {
        this.id = pessoas.getId();
        this.nome = pessoas.getNome();
        this.idade = pessoas.getIdade();
        this.dataCadastro = pessoas.getDataCadastro();
        this.endereco = pessoas.getEndereco();
        this.cpf = pessoas.getCpf();
    }

    public static Page<PessoaDTO> converter(Page<Pessoas> pessoas) {
        return pessoas.map(PessoaDTO::new);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return idade;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getCpf() {
        return cpf;
    }
}