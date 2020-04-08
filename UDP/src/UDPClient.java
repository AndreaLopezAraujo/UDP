import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class UDPClient {
	
	private static InetAddress host;
	private static final int PORT = 1234;
	private static DatagramSocket datagramSocket;
	private static DatagramPacket inPacket, outPacket;
	private static byte[] buffer;
	private static File file;
	private static int numRecibidos = 0;
	
	public static void main(String[] args) {
		try {
			host = InetAddress.getLocalHost();
		} catch (UnknownHostException uhEx) {
			System.out.println("HOST ID not found.. ");
			System.exit(1);
		}
		accessServer();
	}

	private static String getFileChecksum(MessageDigest digest, File file) throws IOException {
		// Get file input stream for reading the file content
		FileInputStream fis = new FileInputStream(file);
		// Create byte array to read data in chunks
		byte[] byteArray = new byte[4096];
		int bytesCount = 0;

		// Read file data and update in message digest
		while ((bytesCount = fis.read(byteArray)) != -1) {
			digest.update(byteArray, 0, bytesCount);
		}
		;

		// close the stream; We don't need it now.
		fis.close();

		// Get the hash's bytes
		byte[] bytes = digest.digest();

		// This bytes[] has bytes in decimal format;
		// Convert it to hexadecimal format
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		// return complete hash
		return sb.toString();
	}

	private static void accessServer() {
		try {

			datagramSocket = new DatagramSocket();
			String message = "", response = "";
			System.out.println("Sending connection package...");
			message = "Ready";
			outPacket = new DatagramPacket(message.getBytes(), message.length(), host, PORT);
			datagramSocket.send(outPacket);
			buffer = new byte[1024];
			inPacket = new DatagramPacket(buffer, buffer.length);
			datagramSocket.receive(inPacket);
			response = new String(inPacket.getData(), 0, inPacket.getLength());
			System.out.println(" \n SERVER-->>" + response);

			datagramSocket.setSoTimeout(600);
			// Recibe el numero de Fragmentos
			datagramSocket.receive(inPacket); numRecibidos++;
			ByteBuffer wrapped = ByteBuffer.wrap(inPacket.getData());
			int numFragmentos = wrapped.getInt();
			System.out.println(" \n SERVER-->>" + numFragmentos);
			
			String tamano = numFragmentos < 200 ?  "100MB" : "250MB";
			String archivo = tamano.equalsIgnoreCase("100MiB") ? "100MiB.txt" : "250MiB.txt";
			int localPort = datagramSocket.getLocalPort();
			file = new File("./data/test/testing" + localPort + ".txt");
			OutputStream os = new FileOutputStream(file);
			//TODO LogCliente log = new LogCliente(localPort, archivo, tamano);
			// Recibe cada fragmento del archivo
			for (int i = 0; i < numFragmentos; ++i) {
				datagramSocket.receive(inPacket); numRecibidos++;
				os.write(inPacket.getData(), 0, inPacket.getLength());
			}
			System.out.println("FileRecieved");
			os.close();

			// Recibir Hash y construir Hash del servidor
			datagramSocket.receive(inPacket); numRecibidos++;
			System.out.println("Recieved Checksum");
			StringBuilder hash = new StringBuilder();
			byte[] bytes = inPacket.getData();
			
			long TInicio, TFin, tiempo;
			TInicio = System.currentTimeMillis();
			for (int i = 0; i < inPacket.getLength(); i++) {
				hash.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			TFin = System.currentTimeMillis();
			tiempo = TFin - TInicio;
			// Get Hash del archivo construido localmente
			MessageDigest md5Digest = MessageDigest.getInstance("MD5");
			String checksum = getFileChecksum(md5Digest, file);
			//
			// Chequear integridad
			if (checksum.equals(hash.toString())) {
				System.out.println("File integrity check successful!");

			} else {
				System.out.println("File integrity check unseccessful");
			}
			
			//TODO log.agregarDatos(true, tiempo, numFragmentos, 1, numFragmentos*1024 + 20, message.getBytes().length);
			
			file.delete();

		} catch (SocketTimeoutException e) {
			System.out.println("Loss of packet");
			e.printStackTrace();
			//TODO log.agregarDatos(false, -1, numRecibidos, 1, numRecibidos*1024, 1024);
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
			//log.agregarDatos(false, -1, numRecibidos, 1, numRecibidos*1024, 1024);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			//log.agregarDatos(false, -1, numRecibidos, 1, numRecibidos*1024, 1024);
		}

		finally {
			System.out.println("\n closing connection.... ");
			if(file != null) file.delete();
			datagramSocket.close();
		}

	}
}
