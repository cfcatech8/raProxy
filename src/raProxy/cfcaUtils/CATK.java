package raProxy.cfcaUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class CATK {

	public static byte[] sendAndReceive(Socket sslSocket, byte[] requestBytes) {
		DataOutputStream dataOutputStream = null;
		DataInputStream dataInputStream = null;
		byte[] responseBytes = null;
		try {
			try {
				dataOutputStream = new DataOutputStream(sslSocket.getOutputStream());
				IOUtil.sendLengthValue(dataOutputStream, requestBytes);
				dataInputStream = new DataInputStream(sslSocket.getInputStream());
				dataOutputStream.flush();
			} catch (IOException ex) {
				ex.printStackTrace();
				return null;
			}
			try {
				responseBytes = IOUtil.readLengthValue(dataInputStream);
			} catch (IOException ex) {
				ex.printStackTrace();
				return null;
			}
			return responseBytes;
		} finally {
			try {
				if (dataInputStream != null) {
					dataInputStream.close();
				}
				if (dataOutputStream != null) {
					dataOutputStream.close();
				}
				if (sslSocket != null) {
					sslSocket.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				return null;
			}
		}
	}
}
