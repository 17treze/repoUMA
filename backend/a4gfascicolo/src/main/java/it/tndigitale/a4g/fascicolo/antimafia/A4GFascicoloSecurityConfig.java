/**
 * 
 */
package it.tndigitale.a4g.fascicolo.antimafia;

import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;

//@Configuration
public class A4GFascicoloSecurityConfig { //extends GlobalMethodSecurityConfiguration {
	
	//@Autowired
	//private FascicoloPermissionEvaluator fascicoloPermEvaluator;

	//@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
	    DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
	    //expressionHandler.setPermissionEvaluator(fascicoloPermEvaluator);
	    return expressionHandler;
	}
}