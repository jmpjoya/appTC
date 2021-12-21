package com.jmp.appTC.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jmp.appTC.model.Moneda;


public interface MonedaRepository extends JpaRepository <Moneda, Long> {
	
	Optional<Moneda> findByNomAbreviado(String nombreAbreviado);
}
