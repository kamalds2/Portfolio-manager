package Managefolio.admin.services;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import Managefolio.admin.model.User;
import Managefolio.admin.repository.UserRepository;
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        System.out.println("🧩 DB User Fetched:");
        System.out.println("ID: " + user.getId());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Password: " + user.getPassword());
        System.out.println("Role: " + user.getRole());
        System.out.println("Enabled: " + user.isEnabled());


        return org.springframework.security.core.userdetails.User.builder()
        	    .username(user.getUsername())
        	    .password(user.getPassword()) // already encoded
        	    .authorities(user.getRole().name()) // e.g., ROLE_ADMIN
        	    .disabled(!user.isEnabled())
        	    .build();
    }
}