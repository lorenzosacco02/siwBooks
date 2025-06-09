package it.uniroma3.siw.siwbooks;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SiwBooksApplicationTests {
    @Test
    public void testPasswordMatchesHash() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String rawPassword = "lollo";
        // Genero un hash nuovo (puoi anche stamparlo)
        String encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println("Encoded password: " + encodedPassword);

        // Verifico che la password corrisponda all'hash appena generato
        assertTrue(passwordEncoder.matches(rawPassword, "$2a$10$Zvb18mH/1S.S.ljBRXJVwefA/qa.ebejmVPEIhOVlQk7QwTt2n2ou"));
    }
}
