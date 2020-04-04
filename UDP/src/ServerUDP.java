import java.net.*;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.io.*;
public class ServerUDP {
	static List<Socket> clients = new ArrayList<Socket>();  
	static BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
	static int x = 0;
	public static void main(String[] args) 
	{
		Scanner reader = new Scanner(System.in);
		int numArchivo = 0;
		int numClientes = 0;
		String archivo="250MiB.txt";
		String tamArchivo="";
		int tamPaquete=0;
		int tamanoPl=0;
		final String[] PATHS = {"./data/100MiB.txt", "./data/250MiB.txt"};
		try 
		{
			
			System.out.println("Numero de archivo(100->1;250->2):");
			numArchivo = Integer.parseInt(reader.nextLine());
			tamArchivo = numArchivo == 1 ? "100MB" : "250MB";
			System.out.println("Numero de clientes:");
			numClientes = Integer.parseInt(reader.nextLine());
			
			
			System.out.println("server creado........");
			// 1. crear el servidor..
			DatagramSocket socketServidor = new DatagramSocket(5555);
			
			// 2. recibir mensaje desde el cliente...
			// 2.1 crear el paquete donde se recibe el mensaje.
			byte[] bufferEnvio = new byte[1024];
			byte[] bufferRecept = new byte[1024];
			while (true){
				DatagramPacket paqueteServidor = new DatagramPacket(bufferRecept, bufferRecept.length);
				// 2.2 recibir el paquete. operacion bloqueante.
				System.out.println("socket esperando....");
				socketServidor.receive(paqueteServidor);
	
				//Leer paquete 1
				String msj = new String(paqueteServidor.getData());
				System.out.println("recibe este mensaje:" + new String(msj));
	
				//Client's Address and port 
				InetAddress addr = paqueteServidor.getAddress();// la misma del
				int port = paqueteServidor.getPort();
				File file = new File(PATHS[numArchivo-1]);
				FileInputStream fis = new FileInputStream(PATHS[numArchivo-1]);
				int count = (int) Math.ceil((double) file.length()/1024.0);
				byte[] numfragmentos = ByteBuffer.allocate(4).putInt(count).array();
				
				DatagramPacket paqueteEnvio = new DatagramPacket(numfragmentos,numfragmentos.length, addr, port);
				socketServidor.send(paqueteEnvio);
				
				while((count = fis.read(bufferRecept)) > 0) {
					paqueteEnvio = new DatagramPacket(bufferRecept, bufferRecept.length, addr, port);
					socketServidor.send(paqueteEnvio);
				}
				// enviar paquete 2
				socketServidor.receive(paqueteServidor);
				System.out.println("Confirmacion: " + new String(paqueteServidor.getData()));	
	
				fis.close();
	
			}
			//4. cerrar el socket...
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}