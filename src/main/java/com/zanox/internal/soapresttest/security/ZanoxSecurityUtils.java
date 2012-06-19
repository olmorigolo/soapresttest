package com.zanox.internal.soapresttest.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * A Collection of methods for the user authentication within a zanox environment.
 * 
 * @author fit, martina.willig
 * 
 */
public class ZanoxSecurityUtils {

	/**
	 * Holds supported date format patterns for SOAP.
	 */
	protected static final String[] SUPPORTED_SOAP_DATE_FORMATS = { "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyy-MM-dd'T'HH:mm:ss.SSS",
		"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", };

	/**
	 * The REST date format pattern. Like "Fri, 04 Jan 2008 13:19:53 GMT"
	 */
	public static final String REST_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss Z";

	/**
	 * The name of the security encoding algorithm.
	 */
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	protected ZanoxSecurityUtils() {
	}

	/**
	 * Create a new randomUUID.
	 * We need this method not only for clients but for the server too if for instance an URL for the client is created.
	 * 
	 * @return The nonce
	 */
	public static String createNonce() {
		return UUID.randomUUID().toString();
	}

	/**
	 * Create a formatted soap-date-string of the given date or the current date if date is null.
	 * 
	 * @param date A date or null
	 * @return
	 */
	public static String createSoapTimestamp(Calendar date) {
		return createTimestamp(date, SUPPORTED_SOAP_DATE_FORMATS[0], null, null);
	}

	/**
	 * Create a formatted rest-date-string of the given date or the current date if date is null.
	 * 
	 * @param date A date or null
	 * @return
	 */
	public static String createRestTimestamp(Calendar date) {
		return createTimestamp(date, REST_DATE_FORMAT, null, null);
	}

	/**
	 * Create a date-string of the give date.
	 * 
	 * @param date A date
	 * @param format A date format pattern
	 * @param timezone A timezone, is used if date is null. Defaults to GMT+0
	 * @param locale A locale used to create the String with the format pattern. Defaults to US.
	 * @return
	 */
	private static String createTimestamp(Calendar date, String format, String timezone, Locale locale) {

		if (timezone == null) {
			timezone = "GMT";
		}
		if (locale == null) {
			locale = Locale.US;
		}
		if (date == null) {
			date = Calendar.getInstance(TimeZone.getTimeZone(timezone));
		}

		SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
		return sdf.format(date.getTime());
	}

	/**
	 * Create the soap signature for the specified wsAuthentication, the serviceName, the methodName and the given timeStamp. The soapSignature
	 * consists of the sharedKey encoded with the hMac algorithm and a complex string of the serviceName, the invoked methodName and the timeStamp.
	 */
	public static String createSoapSignature(String secretKey, String nonce, String serviceName, String methodName, String timestamp) {
		return generateHmacKey(secretKey.getBytes(), serviceName.toLowerCase() + methodName.toLowerCase() + timestamp + nonce);
	}

	/**
	 * Create the rest signature for the specified wsAuthentication, the uriPath, the httpVerb and the given timeStamp. The restSignature consists of
	 * the sharedKey encoded with the hMac algorithm and a complex string of the invoked uriPath, the httpVerb and the timeStamp.
	 */
	public static String createRestSignature(String secretKey, String nonce, String uriPath, String httpVerb, String timestamp) {
		return generateHmacKey(secretKey.getBytes(), httpVerb + uriPath.toLowerCase() + timestamp + nonce);
	}

	/**
	 * Create the signature using a HMAC_SHA1_ALGORITHM
	 * 
	 * @param sharedKeyBytes The security key bytes
	 * @param value The value to be encoded
	 * @return The signature as String
	 */
	@SuppressWarnings("restriction")
	private static String generateHmacKey(byte[] sharedKeyBytes, String value) {

		// acquire an HMAC/SHA1 from the raw key bytes.
		SecretKeySpec signingKey = new SecretKeySpec(sharedKeyBytes, HMAC_SHA1_ALGORITHM);

		// acquire the MAC instance and initialize with the signing key.
		Mac mac = null;
		try {
			mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);
		} catch (InvalidKeyException ex) {
			throw new IllegalArgumentException("Invalid key: '" + signingKey + "'", ex);
		} catch (NoSuchAlgorithmException ex) {
			throw new IllegalStateException("Algorithm not found: '" + HMAC_SHA1_ALGORITHM + "'", ex);
		}

		// compute the HMAC on the digest, then set it and return the complete encoded signature
		byte[] encodedValue = mac.doFinal(value.getBytes());
		sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
		return enc.encode(encodedValue);
	}
}
