package it.tndigitale.a4g.proxy.services.protocollo;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Component
public class PiTreRetryTemplate extends RetryTemplate {
	
	@Value("${pitre.retry.maxattempts:5}")
	private int maxAttempts;
	
	@Value("${pitre.retry.backoffperiod:500}")
	private int backOffPeriod;

	public PiTreRetryTemplate() {
		
		// Retry Policy: specifico le condizioni di retry (p.es. quante volte, su quali
		// eccezioni...)
		ExceptionClassifierRetryPolicy exRetryPolicy = new ExceptionClassifierRetryPolicy();
		HashMap<Class<? extends Throwable>, RetryPolicy> policyMap = new HashMap<>();
		policyMap.put(Exception.class, new SimpleRetryPolicy(maxAttempts));
		policyMap.put(PiTreException.class, new NeverRetryPolicy());
		exRetryPolicy.setPolicyMap(policyMap);
		this.setRetryPolicy(exRetryPolicy);

		// BackOff Policy: specifico quello che succede tra un retry e l'altro
		FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
		backOffPolicy.setBackOffPeriod(backOffPeriod);
		this.setBackOffPolicy(backOffPolicy);
	}
}
