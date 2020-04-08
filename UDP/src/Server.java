import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import java.io.*;

public class Server {
	static List<Socket> clients = new ArrayList<Socket>();
	static BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
	static int x = 0;
	static DatagramSocket datagramSocket;

	public static void main(String[] args) {
		Scanner reader = new Scanner(System.in);
		int numArchivo = 0;
		int numClientes = 0;
		int actClientes = 0;
		Stack<UDPServer> threads = new Stack<>();

		try {
			numArchivo = Integer.parseInt(args[0]);
			System.out.println("Numero de archivo(100->1;250->2): " + numArchivo);
			numClientes = Integer.parseInt(args[1]);
			System.out.println("Numero de clientes: " + numClientes);
			System.out.println("server creado........");
			if (numArchivo < 1 || numArchivo > 2)
				throw new Exception("solo puede ser 1 o 2 el num de archivo");
			if (numClientes > 25)
				throw new Exception("solo puede ser max 25 clientes concurrentes");
			// 2. recibir mensaje desde el cliente...
			// 2.1 crear el paquete donde se recibe el mensaje.
			System.out.println("Server listening on port " + 1234);
			datagramSocket = new DatagramSocket(1234);
			byte[] buffer = new byte[1024];
			do {
				DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
				datagramSocket.receive(inPacket);
				threads.add(new UDPServer(datagramSocket, inPacket, numArchivo));
				actClientes++;
				if (actClientes == numClientes) {
					actClientes = 0;
					while (!threads.isEmpty()) {
						threads.pop().start();
					}
				}
			} while (true);
			// 4. cerrar el socket...
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}