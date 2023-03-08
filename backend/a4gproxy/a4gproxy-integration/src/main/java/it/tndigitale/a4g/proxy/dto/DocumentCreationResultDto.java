package it.tndigitale.a4g.proxy.dto;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class DocumentCreationResultDto {

	private String signature;
	private List<Entry<FailureType, String>> failures;

	public DocumentCreationResultDto() {
		this.failures = new ArrayList<>();
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public List<Entry<FailureType, String>> getFailures() {
		return failures;
	}

	public void addFailure(FailureType failureType, String message) {
		Entry<FailureType, String> entry = new SimpleEntry<>(failureType, message);
		failures.add(entry);
	}
}
