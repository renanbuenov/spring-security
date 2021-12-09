package renan.springsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static renan.springsecurity.security.UserPermission.*;
import static renan.springsecurity.security.UserRole.*;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests() //Autorizar as requisições.
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()  //Permitir todas as request que estão definidas entre parênteses.
                .antMatchers("/api/**").hasRole(STUDENT.name())
                .antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(COURSE_WRITE.getPermissions())  //A ordem dos "antMatchers" importa. Muito importante colocar na ordem correta para não ter sobrescrita de autorizações.
                .antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(COURSE_WRITE.getPermissions())
                .antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(COURSE_WRITE.getPermissions())
                .antMatchers(HttpMethod.GET,"/management/api/**").hasAnyRole(ADMIN.name(), ADMIN_TRAINE.name())
                .anyRequest()       //Qualquer requisição
                .authenticated()     //No qual devem ser autenticadas
                .and()               //E...
                .httpBasic();        //E o mecanismo de autenticação da página será o básico (janela pop-up).
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
                .authorities(ADMIN_TRAINE.getGrantedAuthorities())
                .build();
        UserDetails rafaelBueno = User.builder()
                .username("rafael")
                .password(passwordEncoder.encode("fernanda"))
//                .roles(ADMIN_TRAINE.name())
                .authorities(STUDENT.getGrantedAuthorities())
                .build();
        return new InMemoryUserDetailsManager(
                renanBueno,
                fernandaMarie,
                rafaelBueno); //Retorna o user "construído".
    }
}
