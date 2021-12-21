package com.jmp.appTC.security;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.jmp.appTC.model.ErrorMsg;
import com.fasterxml.jackson.databind.json.JsonMapper;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
	private static final String DESCRIPTION = "Acceso Denegado al Recurso";
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		ErrorMsg errormessage = new ErrorMsg(accessDeniedException, DESCRIPTION);
        OutputStream out = response.getOutputStream();
        JsonMapper mapper = new JsonMapper();
        mapper.writeValue(out, errormessage);
        out.flush();
	}
}
