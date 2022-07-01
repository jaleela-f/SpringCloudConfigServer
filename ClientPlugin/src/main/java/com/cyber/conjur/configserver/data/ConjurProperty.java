package com.cyber.conjur.configserver.data;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



public class ConjurProperty {

	
	  public static String conjurAccount;
	  
	  public static String conjurUrl;
	  
	  public static String conjurAuthTokenFile;
	  
	  public static String conjurAuthLogin;
	  
	  public static String conjurCertFile;
	  
	  public static String conjurSSLCertificate;
	  
	  public static String getConjurAccount() { return conjurAccount; }
	  
	  @Value("${CONJUR.ACCOUNT}") public void setConjurAccount(String
	  conjurAccount) { ConjurProperty.conjurAccount = conjurAccount; }
	  
	  public static String getConjurUrl() { return conjurUrl; }
	  
	  @Value("${CONJUR.APPLIANCE_URL}") public void setConjurUrl(String conjurUrl)
	  { ConjurProperty.conjurUrl = conjurUrl; }
	  
	  public static String getConjurAuthTokenFile() { return conjurAuthTokenFile; }
	  
	  
	  @PostConstruct
	  
	  @Value("${CONJUR.AUTHN_TOKEN_FILE }")
	  
	  public static void setConjurAuthTokenFile(String conjurAuthTokenFile) {
	  
	  System.out.println("Auth Token File "+conjurAuthTokenFile);
	  ConjurProperty.conjurAuthTokenFile = conjurAuthTokenFile; }
	  
	  public static String getConjurAuthLogin() { return conjurAuthLogin; }
	  
	  @Value("${CONJUR.AUTHN_LOGIN}")
	  
	  public void setConjurAuthLogin(String conjurAuthLogin) {
	  ConjurProperty.conjurAuthLogin = conjurAuthLogin; }
	  
	  public static String getConjurCertFile() { return conjurCertFile; }
	  
	  @Value("${CONJUR.CERT_FILE}")
	  
	  public void setConjurCertFile(String conjurCertFile) {
	  ConjurProperty.conjurCertFile = conjurCertFile; }
	  
	  public static String getConjurSSLCertificate() { return conjurSSLCertificate;
	  }
	  
	  @Value("${CONJUR.SSL_CERTIFICATE}") public void
	  setConjurSSLCertificate(String conjurSSLCertificate) {
	  ConjurProperty.conjurSSLCertificate = conjurSSLCertificate; }
	 
}
