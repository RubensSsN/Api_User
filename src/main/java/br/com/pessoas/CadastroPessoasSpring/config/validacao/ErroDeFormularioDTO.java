package br.com.pessoas.CadastroPessoasSpring.config.validacao;

public class ErroDeFormularioDTO {

    /* Para o erro neste caso queremos somente o campo e erro então declaramos e geramos o constructor and getters. */
    private  String campo;
    private  String erro;

    public ErroDeFormularioDTO(String campo, String erro) {
        this.campo = campo;
        this.erro = erro;
    }

    public String getCampo() {
        return campo;
    }

    public String getErro() {
        return erro;
    }
}
