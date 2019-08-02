
  package com.app.config;

//  
//  import org.springframework.beans.factory.annotation.Autowired; import
//  org.springframework.context.annotation.Configuration; import
//  org.springframework.http.HttpMethod; import
//  org.springframework.security.authentication.AuthenticationManager; import
//  org.springframework.security.config.annotation.authentication.builders.
//  AuthenticationManagerBuilder; import
//  org.springframework.security.config.annotation.web.builders.HttpSecurity;
//  import org.springframework.security.config.annotation.web.configuration.
//  EnableWebSecurity; import
//  org.springframework.security.config.annotation.web.configuration.
//  WebSecurityConfigurerAdapter; import
//  org.springframework.security.core.userdetails.UserDetailsService; import
//  org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import
//  org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//  
//  @Configuration
//  @EnableWebSecurity public class SecurityConfig extends
//  WebSecurityConfigurerAdapter{
//  
//  @Autowired private UserDetailsService userDetailsService;
//  
//  @Autowired private BCryptPasswordEncoder passwordEncoder;
//  
//  
//  
//  //1.un,pwd,role(authority)
//  
//  @Autowired public void configureGlobal(AuthenticationManagerBuilder auth)
//  throws Exception { auth.userDetailsService(userDetailsService)
//  .passwordEncoder(passwordEncoder); }
//  
//  //2.url -levels
//  
//  @Override public void configure(HttpSecurity http) throws Exception {
//  
//  http //.csrf().disable() // to disable CSRF , then we don't need to add csrf
//  token in our form
//  
//  .authorizeRequests()
//  .antMatchers("/resources/**","/login","/check","/forget","/otp").permitAll()
//  .antMatchers("/userr/register","/userr/check","/userr/checkM","/userr/save",
//  "/userr/otp").permitAll() .antMatchers(HttpMethod.GET,"/rest/**").permitAll()
//  .antMatchers(HttpMethod.POST,"/rest/**").permitAll()
//  .antMatchers(HttpMethod.PUT,"/rest/**").permitAll()
//  .antMatchers(HttpMethod.DELETE,"/rest/**").permitAll()
//  .anyRequest().authenticated()
//  
//  .and() .formLogin() .loginPage("/login")
//  .defaultSuccessUrl("/uom/register",true) .failureUrl("/login?error=true")
//  .permitAll()
//  
//  .and() .logout() .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//  .logoutSuccessUrl("/login?logout=true") .invalidateHttpSession(true)
//  .permitAll()
//  
//  
//  .and() .exceptionHandling() .accessDeniedPage("/denied")
//  
//  ;
//  
//  }
//  
//  }
// 
  
  /*
  @Configuration
 // @EnableWebSecurity
  public class SecurityConfig extends WebSecurityConfigurerAdapter
  {
	  @Autowired
	  private BCryptPasswordEncoder encoder;
	  
//	  @Autowired
//	  private UserDetailsService service;
	  
	  @Override
	  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		  auth.inMemoryAuthentication().withUser("abc").password(encoder.encode("abc")).authorities("EMP");
	  }

	 @Override
	protected void configure(HttpSecurity http) throws Exception {

		 http.authorizeRequests()
		 .antMatchers("/home").permitAll()
		 .antMatchers("/uom/**").hasAuthority("EMP")
		 .antMatchers("/wh").hasAnyAuthority("ADMIN","EMP")
		 .antMatchers("/**").authenticated()
		 
		 // for login
		 .and()  .formLogin()  .loginPage("/login")  
		 .defaultSuccessUrl("/home",true)
		 
		 // for logout
		 .and()
		 .logout()
		 .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		 .logoutSuccessUrl("/")
		 
		 // for access denied page
		 .and()
		 .exceptionHandling()
		 .accessDeniedPage("/LoginMenu")
		 ;
		 
	 }
  }
  */
  