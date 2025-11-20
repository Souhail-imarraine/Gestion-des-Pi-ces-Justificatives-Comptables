package com.alamanedocs.config;

import com.alamanedocs.entity.Societe;
import com.alamanedocs.entity.Utilisateur;
import com.alamanedocs.enums.Role;
import com.alamanedocs.repository.SocieteRepository;
import com.alamanedocs.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final SocieteRepository societeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Create and save test societe first
        Societe societe = societeRepository.findByIce("123456789")
                .orElseGet(() -> {
                    Societe newSociete = new Societe();
                    newSociete.setRaisonSociale("Test Company");
                    newSociete.setIce("123456789");
                    return societeRepository.save(newSociete);
                });

        // Create test users
        if (!utilisateurRepository.existsByEmail("comptable@test.com")) {
            Utilisateur comptable = new Utilisateur();
            comptable.setEmail("comptable@test.com");
            comptable.setMotDePasse(passwordEncoder.encode("password"));
            comptable.setNomComplet("Comptable Test");
            comptable.setRole(Role.COMPTABLE);
            utilisateurRepository.save(comptable);
        }

        if (!utilisateurRepository.existsByEmail("societe@test.com")) {
            Utilisateur societeUser = new Utilisateur();
            societeUser.setEmail("societe@test.com");
            societeUser.setMotDePasse(passwordEncoder.encode("password"));
            societeUser.setNomComplet("Societe Test");
            societeUser.setRole(Role.SOCIETE);
            societeUser.setSociete(societe);
            utilisateurRepository.save(societeUser);
        }
    }
}