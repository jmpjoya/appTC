package com.jmp.appTC.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Builder
@Data

public class TipoCambioUpdate {

	private String monedaOrigen;
	private String monedaDestino;
	private BigDecimal tipoCambioNuevo;
	
}
