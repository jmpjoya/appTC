package com.jmp.appTC.service;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmp.appTC.dto.TipoCambioUpdate;
import com.jmp.appTC.dto.TipoCambioUpdateRpta;
import com.jmp.appTC.model.Moneda;
import com.jmp.appTC.model.TipoCambio;
import com.jmp.appTC.repository.*;
import rx.Single;


@Service
@Transactional

public class TiposCambioService {
	@Autowired
	private TipoCambioRepository tipoCambioRepository;
	@Autowired
	private MonedaService monedaService;
	private BigDecimal tipoCambioNuevo;
	private BigDecimal tipoCambioOld;
	
	public Single<TipoCambio> getTipoCambioXMoneda(String monOrigen, String monDestino) {
		return Single.create(singleSubscriber -> {
			Optional<TipoCambio> resTipoCambio = tipoCambioRepository.findByMonedaOrigenNomAbreviadoAndMonedaDestinoNomAbreviado(monOrigen, monDestino);
			if (resTipoCambio.isPresent()) 
				singleSubscriber.onSuccess(resTipoCambio.get());
			else
				singleSubscriber.onError(new EntityNotFoundException("Excepcion " + monOrigen + " a "+ monDestino +" No existe"));
		}); 
	}
	
	public Single<TipoCambioUpdateRpta> updTipoCambio(TipoCambioUpdate tipoCambioUpdate) {
		return Single.create(singleSubscriber -> {
			tipoCambioNuevo = tipoCambioUpdate.getTipoCambioNuevo();
			Single<Moneda> singleMonedaOri = monedaService.getMonedaXNombreAbreviado(tipoCambioUpdate.getMonedaOrigen());
			Single<Moneda> singleMonedaDes = monedaService.getMonedaXNombreAbreviado(tipoCambioUpdate.getMonedaDestino());
			TipoCambioUpdateRpta tipoCambioUpdateRpta = Single.zip(singleMonedaOri, singleMonedaDes, this::updTipoCambio).toBlocking().value();
			singleSubscriber.onSuccess(tipoCambioUpdateRpta);
			singleSubscriber.onError(new Exception("Error General en el Sistema"));
		});
	}
	
	public Single<List<TipoCambioUpdateRpta>> buscarTodosTipoCambio() {
		return Single.create(singleSubscriber -> {
			List<TipoCambioUpdateRpta> listExchangeRate = tipoCambioRepository.findAll().
					parallelStream().map(tipoCambio -> {
								return TipoCambioUpdateRpta.builder().id(tipoCambio.getId())
										.monedaOrigen(tipoCambio.getMonedaOrigen().getNomAbreviado())
										.monedaDestino(tipoCambio.getMonedaDestino().getNomAbreviado())
										.build();
					}).collect(Collectors.toList());
			singleSubscriber.onSuccess(listExchangeRate);
			singleSubscriber.onError(new Exception("Error "));
		});
	}
	
	private TipoCambioUpdateRpta updTipoCambio(Moneda monedaOrigen, Moneda monedaDestino) {
		try {
			
			TipoCambio tipoCambio = this.getTipoCambioXMoneda(monedaOrigen.getNomAbreviado(), monedaDestino.getNomAbreviado())
					.toBlocking().value();
			tipoCambioOld = tipoCambio.getCambio();
			tipoCambio.setCambio(tipoCambioNuevo);
			tipoCambioRepository.save(tipoCambio);
			return TipoCambioUpdateRpta.builder()
					.id(tipoCambio.getId())
					.monedaOrigen(monedaOrigen.getNomAbreviado())
					.monedaDestino(monedaDestino.getNomAbreviado())
					.tipocambionuevo(tipoCambioNuevo)
					.tipocambioantiguo(tipoCambioOld).build();
		}
		catch(Exception ex) {
			throw ex;
		}
			
	}
	
			
	}

	
