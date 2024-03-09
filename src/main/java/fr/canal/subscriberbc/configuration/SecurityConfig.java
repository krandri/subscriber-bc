package fr.canal.subscriberbc.configuration;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public InMemoryUserDetailsManager userDetailsService() throws IOException, CsvException {
        List<UserDetails> users = getUsersFromCsv();

        return new InMemoryUserDetailsManager(users);
    }

    private List<UserDetails> getUsersFromCsv() throws IOException, CsvException {
        Resource resource = new ClassPathResource("users.csv");

        List<UserDetails> userDetails = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            List<String[]> rows = csvReader.readAll();

            rows.stream().forEach(row -> {
                String username = row[0];
                String password = row[1];
                String role = row[2];

                UserDetails user = User.withUsername(username).password(passwordEncoder().encode(password)).roles(role).build();
                userDetails.add(user);
            });
        }

        return userDetails;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("subscribers/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated());

        return http.build();
    }

}
