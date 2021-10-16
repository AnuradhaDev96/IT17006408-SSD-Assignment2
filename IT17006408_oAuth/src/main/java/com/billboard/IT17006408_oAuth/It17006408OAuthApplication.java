package com.billboard.IT17006408_oAuth;

import com.billboard.IT17006408_oAuth.model.BillboardUserDetail;
import com.billboard.IT17006408_oAuth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootApplication
public class It17006408OAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(It17006408OAuthApplication.class, args);
	}

	@Autowired
	public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository repository) throws Exception{
		builder.userDetailsService(new UserDetailsService(){
			@Override
			public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
				return new BillboardUserDetail(repository.findByUsername(s));
			}
	   		}
		);
	}

}
