package raProxy;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionHandler {
	private static Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

	public static void Log(Exception e) {
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
		} finally {
			if (sw != null) {
				try {
					sw.close();
				} catch (Exception e2) {
					logger.error(e2.getMessage());
					e2.printStackTrace();
				}
			}
			if (pw != null) {
				pw.close();
			}
			logger.error(sw.toString());

		}
	}
}
