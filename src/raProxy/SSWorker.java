package raProxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import raProxy.cfcaUtils.IOUtil;
import raProxy.utils.Config;

public class SSWorker implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(SSWorker.class);

	protected Socket socket;

	public SSWorker(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			logger.info("[FAKE_CA]#SSWorker#: Start to read request");
			byte[] bytes = IOUtil.readLengthValue(this.socket.getInputStream());
			logger.debug("[FAKE_CA]#SSWorker#: " + new String(bytes, "UTF-8"));
			Socket senderSocket = new Socket(Config.txAddr, Config.txPort);
			senderSocket.setSoTimeout(Config.connectTO);
			OutputStream ous = senderSocket.getOutputStream();
			logger.info("[FAKE_CA]#SSWorker#: Start to send request");
			IOUtil.sendLengthValue(ous, bytes);
			InputStream ins = senderSocket.getInputStream();
			logger.info("[FAKE_CA]#SSWorker#: Start to read response");
			byte[] fakeRARes = IOUtil.readLengthValue(ins);
			logger.debug("[FAKE_CA]#SSWorker#: " + new String(fakeRARes, "UTF-8"));
			ous.close();
			ins.close();
			senderSocket.close();
			logger.info("[FAKE_CA]#SSWorker#: Start to send response");
			IOUtil.sendLengthValue(this.socket.getOutputStream(), fakeRARes);
			System.gc();
		} catch (IOException e) {
			ExceptionHandler.Log(e);
		}
	}

}
