package com.techbloom.smartrh.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techbloom.smartrh.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	public Optional<Usuario> findByUsuario(String usuario); // Query Methods: encontrar por usuario
	
}