package raProxy;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import raProxy.utils.Config;

//SSL Socket Handler
//接收RA请求
public class SSHandler implements Runnable {
	protected Socket socket;
	protected SSLServerSocketFactory ssf;
	protected ServerSocket serverSocket;
	protected ExecutorService pool;
	private static Logger logger = LoggerFactory.getLogger(SSHandler.class);

	public void init() {
		try {
			logger.info("********************Start to init SSL SocketServer************************");
			this.ssf = SysManager.sslManager.getSSLContext("TLS", "JSOFT_LIB").getServerSocketFactory();
			serverSocket = ssf.createServerSocket(Config.rxPort);
			((SSLServerSocket) this.serverSocket).setNeedClientAuth(true);
			logger.info(
					"SSL Socket Server has been created @" + Config.rxPort + " with TLS / JSOFT_LIB need client auth");
			this.pool = Executors.newFixedThreadPool(Config.threadPoolSize);// new ThreadPoolExecutor(16, 64, 5,
																			// TimeUnit.SECONDS, new
			// ArrayBlockingQueue<Runnable>(64));
			logger.info("Thread pool has been created for SSL Socket Server");
			logger.info("********************Success to init SSL SocketServer************************");
		} catch (Exception e) {
			ExceptionHandler.Log(e);
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				this.socket = serverSocket.accept();
				this.socket.setSoTimeout(Config.connectTO);
				this.pool.execute(new SSWorker(socket));
			}
		} catch (Exception e) {
			ExceptionHandler.Log(e);
		}
	}

}
