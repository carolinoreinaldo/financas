package com.financas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableWebMvc
public class FinancasApplication implements WebMvcConfigurer {
	
	/*
	 * Esse método resove o problema de cross-origin permitindo
	 * conexões de outras urls ( origens )
	 * 
	 * Aqui você pode configurar de onde sua aplicação pode receber
	 * pedidos de conexão
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedMethods("GET","POST","PUT","DELETE","OPTIONS");
	}

	public static void main(String[] args) {
		SpringApplication.run(FinancasApplication.class, args);
	}

}
