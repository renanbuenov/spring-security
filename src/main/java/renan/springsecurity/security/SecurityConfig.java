package renan.springsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

import static renan.springsecurity.security.UserRole.*;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                .csrf().disable()
                .authorizeRequests() //Autorizar as requisições.
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()  //Permitir todas as request que estão definidas entre parênteses.
                .antMatchers("/api/**").hasRole(STUDENT.name())
                .anyRequest()       //Qualquer requisição
                .authenticated()     //No qual devem ser autenticadas
                .and()               //E...
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .defaultSuccessUrl("/courses", true)
                .and()
                .rememberMe()
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                    .key("algomuitoseguro")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me", "Idea-4637f4b6", "XSRF-TOKEN")
                    .logoutSuccessUrl("/login");        //E o mecanismo de autenticação da página será o básico (janela pop-up).
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() { //Método que irá especificar username e password do user.
        UserDetails renanBueno = User.builder()
                .username("renan")
                .password(passwordEncoder.encode("1234"))
//                .roles(ADMIN.name())
                .authorities(ADMIN.getGrantedAuthorities())
                .build();
        UserDetails fernandaMarie = User.builder()
                .username("fernanda")
                .password(passwordEncoder.encode("fernanda"))
//                .roles(STUDENT.name())
                .authorities(ADMIN_TRAINEE.getGrantedAuthorities())
                .build();
        UserDetails rafaelBueno = User.builder()
                .username("rafael")
                .password(passwordEncoder.encode("fernanda"))
//                .roles(ADMIN_TRAINEE.name())
                .authorities(STUDENT.getGrantedAuthorities())
                .build();
        return new InMemoryUserDetailsManager(
                renanBueno,
                fernandaMarie,
                rafaelBueno); //Retorna o user "construído".
    }
}
