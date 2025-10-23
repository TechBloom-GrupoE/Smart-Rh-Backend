package com.techbloom.smartrh.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.techbloom.smartrh.model.Colaborador;
import com.techbloom.smartrh.service.ColaboradorService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/colaboradores")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ColaboradorController {
	
	// Injeção de depedencia colaboradorService
	@Autowired
	private ColaboradorService colaboradorService;

	// Buscar todos os colaboradores
	@GetMapping
	public ResponseEntity<List<Colaborador>> getAll() {
		return ResponseEntity.ok(colaboradorService.getAll());
	}

	// Buscar colaborador por ID
	@GetMapping("/{id}")
	public ResponseEntity<Colaborador> getById(@PathVariable Long id) {
		return colaboradorService.getById(id);
	}
	
	// Buscar colaborador por nome
	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Colaborador>> getByNome(@PathVariable String nome) {
		return ResponseEntity.ok(colaboradorService.getByNome(nome));
	}

	// Cadastrar colaborador
	@PostMapping
	public ResponseEntity<Colaborador> post(@Valid @RequestBody Colaborador colaborador) {
		return colaboradorService.post(colaborador);
	}
	
	// Atualizar colaborador
	@PutMapping
	public ResponseEntity<Colaborador> put(@Valid @RequestBody Colaborador colaborador) {
		return colaboradorService.put(colaborador);
	}

	// Deletar colaborador
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		colaboradorService.delete(id);
	}
	
}
