package br.com.unip.controller;
 
 
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.unip.http.Pessoa;
import br.com.unip.repository.PessoaRepository;
import br.com.unip.repository.entity.PessoaEntity;
import lombok.extern.log4j.Log4j2;
 
@RestController
@RequestMapping(value = "/service")
@Log4j2
public class ServiceController {
 
	@Autowired
	private PessoaRepository repository;
 
	@PostMapping("/cadastrar")
	public String Cadastrar(@RequestBody Pessoa pessoa){
 
		PessoaEntity entity = new PessoaEntity();
 
		try {
 
			entity.setNome(pessoa.getNome());
			entity.setSexo(pessoa.getSexo());
 
			repository.save(entity);
			
			log.info("cadastro realizado!");
 
			return "Registro cadastrado com sucesso!";
 
		} catch (Exception e) {
 
			return "Erro ao cadastrar um registro " + e.getMessage();
		}
 
	}
 
	@PutMapping("/alterar")
	public String Alterar(@RequestBody Pessoa pessoa){
 
		PessoaEntity entity = new PessoaEntity();
 
		try {
 
			entity.setCodigo(pessoa.getCodigo());
			entity.setNome(pessoa.getNome());
			entity.setSexo(pessoa.getSexo());
 
			repository.save(entity);
			
			log.info("cadastro alterado!");
 
			return "Registro alterado com sucesso!";
 
		} catch (Exception e) {
 
			return "Erro ao alterar o registro " + e.getMessage();
 
		}
 
	}

	@GetMapping("/todasPessoas")
	public List<Pessoa> TodasPessoas(){
 
		List<Pessoa> pessoas =  new ArrayList<Pessoa>();
 
		List<PessoaEntity> listaEntityPessoas = repository.findAll();
 
		for (PessoaEntity entity : listaEntityPessoas) {
 
			pessoas.add(new Pessoa(entity.getCodigo(), entity.getNome(),entity.getSexo()));
		}
		
		log.info("listado com sucesso!");
 
		return pessoas;
	}
 
	@GetMapping("/getPessoa/{codigo}")
	public Pessoa GetPessoa(@PathVariable("codigo") Integer codigo){
 
		Optional<PessoaEntity> entity = repository.findById(codigo);
		
		log.info("listado com sucesso!");
 
		if(!entity.isEmpty() && entity != null)
			return new Pessoa(entity.orElse(null).getCodigo(), entity.orElse(null).getNome(),entity.orElse(null).getSexo());
 
		return null;
	}
 

	@DeleteMapping("/excluir/{codigo}")
	public String Excluir(@RequestParam("codigo") Integer codigo){
 
		try {
 
			repository.deleteById(codigo);
			
			log.info("registro removido!");
 
			return "Registro excluido com sucesso!";
 
		} catch (Exception e) {
 
			return "Erro ao excluir o registro! " + e.getMessage();
		}
 
	}
 
}