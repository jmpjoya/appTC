package com.jmp.appTC.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Builder
@Data

public class TipoCambioUpdateRpta {
	
	private Long id;
	private String monedaOrigen;
	private String monedaDestino;
	private BigDecimal tipocambionuevo;
	private BigDecimal tipocambioantiguo;
	
	

}
