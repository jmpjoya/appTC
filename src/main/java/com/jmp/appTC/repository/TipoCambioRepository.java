package com.jmp.appTC.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jmp.appTC.model.TipoCambio;

public interface TipoCambioRepository extends JpaRepository <TipoCambio, Long> {
	
	public Optional<TipoCambio> findByMonedaOrigenNomAbreviadoAndMonedaDestinoNomAbreviado(String monOrigen, String monDestino);

}