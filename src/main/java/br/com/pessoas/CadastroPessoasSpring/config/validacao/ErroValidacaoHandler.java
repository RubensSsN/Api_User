package br.com.pessoas.CadastroPessoasSpring.config.validacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ErroValidacaoHandler {

    @Autowired
    private MessageSource messageSource; //Faz possivel pegarmos as mensagens de erro e usaremos a internacionalização

    @ResponseStatus(code = HttpStatus.BAD_REQUEST) //Impede que seja retornada a exception 200 por termos tratado e informa que é para continuar com o erro de formulario que é a exceptions 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErroDeFormularioDTO> handle(MethodArgumentNotValidException exception) { // Se espera a devolução de algo quando temos uma exception e para isso usamos o List<ErroDeFormularioDTO> fazendo assim uma lista ser retornada do tipo DTO.
        // Cria uma ArrayList do tipo dto
        List<ErroDeFormularioDTO> dto = new ArrayList<>();

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        //Faz a mensagem ser gerada a cada erro
        fieldErrors.forEach(e -> {
            String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());  //Pega a mensagem e traduz de acordo com o idioma do local da pessoa
            ErroDeFormularioDTO erro = new ErroDeFormularioDTO(e.getField(), mensagem);
            //Adicionando ao dto os erros para serem listados
            dto.add(erro);
        });

        return dto;
    }

}
