package raProxy.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class SSLManager {
	private String keyStorePath = null;
	private String keyStorePwd = null;
	String trustStorePath = null;
	String trustStorePwd = null;
	private String protocol = "SSL";
	private String deviceId = null;
	public static String SOFT_DEVICE = "JSOFT_LIB";
	public static String HARD_DEVICE = "JHARD_LIB";
	public static final String IBM_X509 = "IBMX509";
	public static final String IBMJSSE2 = "IBMJSSE2";
	public static final String SUN = "SUN";
	public static final String HEWLETT = "HEWLETT";
	private SSLContext context = null;

	public SSLManager(String keyStorePath, String keyStorePwd, String trustStorePath, String trustStorePwd) {
		this.keyStorePath = keyStorePath;
		this.keyStorePwd = keyStorePwd;
		this.trustStorePath = trustStorePath;
		this.trustStorePwd = trustStorePwd;
	}

	public SSLContext getSSLContext(String protocol, String deviceId) throws Exception {
		this.protocol = protocol;
		this.deviceId = deviceId;

		String vmVender = System.getProperty("java.vm.vendor");
		if (vmVender.toUpperCase().indexOf("IBM") != -1) {
			this.protocol = "SSL_TLS";
			this.context = SSLContext.getInstance(this.protocol, "IBMJSSE2");
		} else {
			this.context = SSLContext.getInstance(this.protocol);
		}
		KeyManager[] kms = getKeyManagers();
		TrustManager[] tms = getTrustManagers();

		this.context.init(kms, tms, null);
		return this.context;
	}

	private KeyManager[] getKeyManagers() throws Exception {
		KeyManager[] kms;
		KeyManagerFactory kmFact = null;
		KeyStore ks = null;
		try {
			if (this.deviceId.equalsIgnoreCase(SOFT_DEVICE)) {
				kmFact = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				ks = KeyStore.getInstance("JKS");
			} else {
				throw new KeyStoreException();
			}
		} catch (NoSuchAlgorithmException ex) {
			throw ex;
		} catch (KeyStoreException ex) {
			throw ex;
		}
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(this.keyStorePath);
			ks.load(fis, this.keyStorePwd.toCharArray());
			fis.close();

			kmFact.init(ks, this.keyStorePwd.toCharArray());
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
			kms = kmFact.getKeyManagers();
		} catch (FileNotFoundException ex) {
			throw ex;
		} catch (NoSuchAlgorithmException ex) {
			throw ex;
		} catch (KeyStoreException ex) {
			throw ex;
		} catch (UnrecoverableKeyException ex) {
			throw ex;
		} catch (IOException ex) {
			throw ex;
		} catch (CertificateException ex) {
			throw ex;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}
		return kms;
	}

	private TrustManager[] getTrustManagers() throws Exception {
		TrustManagerFactory trustManagerFactory = null;
		KeyStore trustKS = null;
		try {
			if (this.deviceId.equalsIgnoreCase(SOFT_DEVICE)) {
				trustKS = KeyStore.getInstance("JKS");
			} else {
				throw new KeyStoreException();
			}
			FileInputStream trustCert = new FileInputStream(this.trustStorePath);
			trustKS.load(trustCert, this.trustStorePwd.toCharArray());
			trustCert.close();

			trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(trustKS);
		} catch (NoSuchAlgorithmException ex) {
			throw ex;
		} catch (KeyStoreException ex) {
			throw ex;
		} catch (CertificateException ex) {
			throw ex;
		} catch (IOException ex) {
			throw ex;
		}
		TrustManager[] tms = trustManagerFactory.getTrustManagers();
		return tms;
	}
}
