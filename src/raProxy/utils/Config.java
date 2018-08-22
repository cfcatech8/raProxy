package raProxy.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
	public static int mode;
	public static int rxPort;
	public static String txAddr;
	public static int txPort;
	public static String keyStore;
	public static String keyStorePass;
	public static String trustKeyStore;
	public static String trustKeyStorePass;
	public static int connectTO;
	public static int threadPoolSize;
	private static Logger logger = LoggerFactory.getLogger(Config.class);

	public static void initConfig(String ConfigFilePath) throws IOException {
		Properties raProperties = new Properties();
		File cfgFile = new File(ConfigFilePath);
		FileInputStream fis = new FileInputStream(cfgFile);
		raProperties.load(fis);

		mode = Integer.parseInt(raProperties.getProperty("mode"));
		rxPort = Integer.parseInt(raProperties.getProperty("rxPort"));
		txAddr = raProperties.getProperty("txAddr");
		txPort = Integer.parseInt(raProperties.getProperty("txPort"));
		keyStore = raProperties.getProperty("keyStore");
		keyStorePass = raProperties.getProperty("keyStorePass");
		trustKeyStore = raProperties.getProperty("trustKeyStore");
		trustKeyStorePass = raProperties.getProperty("trustKeyStorePass");
		connectTO = Integer.parseInt(raProperties.getProperty("connectTO"));
		threadPoolSize = Integer.parseInt(raProperties.getProperty("threadPoolSize"));
		logger.info("********************Start to init Config************************");
		logger.info("mode is " + mode);
		logger.info("rxPort is " + rxPort);
		logger.info("txAddr is " + txAddr);
		logger.info("txPort is " + txPort);
		logger.info("keyStore is " + keyStore);
		logger.info("keyStorePass is " + keyStorePass);
		logger.info("keyStorePass is " + keyStorePass);
		logger.info("trustKeyStore is " + trustKeyStore);
		logger.info("trustKeyStorePass is " + trustKeyStorePass);
		logger.info("connectTO is " + connectTO);
		logger.info("threadPoolSize is " + threadPoolSize);
		logger.info("*********************Finish init Config************************");
	}
}
