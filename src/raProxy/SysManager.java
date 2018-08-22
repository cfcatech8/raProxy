package raProxy;

import javax.net.ssl.SSLSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import raProxy.utils.Config;
import raProxy.utils.SSLManager;

public class SysManager {
	public static SSHandler ssHandler;
	protected static SSLSocketFactory ssf;
	public static SSLManager sslManager;
	public static SocketHandler socketHandler;
	private static Logger logger = LoggerFactory.getLogger(SysManager.class);

	public static void main(String[] args) throws Exception {
		Config.initConfig("raProxy.properties");

		if (Config.mode == 0) {
			logger.info("mode is Fake CA");
			// Fake CA - SS server, send Socket
			sslManager = new SSLManager(Config.keyStore, Config.keyStorePass, Config.trustKeyStore,
					Config.trustKeyStorePass);
			ssHandler = new SSHandler();
			ssHandler.init();
			new Thread(ssHandler, "SSL Socket Handler").start();
			logger.info("SSL Socket Handler has been started");
		} else {
			logger.info("mode is Fake RA");
			// Fake RA - Socket server, send SS
			sslManager = new SSLManager(Config.keyStore, Config.keyStorePass, Config.trustKeyStore,
					Config.trustKeyStorePass);
			ssf = sslManager.getSSLContext("TLS", "JSOFT_LIB").getSocketFactory();
			socketHandler = new SocketHandler();
			socketHandler.init();
			new Thread(socketHandler, "Socket Handler").start();
			logger.info("Socket Handler has been started");
		}

	}
}
