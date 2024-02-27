package com.picstory.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringbootSecurityConfig {
	
	@Bean
	   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	      http.cors(AbstractHttpConfigurer::disable).httpBasic((basic) -> basic.disable()).csrf(csrf -> csrf.disable())
	            .authorizeRequests(authorize -> authorize
	                  .antMatchers("/", "/login", "/joinIn", "/idDoubleCheck", "/nickDoubleCheck",
	                        "/mailDoubleCheck", "/myinfo", "/infoUpdate", "/imageUpload",
	                        "/imageDownload", "/selectId", "/selectPw", "/favorTrue",
	                        "/favorFalse", "/favorPageImgList", "/folderList", "/folderListSelect",
	                        "/url", "/naverJoin", "/deleteUser", "/payment", "/updateFolderName",
	                        "/deleteFolder","/createTag","/getCustomTag","/selectUserPremium",
	                        "/loadTaggingPhoto", "/selectTaggedPhoto","/loadSelectedPhotoNum"
	                       ,"/deleteChckedPhoto","/addPhotoToFolder","/savePhotoInFolder","/findFolderNum")
	                  .permitAll().antMatchers("/billing/**").hasRole("1").anyRequest().authenticated())
	            .formLogin(login -> login.disable()); 

	      return http.build();
	   }

	   @Bean
	   PasswordEncoder passwordEncoder() {
	      return new BCryptPasswordEncoder();
	      
	   }

//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .cors().disable()
//            .csrf().disable()
//            .authorizeRequests()
//                .antMatchers("/images/**", "/join", "/join-process").permitAll()
//                .antMatchers("/user").hasAnyRole("USER", "ADMIN")
//                .antMatchers("/admin").hasRole("ADMIN")
//                .anyRequest().authenticated()
//            .and()
//            .formLogin()
//                .loginPage("/login")
//                .loginProcessingUrl("/login-process")
//                .usernameParameter("userId")
//                .passwordParameter("pw")
//                .defaultSuccessUrl("/api/dashboard", true)
//                .permitAll();
//
//        return http.build();
//    }
//
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
