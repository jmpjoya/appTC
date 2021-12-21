package com.jmp.appTC.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jmp.appTC.model.ErrorMsg;
import com.jmp.appTC.security.JWTServiceAuth0;
import com.jmp.appTC.model.ErrorMsg;
import com.jmp.appTC.dto.TCambioOpes;
import com.jmp.appTC.dto.TipoCambioUpdate;
import com.jmp.appTC.service.MonedaService;
import com.jmp.appTC.service.TCambioOpesService;
import com.jmp.appTC.service.TiposCambioService;

import rx.schedulers.Schedulers;

@PreAuthorize("authenticated")
@RestController
@RequestMapping("/tcambio")
//@CrossOrigin("http://localhost:8082/")

public class MonedaController {
	
	@Autowired
	private TCambioOpesService tCambioOpesService;
	@Autowired
	private TiposCambioService tipoCambioService;
	@Autowired
	private MonedaService monedaService;
	
	@Autowired
	private JWTServiceAuth0 lJwtService;
	
	private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MonedaController.class);
	
	// realiza el tipo de cambio
	@PostMapping("/cambiar")
	public ResponseEntity<?> registrarTipoCambio(final @RequestBody TCambioOpes tCambioOpes) {
		try {
			logger.info("Ingreso a  /tcambio/cambiar ", tCambioOpes);
			return tCambioOpesService.registraTCambioOpe(tCambioOpes).subscribeOn(Schedulers.io()).map(
	            s -> ResponseEntity.created(URI.create("/tcambio")).body(s)).toBlocking().value();
		}
		catch(Exception ex) {
			logger.error("Error en /tcambio/cambiar en Request Body: {} con error: {} ", tCambioOpes, ex.getMessage());
			return ResponseEntity.badRequest().body(new ErrorMsg(ex, "/tcambio/cambiar"));
		}
	}
	
	@PostMapping("/actualizar")
	public ResponseEntity<?> getTipoCambioMonto(final @RequestBody TipoCambioUpdate tipoCambioUpdate) {
		try {
			logger.info("Put EN /tcambio/actualizar ", tipoCambioUpdate);
			return tipoCambioService.updTipoCambio(tipoCambioUpdate).subscribeOn(Schedulers.io()).map(
	            s -> ResponseEntity.ok().body(s)).toBlocking().value();
		}
		catch(Exception ex) {
			logger.error("Error en /tcambio/actualizar en Request Body: {} con error: {}", tipoCambioUpdate, ex.getMessage());
			return ResponseEntity.badRequest().body(new ErrorMsg(ex, "/tcambio/monedas"));
		}
	}
	
	
	
	@GetMapping("/monedas")
	public ResponseEntity<?> getMonedas() {
		try {
			logger.info("GET origen tcambio/monedas");
			return monedaService.getMonedasRegistradas().subscribeOn(Schedulers.io()).map(
	            s -> ResponseEntity.ok().body(s)).toBlocking().value();
		}
		catch(Exception ex) {
			logger.error("Error en request tcambio/token with error: {}", ex.getMessage());
			return ResponseEntity.badRequest().body(new ErrorMsg(ex, "tcambio/token"));
		}
	}
	
	@PostMapping("/token")
	public ResponseEntity<?> getToken(final @AuthenticationPrincipal User activeUser) {
		logger.info("Get Request received for tcambio/token");
		try {
			List<String> authorities = new ArrayList<>();
			for (GrantedAuthority role : activeUser.getAuthorities()) 
				authorities.add(role.getAuthority());	
			HashMap<String, String> hashMap = new HashMap();
			hashMap.put("token", lJwtService.createToken(activeUser.getUsername(), authorities));
			return ResponseEntity.ok(hashMap);
		} catch (Exception ex) {
			logger.error("Error on request received for tcambio/token with error: {}", ex.getMessage());
			return ResponseEntity.badRequest().body(new ErrorMsg(ex, "tcambio/token"));
		}
	}
	
		

}
