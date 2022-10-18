package com.yaezzin.webservice.config.auth;


import com.yaezzin.webservice.web.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity // 시큐리티 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests() // 이게 선언이 되어야 antMatcher 옵션을 사용할 수 있음
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()
                .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                .anyRequest().authenticated() //설정값 이외의 나머지 URL들을 나타냄 -> authenticated()를 통해 인증된 사용자만 허용
                .and()
                .logout().logoutSuccessUrl("/")//로그아웃 성공 시 "/" 주소로 이동
                .and()
                .oauth2Login()
                .userInfoEndpoint()//OAuth2 로그인 성공 이후 사용자 정보를 가져올 떄의 설정들을 담당
                .userService(customOAuth2UserService); //소셜 로그인 성공 시 후속 조치를 진행할 UserService를 등록
    }
}
