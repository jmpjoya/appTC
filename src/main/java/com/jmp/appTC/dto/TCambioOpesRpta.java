package com.jmp.appTC.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TCambioOpesRpta {
	
	private BigDecimal montoInicial;
	private BigDecimal montoCambiado;
	private String monedaOrigen;
	private String monedaDestino;
	private BigDecimal tipoCambio;

}
