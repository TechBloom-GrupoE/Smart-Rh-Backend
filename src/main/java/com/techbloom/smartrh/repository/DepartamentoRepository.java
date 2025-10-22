package com.techbloom.smartrh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.techbloom.smartrh.model.Departamento;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

	public List<Departamento> findAllByDescricaoContainingIgnoreCase(@Param("descricao") String descricao); //Query Methods: encontrar por descrição

}