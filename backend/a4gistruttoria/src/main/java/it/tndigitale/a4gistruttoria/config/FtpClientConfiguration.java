package it.tndigitale.a4gistruttoria.config;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class FtpClientConfiguration {

	private FTPClient ftp;

	public FtpClientConfiguration() {
		super();
	}

	public void open(String server, int port, String user, String password) throws IOException {
		ftp = new FTPClient();

		ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

		ftp.connect(server, port);
		int reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			throw new IOException("Exception in connecting to FTP Server");
		}

		ftp.login(user, password);
	}

	public void close() throws IOException {
		ftp.disconnect();
	}

	public void uploadFile(InputStream input, String path) throws IOException {
		ftp.storeFile(path, input);
	}

	public void downloadFile(String source, String destination) throws IOException {
		FileOutputStream out = new FileOutputStream(destination);
		ftp.retrieveFile(source, out);
	}
}
