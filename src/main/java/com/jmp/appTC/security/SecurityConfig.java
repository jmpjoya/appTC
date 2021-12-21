package com.jmp.appTC.security;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	String[] resources = new String[] {"/include/**", "/css/**", "/img/**", "/js/**"};
	
	@Autowired
	UserDetailsService userDetailService;
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
	
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		try {
			http.csrf().disable().httpBasic().and().exceptionHandling().accessDeniedHandler(accessDeniedHandler()).authenticationEntryPoint(authenticationEntryPoint())
			.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			http.addFilterBefore(JWTFilterBean(), UsernamePasswordAuthenticationFilter.class);
		    http.headers().frameOptions().disable();
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	
	}
	
	
	/*
	 * @Bean public FilterRegistrationBean<MyFilterJWT> JWTFilterBean() throws
	 * Exception { FilterRegistrationBean<MyFilterJWT> filterRegistrationBean = new
	 * FilterRegistrationBean<>(); filterRegistrationBean.setFilter(new
	 * MyFilterJWT(this.authenticationManager()));
	 * filterRegistrationBean.setOrder(filterRegistrationBean.LOWEST_PRECEDENCE);
	 * LogManager.getLogManager().getLogger(this.getClass().getName()); return
	 * filterRegistrationBean; }
	 */
	
	@Bean
	public MyFilterJWT JWTFilterBean() throws Exception {
		return new MyFilterJWT(this.authenticationManager());
	}
	
	@Bean
	RestAccessDeniedHandler accessDeniedHandler() {
		return new RestAccessDeniedHandler();
	}

	@Bean
	RestAuthenticationEntryPoint authenticationEntryPoint() {
		return new RestAuthenticationEntryPoint();
	}

}
