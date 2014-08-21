package com.softserveinc.ita.kaiji;

import com.softserveinc.ita.kaiji.web.filter.AuthenticationSuccessFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * Security configuration
 *
 * @author Sydorenko Oleksandra
 * @version 1.0
 * @since 20.08.14
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .authoritiesByUsernameQuery("SELECT nickname,role FROM user, user_role WHERE nickname = ? AND user.id = user_role.user_id")
                .usersByUsernameQuery("SELECT nickname , password, true FROM user WHERE nickname =?")
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .accessDecisionManager(accessDecisionManager())
                .antMatchers("/game/**").access("hasRole('USER_ROLE')")
                .antMatchers("/config/**").access("hasRole('ADMIN_ROLE')")
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(authenticationSuccessHandler())
                .and()
                .logout()
                .logoutSuccessUrl("/");
    }

    @Bean
    public AuthenticationSuccessFilter authenticationSuccessHandler() {
        return new AuthenticationSuccessFilter();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AffirmativeBased accessDecisionManager() {
        return new AffirmativeBased(Arrays.asList(roleVoter(),
                webExpressionVoter(), (AccessDecisionVoter) authenticatedVoter()));
    }

    @Bean
    public RoleVoter roleVoter() {
        RoleVoter roleVoter = new RoleVoter();
        roleVoter.setRolePrefix("");
        return roleVoter;
    }

    @Bean
    public WebExpressionVoter webExpressionVoter() {
        return new WebExpressionVoter();
    }

    @Bean
    public AuthenticatedVoter authenticatedVoter() {
        return new AuthenticatedVoter();
    }
}
