package br.com.pessoas.CadastroPessoasSpring.Controller.form;

import br.com.pessoas.CadastroPessoasSpring.model.Pessoas;
import com.sun.istack.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class PessoasForm {
    @NotNull @NotEmpty
    private String nome;
    @NotNull @Min(0)
    private int idade;
    @NotNull @NotEmpty
    private String endereco;
    @CPF(message = "CPF INVÁLIDO!")
    @Column(unique = true)
    private String cpf;

    //Getters and Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Pessoas converter() {
        return new Pessoas(nome, idade, endereco, cpf);
    }
}
