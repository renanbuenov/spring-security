package renan.springsecurity.student;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
}
