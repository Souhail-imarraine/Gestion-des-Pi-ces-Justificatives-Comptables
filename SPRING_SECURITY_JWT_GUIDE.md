# Guide Complet : Spring Security 6 & JWT Authentication

## 📋 Table des Matières
1. [Architecture du Projet](#architecture-du-projet)
2. [Spring Security 6 - Nouveautés](#spring-security-6---nouveautés)
3. [JWT Authentication](#jwt-authentication)
4. [Implémentation Détaillée](#implémentation-détaillée)
5. [Questions d'Entretien](#questions-dentretien)

---

## 🏗️ Architecture du Projet

### Structure des Packages
```
com.alamanedocs/
├── entity/           # Entités JPA
├── enums/           # Énumérations (Role, TypeDocument, StatutDocument)
├── dto/             # Data Transfer Objects
├── repository/      # Repositories JPA
├── service/         # Logique métier
├── controller/      # Contrôleurs REST
├── security/        # Configuration sécurité
├── config/          # Configurations Spring
└── exception/       # Gestion des exceptions
```

### Entités Principales
- **Utilisateur** : Implémente `UserDetails` pour Spring Security
- **Societe** : Entité métier pour les entreprises
- **Document** : Gestion des pièces justificatives

---

## 🔐 Spring Security 6 - Nouveautés

### 1. Configuration Lambda-Based
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())  // Lambda au lieu de .csrf().disable()
        .sessionManagement(session -> 
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll()
            .anyRequest().authenticated()
        );
    return http.build();
}
```

### 2. Suppression de WebSecurityConfigurerAdapter
- **Avant Spring Security 5** : Extension de `WebSecurityConfigurerAdapter`
- **Spring Security 6** : Configuration par `@Bean` methods

### 3. Nouvelles Annotations
- `@EnableWebSecurity` : Toujours nécessaire
- `@EnableMethodSecurity` : Remplace `@EnableGlobalMethodSecurity`

---

## 🎫 JWT Authentication

### Qu'est-ce que JWT ?
**JSON Web Token** est un standard ouvert (RFC 7519) pour transmettre des informations de manière sécurisée.

### Structure JWT
```
Header.Payload.Signature
```

#### Header
```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

#### Payload
```json
{
  "sub": "user@example.com",
  "iat": 1516239022,
  "exp": 1516325422
}
```

#### Signature
```
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret
)
```

### Avantages JWT
- **Stateless** : Pas de stockage côté serveur
- **Scalable** : Fonctionne avec plusieurs serveurs
- **Sécurisé** : Signé cryptographiquement
- **Compact** : Taille réduite

---

## 🛠️ Implémentation Détaillée

### 1. Entité Utilisateur avec UserDetails
```java
@Entity
public class Utilisateur implements UserDetails {
    // Champs de base...
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
    
    @Override
    public String getUsername() { return email; }
    
    @Override
    public String getPassword() { return motDePasse; }
    
    @Override
    public boolean isEnabled() { return actif; }
    
    // Autres méthodes UserDetails...
}
```

### 2. JWT Utility Class
```java
@Component
public class JwtUtil {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long jwtExpiration = 86400000; // 24h
    
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key)
                .compact();
    }
    
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
```

### 3. UserDetailsService Implementation
```java
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}
```

### 4. Authentication Service
```java
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getMotDePasse())
        );
        
        Utilisateur user = (Utilisateur) authentication.getPrincipal();
        String token = jwtUtil.generateToken(user.getEmail());
        
        return new AuthResponse(token, user.getEmail(), user.getNomComplet(), user.getRole());
    }
}
```

### 5. Security Configuration
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/h2-console/**").permitAll()
                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions().disable());
        
        return http.build();
    }
}
```

---

## 🎯 Concepts Clés Spring Boot 3

### 1. Jakarta EE Migration
- **Ancien** : `javax.persistence.*`
- **Nouveau** : `jakarta.persistence.*`

### 2. Native Compilation
- Support GraalVM pour compilation native
- Démarrage plus rapide, consommation mémoire réduite

### 3. Observability
- Micrometer intégré par défaut
- Support natif pour les métriques et tracing

### 4. Spring Data JPA Améliorations
- Meilleure performance des requêtes
- Support amélioré pour les projections

---

## 🎤 Questions d'Entretien Spring Security & JWT

### Questions Débutant

1. **Qu'est-ce que Spring Security ?**
   - Framework de sécurité pour applications Spring
   - Gère l'authentification et l'autorisation
   - Protection contre les attaques communes (CSRF, XSS, etc.)

2. **Différence entre authentification et autorisation ?**
   - **Authentification** : Vérifier l'identité (qui êtes-vous ?)
   - **Autorisation** : Vérifier les permissions (que pouvez-vous faire ?)

3. **Qu'est-ce qu'un JWT ?**
   - JSON Web Token
   - Token auto-contenu avec informations utilisateur
   - Signé cryptographiquement

### Questions Intermédiaires

4. **Comment fonctionne l'authentification JWT ?**
   - Client envoie credentials
   - Serveur valide et génère JWT
   - Client stocke JWT et l'envoie dans chaque requête
   - Serveur valide JWT sans état de session

5. **Avantages/Inconvénients de JWT vs Sessions ?**
   
   **JWT Avantages :**
   - Stateless, scalable
   - Pas de stockage serveur
   - Fonctionne cross-domain
   
   **JWT Inconvénients :**
   - Taille plus importante
   - Difficile à révoquer
   - Stockage côté client sensible

6. **Qu'est-ce que UserDetails dans Spring Security ?**
   - Interface représentant un utilisateur
   - Contient informations d'authentification
   - Implémentée par l'entité utilisateur

### Questions Avancées

7. **Comment implémenter un JWT Filter ?**
   ```java
   public class JwtAuthenticationFilter extends OncePerRequestFilter {
       @Override
       protected void doFilterInternal(HttpServletRequest request, 
                                     HttpServletResponse response, 
                                     FilterChain filterChain) {
           String token = extractToken(request);
           if (token != null && jwtUtil.isTokenValid(token)) {
               String email = jwtUtil.extractEmail(token);
               UserDetails userDetails = userDetailsService.loadUserByUsername(email);
               
               UsernamePasswordAuthenticationToken authentication = 
                   new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
               SecurityContextHolder.getContext().setAuthentication(authentication);
           }
           filterChain.doFilter(request, response);
       }
   }
   ```

8. **Gestion de l'expiration des tokens JWT ?**
   - Refresh tokens pour renouveler l'accès
   - Blacklist des tokens révoqués
   - Durée de vie courte pour les access tokens

9. **Sécurisation des endpoints avec des rôles ?**
   ```java
   @PreAuthorize("hasRole('COMPTABLE')")
   @GetMapping("/admin/documents")
   public List<Document> getAllDocuments() { ... }
   
   @PreAuthorize("hasRole('SOCIETE') and #societeId == authentication.principal.societe.id")
   @GetMapping("/societe/{societeId}/documents")
   public List<Document> getDocumentsBySociete(@PathVariable Long societeId) { ... }
   ```

10. **Comment gérer CORS avec Spring Security ?**
    ```java
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    ```

### Questions Architecturales

11. **Comment structurer un projet Spring Security ?**
    - Séparation des responsabilités
    - Configuration centralisée
    - Services dédiés à l'authentification
    - DTOs pour les échanges

12. **Bonnes pratiques sécurité JWT ?**
    - Utiliser HTTPS uniquement
    - Durée de vie courte des tokens
    - Stockage sécurisé côté client
    - Validation stricte des tokens
    - Rotation des clés de signature

13. **Tests unitaires pour Spring Security ?**
    ```java
    @Test
    @WithMockUser(roles = "COMPTABLE")
    void testComptableAccess() {
        // Test avec utilisateur mocké
    }
    
    @Test
    void testJwtTokenGeneration() {
        String token = jwtUtil.generateToken("test@example.com");
        assertThat(jwtUtil.extractEmail(token)).isEqualTo("test@example.com");
    }
    ```

---

## 🚀 Conseils pour l'Entretien

### Préparation Technique
1. **Maîtriser les concepts de base** : Authentication, Authorization, JWT
2. **Connaître Spring Security 6** : Nouvelles configurations, lambda syntax
3. **Comprendre les patterns** : Filter Chain, UserDetails, AuthenticationManager
4. **Pratique hands-on** : Implémenter un projet complet comme celui-ci

### Questions à Poser au Recruteur
1. "Quelles sont les pratiques de sécurité en place dans l'équipe ?"
2. "Comment gérez-vous l'authentification dans vos microservices ?"
3. "Utilisez-vous des outils comme OAuth2, Keycloak ?"
4. "Quelle est votre approche pour les tests de sécurité ?"

### Démonstration Pratique
- Montrer ce projet fonctionnel
- Expliquer l'architecture et les choix techniques
- Démontrer la compréhension des concepts sécurité
- Proposer des améliorations possibles

---

## 📚 Ressources Complémentaires

- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [JWT.io](https://jwt.io/) - Décodeur JWT
- [OWASP Security Guidelines](https://owasp.org/)
- [Spring Boot 3 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide)

---

*Ce guide couvre les aspects essentiels de Spring Security 6 et JWT pour réussir vos entretiens techniques.*