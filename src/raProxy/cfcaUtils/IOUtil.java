package raProxy.cfcaUtils;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class IOUtil {

	public static byte[] addLength(byte[] bytes) {
		int totalLength = bytes.length + 4;

		byte[] lengthedArray = new byte[totalLength];

		System.arraycopy(intToByteArray(bytes.length), 0, lengthedArray, 0, 4);
		System.arraycopy(bytes, 0, lengthedArray, 4, bytes.length);

		return lengthedArray;
	}

	public static byte[] readLengthValue(InputStream inputStream) throws IOException {
		int receiveLength = readInt(inputStream);
		int bufferSize = receiveLength < 4096 ? receiveLength : 4096;
		byte[] read = read(inputStream, receiveLength, bufferSize);
		return read;
	}

	public static byte[] read(InputStream inputStream, int bufferSize) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[bufferSize];
		int num = inputStream.read(buffer);
		while (num != -1) {
			baos.write(buffer, 0, num);
			num = inputStream.read(buffer);
		}
		baos.flush();
		return baos.toByteArray();
	}

	public static void sendLengthValue(OutputStream outputStream, byte[] sendBytes) throws IOException {
		outputStream.write(addLength(sendBytes));
		outputStream.flush();
	}

	public static byte[] read(InputStream inputStream, int length, int bufferSize) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[bufferSize];
		int totalNum = 0;
		int num = 0;
		int readLength = bufferSize;
		while (totalNum < length) {
			num = inputStream.read(buffer, 0, readLength);
			if (num <= 0) {
				break;
			}
			baos.write(buffer, 0, num);
			totalNum += num;
			readLength = length - totalNum > bufferSize ? bufferSize : length - totalNum;
		}
		baos.flush();

		return baos.toByteArray();
	}

	public static byte[] intToByteArray(int i) {
		byte[] intBytes = new byte[4];
		intBytes[0] = ((byte) (i >> 24 & 0xFF));
		intBytes[1] = ((byte) (i >> 16 & 0xFF));
		intBytes[2] = ((byte) (i >> 8 & 0xFF));
		intBytes[3] = ((byte) (i & 0xFF));
		return intBytes;
	}

	public static int readInt(InputStream inputStream) throws IOException {
		int ch1 = inputStream.read();
		int ch2 = inputStream.read();
		int ch3 = inputStream.read();
		int ch4 = inputStream.read();
		if ((ch1 | ch2 | ch3 | ch4) < 0) {
			throw new EOFException();
		}
		return (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0);
	}

	public static Socket createNewSocket(String host, int port, int connectTimeout) throws IOException {
		return createSocket(host, port, connectTimeout, connectTimeout);
	}

	public static Socket createSocket(String host, int port, int connectTimeout, int readTimeout) throws IOException {
		Socket socket = new Socket();
		socket.setReuseAddress(true);
		socket.setSoLinger(true, 0);
		if (readTimeout > 0) {
			socket.setSoTimeout(readTimeout);
		}
		socket.connect(new InetSocketAddress(host, port), connectTimeout);
		return socket;
	}
}
