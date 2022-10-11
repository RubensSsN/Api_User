package br.com.pessoas.CadastroPessoasSpring.Controller.form;

import br.com.pessoas.CadastroPessoasSpring.model.Pessoas;
import br.com.pessoas.CadastroPessoasSpring.repository.PessoasRepository;
import com.sun.istack.NotNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class AtualizaPessoasForm {

    @NotNull @NotEmpty
    private String nome;

    @NotNull @NotEmpty @Length(min = 2)
    private String endereco;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Pessoas atualizar(Long id, PessoasRepository pessoasRepository) {
        Pessoas pessoas = pessoasRepository.getReferenceById(id);
        pessoas.setNome(this.nome);
        pessoas.setEndereco(this.endereco);

        return pessoas;
    }
}
