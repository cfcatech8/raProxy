package raProxy;

import java.io.OutputStream;
import java.net.Socket;

import javax.net.ssl.SSLSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import raProxy.cfcaUtils.CATK;
import raProxy.cfcaUtils.IOUtil;
import raProxy.utils.Config;

public class SocketWorker implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(SocketWorker.class);

	protected Socket socket;

	public SocketWorker(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			logger.info("[FAKE_RA]#SocketWorker#: Start to read request");
			byte[] reqBytes = IOUtil.readLengthValue(this.socket.getInputStream());
			logger.debug("[FAKE_RA]#SocketWorker#: " + new String(reqBytes, "UTF-8"));
			logger.info("[FAKE_RA]#SocketWorker#: Start to connect " + Config.txAddr + ":" + Config.txPort);
			SSLSocket sslSocket = (SSLSocket) SysManager.ssf.createSocket(Config.txAddr, Config.txPort);
			sslSocket.setReuseAddress(true);
			sslSocket.setUseClientMode(true);
			sslSocket.setSoTimeout(Config.connectTO);
			sslSocket.startHandshake();
			logger.info("[FAKE_RA]#SocketWorker#: Already connected to" + Config.txAddr + ":" + Config.txPort);
			logger.info("[FAKE_RA]#SocketWorker#: Start to send request and recieve response");
			byte[] resBytes = CATK.sendAndReceive(sslSocket, reqBytes);
			logger.debug("[CA_RES][SocketWorker]: " + new String(resBytes, "UTF-8"));
			logger.info("[FAKE_RA]#SocketWorker#: Start to send response");
			OutputStream ous = this.socket.getOutputStream();
			IOUtil.sendLengthValue(ous, resBytes);
			System.gc();
		} catch (Exception e) {
			ExceptionHandler.Log(e);
		}
	}
}
