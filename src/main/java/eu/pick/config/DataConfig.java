package eu.pick.config;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("eu.pick.repository")
//@ImportResource({"classpath*:META-INF/persistence.xml"})

public class DataConfig extends WebMvcConfigurerAdapter {

	@Autowired 
	private Environment env;
	@Autowired
	private ServletContext servletContext;
	
	@Bean
	public DataSource dataSource() {
		try {
			ComboPooledDataSource ds = new ComboPooledDataSource();
			ds.setDriverClass(env.getRequiredProperty("app.jdbc.driverClassName"));
			ds.setJdbcUrl(env.getRequiredProperty("app.jdbc.url"));
			ds.setUser(env.getRequiredProperty("app.jdbc.username"));
			ds.setPassword(env.getRequiredProperty("app.jdbc.password"));
			ds.setAcquireIncrement(5);
			ds.setIdleConnectionTestPeriod(60);
			ds.setMaxPoolSize(100);
			ds.setMaxStatements(50);
			ds.setMinPoolSize(10);
			return ds;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Bean 
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		HibernateJpaVendorAdapter vendor = new HibernateJpaVendorAdapter();
		vendor.setShowSql(false);
		
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		System.out.print("saraaaaaaaaaaaaaaaaaaaaaaaaaa");
		System.out.println("saraaaaaaaaaaaaaaaaaaaaaaaaqqaa");


		String persistencelocation = servletContext.getRealPath("src/main/java/META-INF/persistence.xml");

		System.out.println("danishhhhhhhhhhhhhh"+persistencelocation);
		
		em.setPersistenceXmlLocation("classpath:META-INF/persistence.xml");
		em.setPersistenceUnitName("hibernatePersistenceUnit");
		em.setDataSource(dataSource());
		em.setJpaVendorAdapter(vendor);
		return em;
	}
	
	@Bean 
	public JpaTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}
}
