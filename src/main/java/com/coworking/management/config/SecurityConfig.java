package com.coworking.management.config;

import com.coworking.management.service.OurUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private OurUserDetailsService ourUserDetailsService;

    @Autowired
    private JWTAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(request -> request.requestMatchers("/auth/**", "/public/**").permitAll()
                		.requestMatchers("/uploads/**").permitAll()
                		// Autoriser les requêtes OPTIONS pour le CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Admin : Accès complet
                        .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")

                        // Coworker : Gestion des réservations et des événements
                        .requestMatchers("/coworker/reservations/**").hasAnyAuthority("COWORKER")
                        .requestMatchers("/coworker/events/**").hasAnyAuthority("COWORKER")

                        // Entreprise : Gestion des réservations et des abonnements
                        .requestMatchers("/entreprise/reservations/**").hasAnyAuthority("ENTREPRISE")
                        .requestMatchers("/entreprise/subscriptions/**").hasAnyAuthority("ENTREPRISE")

                        // Réceptionniste : Gestion des réservations et des arrivées/départs
                        .requestMatchers("/receptionniste/reservations/**").hasAnyAuthority("RECEPTIONNISTE")
                        .requestMatchers("/receptionniste/checkin-checkout/**").hasAnyAuthority("RECEPTIONNISTE")

                        // Comptable : Gestion des paiements et des rapports
                        .requestMatchers("/comptable/payments/**").hasAnyAuthority("COMPTABLE")
                        .requestMatchers("/comptable/reports/**").hasAnyAuthority("COMPTABLE")

                        // Espaces : Lecture pour tous les rôles, modification pour l'admin
                        .requestMatchers(HttpMethod.GET, "/espaces/**").hasAnyAuthority("ADMIN", "COWORKER", "ENTREPRISE", "RECEPTIONNISTE", "COMPTABLE")
                        .requestMatchers(HttpMethod.POST, "/espaces/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/espaces/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/espaces/**").hasAnyAuthority("ADMIN")

                        // Événements : Lecture pour tous les rôles, modification pour l'admin
                        .requestMatchers(HttpMethod.GET, "/events/**").hasAnyAuthority("ADMIN", "COWORKER", "ENTREPRISE", "RECEPTIONNISTE", "COMPTABLE")
                        .requestMatchers(HttpMethod.POST, "/events/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/events/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/events/**").hasAnyAuthority("ADMIN")
                        
                        // Reviews
                        .requestMatchers(HttpMethod.POST, "/reviews/").hasAnyAuthority("ADMIN", "COWORKER", "ENTREPRISE")
                        .requestMatchers(HttpMethod.GET, "/reviews/espace/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/reviews/pending").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/reviews/approve/**").hasAuthority("ADMIN")
                        
                         // Pricing
                        .requestMatchers(HttpMethod.GET, "/pricing/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/events/register").hasAnyAuthority("COWORKER", "ENTREPRISE")
                        .requestMatchers(HttpMethod.POST, "/events/cancel-registration").hasAnyAuthority("COWORKER", "ENTREPRISE")
                        
                        .requestMatchers("/entreprise/team-subscriptions/**").hasAuthority("ENTREPRISE")
                        .requestMatchers("/receptionniste/checkin/**").hasAuthority("RECEPTIONNISTE")
                        .requestMatchers("/receptionniste/walkin/**").hasAuthority("RECEPTIONNISTE")
                        .requestMatchers("/comptable/payments/**").hasAuthority("COMPTABLE")

                        // Profil utilisateur : Accès pour tous les rôles
                        .requestMatchers("/adminuser/**").hasAnyAuthority("ADMIN", "COWORKER", "ENTREPRISE", "RECEPTIONNISTE", "COMPTABLE")

                        // Toutes les autres requêtes nécessitent une authentification
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtAuthFilter, UsernamePasswordAuthenticationFilter.class
                );
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(ourUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}