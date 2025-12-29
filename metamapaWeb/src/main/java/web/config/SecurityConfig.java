package web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/admin/**").authenticated()
//                        .requestMatchers("/css/**", "/js/**", "/img/**", "/", "/verColeccion/**", "/navegarColecciones/**", "/reportarSuceso/**").permitAll()
//                        .anyRequest().permitAll()
//                )
//                .oauth2Login(oauth2 -> oauth2
//                        // usa la URL por defecto de Spring para iniciar login con Google
//                        .loginPage("/oauth2/authorization/google")
//                        .defaultSuccessUrl("/admin", true)
//                )
//                .logout(logout -> logout
//                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                        .logoutSuccessUrl("/")
//                        .permitAll()
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers("/static/**", "/webjars/**");
//    }
//}

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthProvider customAuthProvider;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    public SecurityConfig(CustomAuthProvider customAuthProvider,
                          OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) {
        this.customAuthProvider = customAuthProvider;
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                new AntPathRequestMatcher("/api/auth/**"),
                                new AntPathRequestMatcher("/admin/**")
                        )
                )
                .authenticationProvider(customAuthProvider)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/login", "/api/auth/login").permitAll()
                        .requestMatchers("/admin/**", "/panelDeControl/**").authenticated()
                        .requestMatchers("/perfil").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/api/auth/login")
                        .loginProcessingUrl("/api/auth/login")
                        .defaultSuccessUrl("/?login=success", true)
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")              // tu página de login con botón de Google
                        .successHandler(oAuth2LoginSuccessHandler) // acá se engancha el POST al CORE
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }
}
