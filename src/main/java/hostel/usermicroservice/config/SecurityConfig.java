package hostel.usermicroservice.config;


import hostel.usermicroservice.jwt.JwtRequestFilter;
import hostel.usermicroservice.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserServiceImpl userService;
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(c -> c
                        .requestMatchers("/users/edit").authenticated()
                        .requestMatchers("/wallet").authenticated()
                        .requestMatchers("/users/profile").authenticated()
                        .requestMatchers("/sessions/current").authenticated()
                        .requestMatchers("/sessions/byactive/{active}").authenticated()
                        .requestMatchers("/sessions/getall").authenticated()
                        .requestMatchers("/sessions/{id}").authenticated()
                        .requestMatchers("/transfers/hesoyam").authenticated()
                        .requestMatchers("/transfers/casino").authenticated()
                        .requestMatchers("/transfers/roulette/{min}/{max}/{number}").authenticated()
                        .requestMatchers("/transfers/viaphone").authenticated()
                        .requestMatchers("/transfers/viaid").authenticated()
                        .requestMatchers("/transfers/viainvoice").authenticated()
                        .requestMatchers("/transfers/getall").authenticated()
                        .requestMatchers("/transfers/getviarecipientid/{id}").authenticated()
                        .requestMatchers("/transfers/getviamywallet").authenticated()
                        .requestMatchers("/transfers/getviatype/{type}").authenticated()
                        .requestMatchers("/transfers/getHistory/{type}").authenticated()
                        .requestMatchers("/sessions/logout").authenticated()
                        .requestMatchers("/invoices").authenticated()
                        .requestMatchers("/invoices/getall").authenticated()
                        .requestMatchers("/invoices/cancel/{id}").authenticated()
                        .requestMatchers("/invoices/getallincoming").authenticated()
                        .requestMatchers("/invoices/getalloutgoing").authenticated()
                        .requestMatchers("/invoices/{id}").authenticated()
                        .requestMatchers("/invoices/last").authenticated()
                        .requestMatchers("/invoices/first").authenticated()
                        .requestMatchers("/invoices/gettotal").authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(c -> c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
