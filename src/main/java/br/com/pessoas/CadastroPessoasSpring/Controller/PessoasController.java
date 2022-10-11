package br.com.pessoas.CadastroPessoasSpring.Controller;

import br.com.pessoas.CadastroPessoasSpring.Controller.dto.PessoaDTO;
import br.com.pessoas.CadastroPessoasSpring.Controller.form.AtualizaPessoasForm;
import br.com.pessoas.CadastroPessoasSpring.Controller.form.PessoasForm;
import br.com.pessoas.CadastroPessoasSpring.model.Pessoas;
import br.com.pessoas.CadastroPessoasSpring.repository.PessoasRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pessoas")
public class PessoasController {

    @Autowired
    private PessoasRepository pessoasRepository;


    @GetMapping
    //Faz a requisição GET fique salva em memoria com o "id" de "listaDePessoas"
    @Cacheable(value = "listaDePessoas")
    public Page<PessoaDTO> lista(@RequestParam(required = false) String nomePessoas,@PageableDefault(sort = "id", direction = Sort.Direction.ASC, page = 0, size = 10) Pageable paginacao) {

        if(nomePessoas == null) {
            Page<Pessoas> pessoa = pessoasRepository.findAll(paginacao);
            return PessoaDTO.converter(pessoa);
        }   else {
            Page<Pessoas> pessoa = pessoasRepository.findByNome(nomePessoas, paginacao);
            return PessoaDTO.converter(pessoa);
        }
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "listaDePessoas", allEntries = true) //Se fizermos qualquer alteração é para esquecermos o cache do Listar e fazer o select novamente || Allentries é para esquecer todas as linhas do cache
    public ResponseEntity<PessoaDTO> cadastrar(@RequestBody @Valid PessoasForm form, UriComponentsBuilder uriBuilder) {
        Pessoas pessoas = form.converter();
        String cpfsV = pessoasRepository.findByCpf(pessoas.getCpf());
        if (cpfsV != null) {
            throw new RuntimeException("CPF já cadastrado!");
        } else {
            pessoasRepository.save(pessoas);
            URI uri = uriBuilder.path("/pessoas/{id}").buildAndExpand(pessoas.getId()).toUri();
            return ResponseEntity.created(uri).body(new PessoaDTO(pessoas));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaDTO> detalhar(@PathVariable Long id) {
        Optional<Pessoas> pessoas = pessoasRepository.findById(id);
        if (pessoas.isPresent()) {
            return ResponseEntity.ok(new PessoaDTO(pessoas.get()));
        }

        // Retorna a exception (404) not found se não encontrar o id
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaDePessoas", allEntries = true) //Se fizermos qualquer alteração é para esquecermos o cache do Listar e fazer o select novamente || Allentries é para esquecer todas as linhas do cache
    public ResponseEntity<PessoaDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizaPessoasForm form) {

        Optional<Pessoas> optional = pessoasRepository.findById(id);
        if (optional.isPresent()) {
            Pessoas pessoas = form.atualizar(id, pessoasRepository);
            return ResponseEntity.ok(new PessoaDTO(pessoas));
        }

        // Retorna a exception (404) not found se não encontrar o id
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    @SecurityRequirement(name = "bearer-key") //Swagger
    @CacheEvict(value = "listaDePessoas", allEntries = true) //Se fizermos qualquer alteração é para esquecermos o cache do Listar e fazer o select novamente || Allentries é para esquecer todas as linhas do cache
    public ResponseEntity<?> deletar(@PathVariable Long id) {

        Optional<Pessoas> pessoas = pessoasRepository.findById(id);
        if (pessoas.isPresent()) {
            // Pega o método deleteByID da interface de PessoasRepository e faz a exclusão
            pessoasRepository.deleteById(id);
            // Para darmos um retorno (200) mesmo sendo delete
            return ResponseEntity.ok().build();
        }
        // Retorna a exception (404) not found se não encontrar o id
        return ResponseEntity.notFound().build();
    }

}
