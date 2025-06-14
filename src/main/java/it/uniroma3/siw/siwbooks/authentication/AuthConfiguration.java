package it.uniroma3.siw.siwbooks.authentication;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import static it.uniroma3.siw.siwbooks.model.Credentials.*;

@Configuration
@EnableWebSecurity
public class AuthConfiguration {

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .authoritiesByUsernameQuery("SELECT username, role from credentials WHERE username=?")
                .usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain configure(final HttpSecurity httpSecurity)
            throws Exception{
        httpSecurity
                .csrf().and().cors().disable()
                .authorizeHttpRequests()

                // pagine e risorse su cui tutti possono fare GET
                .requestMatchers(HttpMethod.GET,"/","/login","/register","/index","/author/**","/authors","/search","/book/**","/books","/css/**", "/images/**", "favicon.ico").permitAll()
                // pagine e risorse su cui tutti possono fare POST
                .requestMatchers(HttpMethod.POST,"/search","/register","/login").permitAll()

                // pagine e risorse su cui solo gli ADMIN possono fare GET
                .requestMatchers(HttpMethod.GET,"/admin/**").hasAnyAuthority(ADMIN_ROLE)
                // pagine e risorse su cui solo gli ADMIN possono fare POST
                .requestMatchers(HttpMethod.POST,"/admin/**").hasAnyAuthority(ADMIN_ROLE)

                // pagine non elencate sopra richiedono auth (autenticato come ADMIN o come DEFAULT)
                .anyRequest().authenticated()

                // se utente tenta di accedere a contenuto per cui non ha permesso viene reindirizzato qui
                .and()
                .exceptionHandling()
                .accessDeniedPage("/accessDenied")

                // LOGIN:
                .and().formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/success",true)
                .failureUrl("/login?error=true")

                // LOGOUT:
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .clearAuthentication(true).permitAll();
        return httpSecurity.build();
    }
}
