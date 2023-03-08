package it.tndigitale.a4gutente.component;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;


/**
 *
 * @author Massimo Zorer
 *
 */
@Component
public class EncryptionComponent {

	private String encryptKey = "oOsqhDDHzd91xJTZ";
	
	private static final String SECRET_KEY_SPEC_ALGOR = "AES";


	/**
	 * 	@author Massimo Zorer
	 *  @param  text stringa da criptare a 128 bit aes
	 */
	public String encrypt(String text) throws Exception {
		// Create key and cipher
		Key aesKey = new SecretKeySpec(getEncryptKey().getBytes(), SECRET_KEY_SPEC_ALGOR);
		Cipher cipher = Cipher.getInstance(SECRET_KEY_SPEC_ALGOR);
		// encrypt the text
		cipher.init(Cipher.ENCRYPT_MODE, aesKey);
		byte[] encrypted = cipher.doFinal(text.getBytes());
		byte[] enc = Base64.getEncoder().encode(encrypted);
		return new String(enc);
	}

	/**
	 * @author Massimo Zorer
	 * @param encrypted array criptato 128 bit aes
	 */
	public String decrypt(String text) throws Exception {
		// Create key and cipher
		Key aesKey = new SecretKeySpec(getEncryptKey().getBytes(), SECRET_KEY_SPEC_ALGOR);
		Cipher cipher = Cipher.getInstance(SECRET_KEY_SPEC_ALGOR);
		// decrypt the text
		cipher.init(Cipher.DECRYPT_MODE, aesKey);

		byte[] dec = Base64.getDecoder().decode(text.getBytes());

		String decrypted = new String(cipher.doFinal(dec));
//		System.err.println(decrypted);
		return decrypted;
	}

	public String getEncryptKey() {
		return encryptKey;
	}

	protected void setEncryptKey(String encryptKey) {
		this.encryptKey = encryptKey;
	}



}
