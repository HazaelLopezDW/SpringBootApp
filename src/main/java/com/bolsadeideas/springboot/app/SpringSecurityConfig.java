package com.bolsadeideas.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.bolsadeideas.springboot.app.auth.handler.LoginSuccesHandler;


@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig {
	
	@Autowired
	private LoginSuccesHandler successHandler;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	

	@Bean
	public UserDetailsService userDetailsService() throws Exception {

		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("jhon")
				.password(passwordEncoder.encode("12345")).roles("USER").build());

		manager.createUser(
				User.withUsername("admin")
					.password(passwordEncoder.encode("admin")).roles("ADMIN", "USER").build());

		return manager;
	}


	@Bean
	MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
		return new MvcRequestMatcher.Builder(introspector);
	}


	@Bean
	public SecurityFilterChain securityFilterChain(
			HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
		http.authorizeHttpRequests((authz) -> {
			try {
				authz.requestMatchers(
						mvc.pattern("/"), mvc.pattern("/css/**"), mvc.pattern("/js/**"),
						mvc.pattern("/images/**"), mvc.pattern("/listar")).permitAll()
						/*.requestMatchers(mvc.pattern("/ver/**")).hasAnyRole("USER")
						.requestMatchers(mvc.pattern("/uploads/**")).hasAnyRole("USER")
						.requestMatchers(mvc.pattern("/form/**")).hasRole("ADMIN")
						.requestMatchers(mvc.pattern("/eliminar/**")).hasRole("ADMIN")
						.requestMatchers(mvc.pattern("/factura/**")).hasRole("ADMIN")*/
						.anyRequest().authenticated()
						.and()
							.formLogin()
								.successHandler(successHandler)
								.loginPage("/login")
							.permitAll()
						.and()
						.logout().permitAll()
						.and()
						.exceptionHandling().accessDeniedPage("/error_403");
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return http.build();
	}
}