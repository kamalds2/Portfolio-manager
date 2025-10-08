package Managefolio.admin.config;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import Managefolio.admin.services.CustomUserDetailsService;

@Configuration
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
                // 🔐 Admin-only routes
                .requestMatchers("/admin/users/**", "/admin/profile", "/admin/profile/view/**", "/admin/profile/delete/**").hasAuthority("ROLE_ADMIN")

                // 👤 Shared routes (accessible by both roles)
                .requestMatchers("/dashboard", "/admin/dashboard").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .requestMatchers("/admin/profile/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .requestMatchers("/admin/skills/**", "/admin/projects/**", "/admin/education/**", "/admin/jobs/**", "/admin/about/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")

                // Public routes
                .requestMatchers("/login", "/forgot-password", "/css/**", "/js/**", "/images/**").permitAll()

                // All other routes require authentication
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true) // ✅ shared dashboard for both roles
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
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
    public UserDetailsService userDetailsService() {
        return customUserDetailsService;
    }

    @Bean
    public Filter loginDebugFilter() {
        return (request, response, chain) -> {
            if (request instanceof HttpServletRequest req &&
                "/login".equals(req.getRequestURI()) &&
                "POST".equalsIgnoreCase(req.getMethod())) {

                String username = req.getParameter("username");
                String password = req.getParameter("password");

                System.out.println("🔐 Login Attempt:");
                System.out.println("Username: " + username);
                System.out.println("Password: " + password); // ⚠️ Don't log this in production
            }
            chain.doFilter(request, response);
        };
    }
}