package com.alamanedocs.seeder;

import com.alamanedocs.entity.Societe;
import com.alamanedocs.entity.User;
import com.alamanedocs.enums.RoleType;
import com.alamanedocs.repository.SocieteRepository;
import com.alamanedocs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final SocieteRepository societeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (societeRepository.count() == 0) {
            log.info("Démarrage du seeding de la base de données...");
            seedSocietes();
            seedUsers();
            log.info("Seeding terminé avec succès !");
        }
    }

    private void seedSocietes() {
        List<Societe> societes = new ArrayList<>();

        societes.add(Societe.builder()
                .raisonSociale("MAROC TELECOM SA")
                .ice("001234567890001")
                .adresse("Avenue Annakhil, Hay Riad, Rabat")
                .telephone("0537717171")
                .emailContact("contact@maroctelecom.ma")
                .build());

        societes.add(Societe.builder()
                .raisonSociale("COSUMAR SA")
                .ice("001234567890002")
                .adresse("Route Côtière 1018, Casablanca")
                .telephone("0522488888")
                .emailContact("contact@cosumar.ma")
                .build());

        societes.add(Societe.builder()
                .raisonSociale("ATTIJARIWAFA BANK")
                .ice("001234567890003")
                .adresse("2, Boulevard Moulay Youssef, Casablanca")
                .telephone("0522294040")
                .emailContact("contact@attijariwafa.ma")
                .build());

        societes.add(Societe.builder()
                .raisonSociale("LABEL VIE")
                .ice("001234567890004")
                .adresse("Angle Bd Brahim Roudani et Rue Bnou Majed El Bahar, Casablanca")
                .telephone("0522974040")
                .emailContact("contact@labelvie.ma")
                .build());

        societes.add(Societe.builder()
                .raisonSociale("MANAGEM SA")
                .ice("001234567890005")
                .adresse("Twin Center, Tour A, Casablanca")
                .telephone("0522959595")
                .emailContact("contact@managem.ma")
                .build());

        societeRepository.saveAll(societes);
        log.info("5 sociétés créées");
    }

    private void seedUsers() {
        // Comptable
        User comptable = User.builder()
                .email("comptable@alamane.ma")
                .password(passwordEncoder.encode("Comptable123!"))
                .nomComplet("Ahmed Bennani")
                .role(RoleType.COMPTABLE)
                .actif(true)
                .build();
        userRepository.save(comptable);
        log.info("✅ Comptable créé: comptable@alamane.ma / Comptable123!");

        // Users Sociétés
        List<Societe> societes = societeRepository.findAll();
        String[] emails = {
                "contact@maroctelecom.ma",
                "contact@cosumar.ma",
                "contact@attijariwafa.ma",
                "contact@labelvie.ma",
                "contact@managem.ma"
        };
        String[] noms = {
                "Responsable Maroc Telecom",
                "Responsable Cosumar",
                "Responsable Attijariwafa",
                "Responsable Label Vie",
                "Responsable Managem"
        };

        for (int i = 0; i < societes.size(); i++) {
            User user = User.builder()
                    .email(emails[i])
                    .password(passwordEncoder.encode("Societe123!"))
                    .nomComplet(noms[i])
                    .role(RoleType.SOCIETE)
                    .societe(societes.get(i))
                    .actif(true)
                    .build();
            userRepository.save(user);
        }
        log.info("5 utilisateurs sociétés créés (password: Societe123!)");
    }
}
