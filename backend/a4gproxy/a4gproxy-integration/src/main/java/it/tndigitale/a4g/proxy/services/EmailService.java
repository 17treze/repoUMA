package it.tndigitale.a4g.proxy.services;

public interface EmailService {
	
    void sendSimpleMessage(	String to,
            				String subject,
            				String text);

}
