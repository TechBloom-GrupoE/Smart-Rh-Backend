package com.techbloom.smartrh.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.techbloom.smartrh.model.Departamento;
import com.techbloom.smartrh.repository.DepartamentoRepository;

import jakarta.validation.Valid;

@Service
public class DepartamentoService {

	// injeção de dependência do repositório
	@Autowired
	private DepartamentoRepository departamentoRepository;

	// Buscar todos os departamentos
	public List<Departamento> getAll() {
		return departamentoRepository.findAll();
	}

	// Buscar departamento por id
	public Optional<Departamento> getById(Long id) {
		return departamentoRepository.findById(id);

	}

	// Buscar departamento por descrição
	public List<Departamento> getByDescricao(String descricao) {
		return departamentoRepository.findAllByDescricaoContainingIgnoreCase(descricao);
	}

	// Cadastrar novo departamento
	public Departamento post(@Valid Departamento departamento) {
		return departamentoRepository.save(departamento);
	}

	// Atualizar departamento
	public Departamento put(@Valid Departamento departamento) {
		return departamentoRepository.findById(departamento.getId())
				.map(resposta -> departamentoRepository.save(departamento))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Departamento não encontrado"));
	}

	// Deletar departamento
	public void delete(Long id) {
		Optional<Departamento> departamento = departamentoRepository.findById(id);

		if (departamento.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Departamento não encontrado");

		departamentoRepository.deleteById(id);
	}
}