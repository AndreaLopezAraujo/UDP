

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;


public class ServerThread extends Thread {

	protected DatagramSocket socketServidor;
	protected FileInputStream fis;
	private int numArchivo;
	String line;
	String holder;
	String clientWord;
	int bytNumber;
	String tamArchivo = "";
	final String[] PATHS = {"./data/100MiB.txt", "./data/250MiB.txt"};

	public ServerThread(DatagramSocket dSocket, int NumArchivo) throws SocketException {
		super("test");
		socketServidor = new DatagramSocket(4445);
		this.numArchivo = NumArchivo;	
		line = null;
		holder = null;
		clientWord = null;
		bytNumber = 0;
		fis = null;
		//Solo se usa para meter.
		tamArchivo = numArchivo == 1 ? "100MB" : "250MB";
	}
	

	public void run() {
		try {			
			byte[] bufferEnvio = new byte[1024];
			byte[] bufferRecept = new byte[1024];
			
			
			DatagramPacket paqueteServidor = new DatagramPacket(bufferRecept, bufferRecept.length);
			// 2.2 recibir el paquete. operacion bloqueante.
			socketServidor.receive(paqueteServidor);
			String msj = new String(paqueteServidor.getData());
			System.out.println("recibe este mensaje:" + new String(msj));

			//Client's Address and port 
			InetAddress addr = paqueteServidor.getAddress();// la misma del
			int port = paqueteServidor.getPort();
			File file = new File(PATHS[numArchivo-1]);
			fis = new FileInputStream(PATHS[numArchivo-1]);
			int count = (int) Math.ceil((double) file.length()/1024.0);
			byte[] numfragmentos = ByteBuffer.allocate(4).putInt(count).array();
			DatagramPacket paqueteEnvio = new DatagramPacket(numfragmentos,numfragmentos.length, addr, port);
			//envia paquete 2
			socketServidor.send(paqueteEnvio);
			//recibe confirmacion de recibido para empezar a mandar archivo
			paqueteServidor.setLength(paqueteServidor.getData().length);
			socketServidor.receive(paqueteServidor);
			msj = new String(paqueteServidor.getData(), paqueteServidor.getOffset(), paqueteServidor.getLength());
			System.out.println("msj: "+msj);
			
			if(msj.equals("OK1")) {
				
				while((count = fis.read(bufferRecept)) > 0) {
					paqueteEnvio = new DatagramPacket(bufferRecept, bufferRecept.length, addr, port);
					socketServidor.send(paqueteEnvio);
				}
				System.out.println("count"+count);
				// recibir confirmacion 2
				socketServidor.receive(paqueteServidor);
				System.out.println("Confirmacion: " + new String(paqueteServidor.getData()));	
			}
			fis.close();
			

		} catch (IOException ex) {
			System.out.println("Server exception: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

}
