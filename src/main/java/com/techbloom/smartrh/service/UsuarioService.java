package com.techbloom.smartrh.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.techbloom.smartrh.model.Usuario;
import com.techbloom.smartrh.repository.UsuarioRepository;

@Service
public class UsuarioService {

	// injeção de dependência do repositório
	@Autowired
	private UsuarioRepository usuarioRepository;

	// Em condigos futuros lembrar de colocar jwt de autenticação para senhas

	// Buscar todos os usuarios
	public List<Usuario> getAll() {
		return usuarioRepository.findAll();
	}

	// Buscar usuario por id
	public Optional<Usuario> getById(Long id) {
		return usuarioRepository.findById(id);

	}
    // Buscar usuario por nome de usuário
	public Optional<Usuario> cadastrarUsuario(Usuario usuario) {

		if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) {
			return Optional.empty();
		}

		usuario.setId(null);

		return Optional.of(usuarioRepository.save(usuario));
	}

	// Atualizar usuario
	public Optional<Usuario> atualizarUsuario(Usuario usuario) {

		if (!usuarioRepository.findById(usuario.getId()).isPresent()) {
			return Optional.empty();
		}

		Optional<Usuario> usuarioExistente = usuarioRepository.findByUsuario(usuario.getUsuario());

		if (usuarioExistente.isPresent() && !usuarioExistente.get().getId().equals(usuario.getId())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);
		}

		return Optional.of(usuarioRepository.save(usuario));
	}

}