package com.jmp.appTC.service;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jmp.appTC.dto.TCambioOpes;
import com.jmp.appTC.dto.TCambioOpesRpta;
import com.jmp.appTC.model.Moneda;
import com.jmp.appTC.model.TCambioOpe;
import com.jmp.appTC.model.TipoCambio;
import com.jmp.appTC.repository.TCambioOpesRepository;

import rx.Single;

@Service
@Transactional
public class TCambioOpesService {
	
	@Autowired
	private TCambioOpesRepository tCambioOpesRepository;
	@Autowired
	private MonedaService monedaService;
	@Autowired
	private TiposCambioService tipoCambioService;
	
	private BigDecimal montoInicial;
	
	public Single<TCambioOpesRpta> registraTCambioOpe(final TCambioOpes tCambioOpes) {
		return Single.create(singleSubscriber -> {
			Single<Moneda> singleMonOrigen = monedaService.getMonedaXNombreAbreviado(tCambioOpes.getMonedaOrigen());
			Single<Moneda> singleMonDestino = monedaService.getMonedaXNombreAbreviado(tCambioOpes.getMonedaDestino());
			montoInicial = tCambioOpes.getMonto();
			
			TCambioOpesRpta tCambioOpeRes = Single.zip(singleMonOrigen, singleMonDestino, this::getTipoCambio).toBlocking().value();
			singleSubscriber.onSuccess(tCambioOpeRes);
			singleSubscriber.onError(new Exception("ERROR"));
		});
	}

	private TCambioOpesRpta getTipoCambio(Moneda monOrigen, Moneda monDestino) {
		try {
			TipoCambio tipoCambio = tipoCambioService.getTipoCambioXMoneda(monOrigen.getNomAbreviado(), monDestino.getNomAbreviado())
					.toBlocking().value();
			BigDecimal tipoCambioMonto = tipoCambio.getCambio();
			//guarda operacion 
			tCambioOpesRepository.save(
					TCambioOpe.builder().monedaOrigen(monOrigen)
					.monedaDestino(monDestino).tipoCambio(tipoCambioMonto)
					.montoInicial(montoInicial).montoCambiado(montoInicial.multiply(tipoCambioMonto))
					.operationDate(LocalDateTime.now()).build());
			return TCambioOpesRpta.builder()
					.monedaOrigen(monOrigen.getNomAbreviado())
					.monedaDestino(monDestino.getNomAbreviado())
					.tipoCambio(tipoCambioMonto).montoInicial(montoInicial)
					.montoCambiado(montoInicial.multiply(tipoCambioMonto))
					.build();
		}
		catch(Exception ex) {
			throw ex;
		}
			
	}

	

}
