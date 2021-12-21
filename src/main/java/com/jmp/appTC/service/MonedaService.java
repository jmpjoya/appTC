package com.jmp.appTC.service;

import com.jmp.appTC.model.Moneda;
import com.jmp.appTC.repository.*;

import rx.Single;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
@Transactional
public class MonedaService {

	@Autowired
	private MonedaRepository monedaRepository;

	public Single<Moneda> getMonedaXNombreAbreviado(String monedaOrigen) {
		return Single.create(singleSubscriber -> {
			Optional<Moneda> rptMoneda = monedaRepository.findByNomAbreviado(monedaOrigen);
			if (rptMoneda.isPresent()) 
				singleSubscriber.onSuccess(rptMoneda.get());
			else
				singleSubscriber.onError(new EntityNotFoundException("ERROR: "+ monedaOrigen+" No Existe"));
		}); 
	}
	
	public Single<List<Moneda>> getMonedasRegistradas() {
		return Single.create(singleSubscriber -> {
			try {
				List<Moneda> listCoin = monedaRepository.findAll();
				singleSubscriber.onSuccess(listCoin);
			}
			catch (Exception ex) {
				singleSubscriber.onError(new EntityNotFoundException("NO MONEDAS REGISTRADAS "+ex.getMessage()));
			}
		}); 
	}
	
}
