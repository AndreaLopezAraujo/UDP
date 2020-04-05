import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;


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
		Stack<ServerThread> threads = new Stack<>();
		
		try (DatagramSocket socketServidor = new DatagramSocket(5555))
		{
			System.out.println("Numero de archivo(100->1;250->2):");
			numArchivo = Integer.parseInt(reader.nextLine());
			System.out.println("Numero de clientes:");
			numClientes = Integer.parseInt(reader.nextLine());
			System.out.println("server creado........");
			if(numArchivo<1 || numArchivo>2)
				throw new Exception( "solo puede ser 1 o 2 el num de archivo" );
			if(numClientes>25 )
				throw new Exception( "solo puede ser max 25 clientes concurrentes" );
			// 2. recibir mensaje desde el cliente...
			// 2.1 crear el paquete donde se recibe el mensaje.
			System.out.println("Server listening on port " + 5555);
			while (true){
				ServerThread st=new ServerThread(socketServidor,numArchivo);
				threads.add(st);
				if(threads.size() >= numClientes){
						while(threads.size() > 0) {
						threads.pop().start();
					}
				}
			}
			//4. cerrar el socket...
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}