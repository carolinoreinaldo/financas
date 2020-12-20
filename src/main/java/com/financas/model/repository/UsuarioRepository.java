package com.financas.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.financas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	//Optional quer dizer que o usuário pode existir ou não
	//aqui também o Spring data já vai gera o SQL, ou seja,
	//não precisa implementar o sql
	Optional<Usuario> findByEmail(String email);
	
	boolean existsByEmail(String email);
}