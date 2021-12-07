package renan.springsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

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
                .authorizeRequests() //Autorizar as requisições.
                .antMatchers("/", "index", "/cs/*", "/js/*")  //Permitir todas as request que estão
                .permitAll()                                            //definidas entre parênteses.
                .anyRequest()        //Qualquer requisição
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
                .roles("STUDENT")
                .build();
        UserDetails fernandaMarie = User.builder()
                .username("fernanda")
                .password(passwordEncoder.encode("fernanda"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(
                renanBueno,
                fernandaMarie); //Retorna o user "construído", mas sem o encodar a senha.
    }
}
