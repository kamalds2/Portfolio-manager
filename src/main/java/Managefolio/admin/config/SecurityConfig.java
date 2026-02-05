package Managefolio.admin.config;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import Managefolio.admin.services.CustomUserDetailsService;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(loginDebugFilter(), UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                .requestMatchers("/login", "/forgot-password", "/uploads/**").permitAll()
                .requestMatchers("/api/keepalive").authenticated()
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/admin/users/**", "/admin/profile/view/**", "/admin/profile/delete/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/dashboard", "/admin/dashboard", "/admin/profile/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .requestMatchers("/admin/skills/**", "/admin/projects/**", "/admin/education/**", "/admin/jobs/**", "/admin/about/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .requestMatchers("/admin/portfolio/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .requestMatchers(HttpMethod.GET, "/portfolio/**").permitAll()
                .requestMatchers("/portfolio/profiles/*/upload-image", "/portfolio/profiles/*/upload-resume").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionUrl("/login?expired")
                .maximumSessions(1) // Allow only 1 session per user
                .maxSessionsPreventsLogin(false) // Don't prevent login, just invalidate old sessions
            )
            .userDetailsService(customUserDetailsService)
            .csrf().disable();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Filter loginDebugFilter() {
        return (request, response, chain) -> {
            if (request instanceof HttpServletRequest req &&
                "/login".equals(req.getRequestURI()) &&
                "POST".equalsIgnoreCase(req.getMethod())) {

                System.out.println("🔐 Login Attempt:");
                System.out.println("Username: " + req.getParameter("username"));
                System.out.println("Password: " + req.getParameter("password")); // ⚠️ Avoid in production
            }
            chain.doFilter(request, response);
        };
    }
}