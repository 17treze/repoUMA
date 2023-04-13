/**
 * 
 */
package it.tndigitale.a4gutente;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author it417
 */
@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = { "it.tndigitale.a4gutente", "it.tndigitale.a4g" })
public class A4gutenteApplication {
	
	//    private static final Logger logger = LoggerFactory.getLogger(A4gutenteApplication.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(A4gutenteApplication.class, args);
		//        logger.debug("-- Application SSO Started --");
	}
	
}
