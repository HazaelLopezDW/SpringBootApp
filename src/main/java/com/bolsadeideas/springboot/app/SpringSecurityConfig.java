package com.bolsadeideas.springboot.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class SpringSecurityConfig {

	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService() throws Exception {

		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("jhon").password(passwordEncoder().encode("12345")).roles("USER").build());

		manager.createUser(
				User.withUsername("admin").password(passwordEncoder().encode("admin")).roles("ADMIN", "USER").build());

		return manager;
	}


	@Bean
	MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
		return new MvcRequestMatcher.Builder(introspector);
	}


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
		http.authorizeHttpRequests((authz) -> {
			try {
				authz.requestMatchers(mvc.pattern("/"), mvc.pattern("/css/**"), mvc.pattern("/js/**"),
						mvc.pattern("/images/**"), mvc.pattern("/listar")).permitAll()
						.requestMatchers(mvc.pattern("/ver/**")).hasAnyRole("USER")
						.requestMatchers(mvc.pattern("/uploads/**")).hasAnyRole("USER")
						.requestMatchers(mvc.pattern("/form/**")).hasRole("ADMIN")
						.requestMatchers(mvc.pattern("/eliminar/**")).hasRole("ADMIN")
						.requestMatchers(mvc.pattern("/factura/**")).hasRole("ADMIN").anyRequest().authenticated()
						.and()
						.formLogin().loginPage("/login")
						.permitAll()
						.and()
						.logout().permitAll();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return http.build();
	}
}