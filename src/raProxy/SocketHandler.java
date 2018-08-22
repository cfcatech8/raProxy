package raProxy;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import raProxy.utils.Config;

//SSL Socket Hander
//接收RA请求
public class SocketHandler implements Runnable {
	protected Socket socket;
	protected ServerSocket serverSocket;
	protected ExecutorService pool;
	private static Logger logger = LoggerFactory.getLogger(SocketHandler.class);

	public void init() {
		try {
			logger.info("********************Start to init SocketServer************************");
			this.serverSocket = new ServerSocket(Config.rxPort);
			logger.info("Socket Server has been created @" + Config.rxPort);
			this.pool = Executors.newFixedThreadPool(Config.threadPoolSize);// new ThreadPoolExecutor(16, 64, 5,
																			// TimeUnit.SECONDS, new
			// ArrayBlockingQueue<Runnable>(64));
			logger.info("Thread pool has been created for Socket Server");
			logger.info("********************Success to init SocketServer************************");
		} catch (Exception e) {
			ExceptionHandler.Log(e);
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				this.socket = this.serverSocket.accept();
				this.socket.setSoTimeout(Config.connectTO);
				this.pool.execute(new SocketWorker(socket));
			}
		} catch (Exception e) {
			ExceptionHandler.Log(e);
		}
	}

}
