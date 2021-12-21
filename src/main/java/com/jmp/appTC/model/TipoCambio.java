package com.jmp.appTC.model;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity

public class TipoCambio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "monori_fk", referencedColumnName = "id")
	private Moneda monedaOrigen;
	@OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mondes_fk", referencedColumnName = "id")
	private Moneda monedaDestino;
	private BigDecimal cambio;
	
}
