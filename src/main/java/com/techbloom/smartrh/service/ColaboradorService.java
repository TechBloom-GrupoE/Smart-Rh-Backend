package com.techbloom.smartrh.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.techbloom.smartrh.model.Colaborador;
import com.techbloom.smartrh.repository.ColaboradorRepository;
import com.techbloom.smartrh.repository.DepartamentoRepository;

import jakarta.validation.Valid;

@Service
public class ColaboradorService {

	// injeção de dependência dos repositórios
	@Autowired
	private ColaboradorRepository colaboradorRepository;

	@Autowired
	private DepartamentoRepository departamentoRepository;

	// Buscar todos os colaboradores
	public List<Colaborador> getAll() {
		return colaboradorRepository.findAll();

	}

	// Buscar colaborador por id
	public ResponseEntity<Colaborador> getById(Long id) {
		return colaboradorRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	// Buscar colaborador por nome
	public List<Colaborador> getByNome(String nome) {
		return colaboradorRepository.findAllByNomeContainingIgnoreCase(nome);
	}

	// Cadastrar novo colaborador
	public ResponseEntity<Colaborador> post(@Valid Colaborador colaborador) {
		if (departamentoRepository.existsById(colaborador.getDepartamento().getId()))
			return ResponseEntity.status(HttpStatus.CREATED).body(colaboradorRepository.save(colaborador));

		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Departamento não existe!", null);
	}

	// Atualizar colaborador
	public ResponseEntity<Colaborador> put(@Valid Colaborador colaborador) {
		if (colaboradorRepository.existsById(colaborador.getId())) {

			if (departamentoRepository.existsById(colaborador.getDepartamento().getId()))
				return ResponseEntity.status(HttpStatus.OK).body(colaboradorRepository.save(colaborador));

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Departamento não existe!", null);
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	// Deletar colaborador
	public void delete(Long id) {
		Optional<Colaborador> colaborador = colaboradorRepository.findById(id);

		if (colaborador.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		colaboradorRepository.deleteById(id);
	}
}
