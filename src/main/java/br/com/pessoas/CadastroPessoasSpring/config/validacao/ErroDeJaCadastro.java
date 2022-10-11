package br.com.pessoas.CadastroPessoasSpring.config.validacao;

public class ErroDeJaCadastro {

    //Gera a mensagem do erro

    private String mensagem;

    public ErroDeJaCadastro(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
