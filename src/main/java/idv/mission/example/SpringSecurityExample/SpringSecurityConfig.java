package idv.mission.example.SpringSecurityExample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	AccessDeniedHandler accessDeniedHandler;

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("mission").password("123456").roles("USER");
		auth.inMemoryAuthentication().withUser("root").password("123456").roles("ADMIN");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {		
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers("/").permitAll()
			.antMatchers("/403").permitAll()
			.antMatchers("/login").permitAll()
			.antMatchers("/jump").permitAll()
			.antMatchers("/jumped").permitAll()
			.antMatchers("/test").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
			.anyRequest().authenticated()	// 加這行，所有request都需要權限，不然SpringBoot會直接回答找不到，報錯
			.and().formLogin().loginPage("/login").defaultSuccessUrl("/success/logined", true)
			.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
			.and().exceptionHandling().accessDeniedHandler(accessDeniedHandler);
	}

}