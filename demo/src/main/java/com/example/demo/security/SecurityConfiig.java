package com.example.demo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiig {
    @Bean   //@Bean → Spring-ə deyir ki, bu metod bir obyekt  yaradacaq və onu istifadə edə bilərik.
    //Burada SecurityFilterChain obyekti yaradacağıq və Spring onu tətbiqdə təhlükəsizlik üçün istifadə edəcək.
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{ //Girişin yoxlanması tipidir
        //SecurityFilterChain → Spring Security-də filterlər zənciridir.
        //Sadə dillə: “Hər HTTP sorğusu gəldikdə yoxlamaları keçirən sistem” deməkdir.
        //Məsələn: “Bu sorğu login olub, roluna baxaq, icazə verək/verməyək” deyən hissədir.
      http
              .csrf(csrf -> csrf.disable())
              //CSRF → “Cross-Site Request Forgery” deməkdir, yəni saytlararası hücumları önləyən qoruma.
      //  csrf.disable() → bunu söndürürük, çünki API-lərdə çox vaxt lazım olmur (token və ya Basic Auth ilə işləyirik).
              .authorizeHttpRequests(auth -> auth
                      //Burada kim hansı URL-lərə daxil ola bilər deyirik.
                      //auth -> auth → Java lambda, yəni “burada qaydaları yazıram”.
                      .requestMatchers("/api/auth/**").permitAll()
                      .requestMatchers("/api/users/**").permitAll()
                      .anyRequest().authenticated()
                      //Qalan bütün yollar istifadəçi login olmalıdır.
                      //Sadə dillə: “Bütün digər qapılar üçün, kimlik yoxlanışı lazımdır.”
              )
              .httpBasic(Customizer.withDefaults());
      //HTTP Basic Authentication istifadə olunur.
        //Basic Auth → istifadəçi username və password ilə doğrulanır.
        //Customizer.withDefaults() → default, yəni əlavə ayar etməyə ehtiyac yoxdur.
      return http.build();
      //http.build() → bütün bu qaydaları filter chain  halına gətirir.
        //return → Spring-ə qaytarır, Spring bu filter chain-i HTTP sorğularına tətbiq edir.
        //Sadə misal: “Hazır təhlükəsizlik sistemi quraşdırılıb, indi hər gələn qonağa yoxlama aparırıq.”

    }
}
