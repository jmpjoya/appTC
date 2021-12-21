package com.jmp.appTC.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TCambioOpes {

	private BigDecimal monto;
	private String monedaOrigen;
	private String monedaDestino;
}
