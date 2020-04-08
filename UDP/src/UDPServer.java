import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UDPServer extends Thread {
	private int numArchivo;

	private DatagramSocket datagramSocket;
	private DatagramPacket inPacket, outPacket;
	private byte[] buffer;

	private final static String[] PATHS = { "./data/100MiB.txt", "./data/250MiB.txt" };
	private File file;
	private FileInputStream fis;
	private DigestInputStream dis;

	public UDPServer(DatagramSocket datagramSocket, DatagramPacket inPacket, int numArchivo) {
		this.datagramSocket = datagramSocket;
		this.numArchivo = numArchivo;
		this.inPacket = inPacket;
	}

	public void run() {
		try {
			String messageIn, messageOut;
			int numMessages = 0;
			InetAddress clientAddress = null;
			int clientPort;
			buffer = new byte[1024];
			clientAddress = inPacket.getAddress();
			clientPort = inPacket.getPort();
			messageIn = new String(inPacket.getData(), 0, inPacket.getLength());
			System.out.print(inPacket.getSocketAddress());
			System.out.print(" : ");
			System.out.println(messageIn);

			numMessages++;
			messageOut = "message" + numMessages + ": " + messageIn;
			outPacket = new DatagramPacket(messageOut.getBytes(), messageOut.length(), clientAddress, clientPort);
			datagramSocket.send(outPacket);

			MessageDigest md = MessageDigest.getInstance("MD5");
			file = new File(PATHS[numArchivo - 1]);
			fis = new FileInputStream(PATHS[numArchivo - 1]);
			dis = new DigestInputStream(fis, md);
			int count = (int) Math.ceil((double) file.length() / 1024.0);
			byte[] numfragmentos = ByteBuffer.allocate(4).putInt(count).array();
			DatagramPacket paqueteEnvio = new DatagramPacket(numfragmentos, numfragmentos.length, clientAddress,
					clientPort);
			// envia paquete 2
			datagramSocket.send(paqueteEnvio);

			// Mandar fragmentos al cliente
			while ((count = dis.read(buffer)) > 0) {
				paqueteEnvio = new DatagramPacket(buffer, count, clientAddress, clientPort);
				datagramSocket.send(paqueteEnvio);
			}

			// Send MD5 Hash
			MessageDigest digest = dis.getMessageDigest();
			dis.close();
			fis.close();
			byte[] md5 = digest.digest();
			paqueteEnvio = new DatagramPacket(md5, md5.length, clientAddress, clientPort);
			datagramSocket.send(paqueteEnvio);
		} catch (IOException | NoSuchAlgorithmException ioEx) {
			ioEx.printStackTrace();
		} finally {
			System.out.println("\n Closing session.. ");
		}
	}
}