/*
 * package com.app.config;
 * 
 * import java.util.Properties;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.core.env.Environment; import
 * org.springframework.mail.javamail.JavaMailSenderImpl; import
 * org.springframework.orm.hibernate5.HibernateTemplate; import
 * org.springframework.orm.hibernate5.HibernateTransactionManager; import
 * org.springframework.orm.hibernate5.LocalSessionFactoryBean; import
 * org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import
 * org.springframework.web.multipart.commons.CommonsMultipartResolver; import
 * org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
 * import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 * import org.springframework.web.servlet.view.InternalResourceViewResolver;
 * 
 * @EnableWebMvc //it is Spring WEB MVC AppConfig
 * 
 * @EnableTransactionManagement // enable commit/rollback
 * 
 * @Configuration
 * 
 * @PropertySource("classpath:app.properties")
 * 
 * @ComponentScan(basePackages="com.app")
 * 
 * @Import(SecurityConfig.class) public class AppConfig implements
 * WebMvcConfigurer {
 * 
 * @Autowired private Environment env;
 * 
 * 
 * //1. DataSource
 * 
 * @Bean public BasicDataSource dsObj() { BasicDataSource ds=new
 * BasicDataSource(); ds.setDriverClassName(env.getProperty("dc"));
 * ds.setUrl(env.getProperty("url")); ds.setUsername(env.getProperty("un"));
 * ds.setPassword(env.getProperty("pwd")); ds.setInitialSize(1);
 * ds.setMaxIdle(1); ds.setMinIdle(1); ds.setMaxTotal(5); return ds; }
 * 
 * // 2. SessionFactory
 * 
 * @Bean public LocalSessionFactoryBean sfObj() { LocalSessionFactoryBean sf=new
 * LocalSessionFactoryBean(); sf.setDataSource(dsObj());
 * sf.setHibernateProperties(props()); TODO pass model classes
 * //sf.setAnnotatedClasses(Uom.class); sf.setPackagesToScan("com.app.model");
 * //Model class names return sf; }
 * 
 * private Properties props() { Properties p = new Properties();
 * p.put("hibernate.dialect", env.getProperty("dialect"));
 * p.put("hibernate.show_sql", env.getProperty("showsql"));
 * p.put("hibernate.format_sql", env.getProperty("fmtsql"));
 * p.put("hibernate.hbm2ddl.auto", env.getProperty("ddlauto")); return p; }
 * 
 * // 3. HibernateTemplate
 * 
 * @Bean public HibernateTemplate htObj() { HibernateTemplate ht = new
 * HibernateTemplate(); ht.setSessionFactory(sfObj().getObject()); return ht; }
 * 
 * // 4. Transaction Manager
 * 
 * @Bean public HibernateTransactionManager htmObj() {
 * HibernateTransactionManager htm = new HibernateTransactionManager();
 * htm.setSessionFactory(sfObj().getObject()); return htm; }
 * 
 * // 5. View Resolver
 * 
 * @Bean public InternalResourceViewResolver ivr() {
 * InternalResourceViewResolver v = new InternalResourceViewResolver();
 * v.setPrefix(env.getProperty("prefix"));
 * v.setSuffix(env.getProperty("suffix")); return v; }
 * 
 * // 6. adding CommonsMultipartResolver object
 * 
 * @Bean public CommonsMultipartResolver multipartResolver() // object name must
 * be multipartResolver { return new CommonsMultipartResolver();
 * 
 * }
 * 
 * // java mail sender Configuration
 * 
 * @Bean public JavaMailSenderImpl mail() { JavaMailSenderImpl mail = new
 * JavaMailSenderImpl(); mail.setHost(env.getProperty("email.host"));
 * mail.setPort(env.getProperty("email.port", Integer.class));
 * mail.setUsername(env.getProperty("email.usr"));// enter your emailId.
 * mail.setPassword(env.getProperty("email.pwd"));// enter ur password.
 * mail.setJavaMailProperties(eprops()); return mail; }
 * 
 * private Properties eprops() { Properties p = new Properties();
 * p.put("mail.smtp.auth", env.getProperty("email.auth"));
 * p.put("mail.smtp.starttls.enable", env.getProperty("email.ssl.enbl")); return
 * p; }
 * 
 * // Password Encoder
 * 
 * @Bean public BCryptPasswordEncoder pwdEncoder() { return new
 * BCryptPasswordEncoder(); }
 * 
 * // to add resources like images css , js
 * 
 * @Override public void addResourceHandlers(ResourceHandlerRegistry registry) {
 * registry.addResourceHandler("/resources/**").addResourceLocations(
 * "/resources/"); }
 * 
 * }
 */