import java.net.*;
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
		String numero = "";
		String archivo="250MiB.txt";
		String tamaño="250MB";
		int tamañoP=0;
		int tamañoPl=0;

		try 
		{
			System.out.println("server creado........");
			// 1. crear el servidor..
			DatagramSocket socket = new DatagramSocket(45000);

			// 2. recibir mensaje desde el cliente...

			// 2.1 crear el paquete donde se recibe el mensaje.
			byte[] buffer = new byte[1024];
			DatagramPacket paqueteCliente = new DatagramPacket(buffer, 1024);
			// 2.2 recibir el paquete. operacion bloqueante.
			System.out.println("socket esperando....");
			socket.receive(paqueteCliente);

			//Leer paquete 1
			String msj = new String(paqueteCliente.getData());
			System.out.println("recibe de ip: "
					+ paqueteCliente.getAddress().getHostAddress()
					+ " desde el puerto " + paqueteCliente.getPort()
					+ " este mensaje:" + new String(msj));


			System.out.println("Numero de archivo(100->1;250->2):");
			numero = reader.nextLine();
			System.out.println("Numero de clientes:");
			numero = numero+","+reader.nextLine()+",";
			String[] datos=numero.split(",");
			

			//Client's Address and port 
			InetAddress addr = paqueteCliente.getAddress();// la misma del
			int port = paqueteCliente.getPort();
			msj=numero;
			DatagramPacket paqueteEnvio = new DatagramPacket(msj.getBytes(),
					msj.length(), addr, port);
			
			// enviar paquete 2
			socket.send(paqueteEnvio);
			System.out.println("se envio paq 2.");


			envio(datos[0],datos[1],addr,port, socket);
			// el datagrama contiene la información del destino.




			//4. cerrar el socket...
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void envio(String fileSelection,String numClientes,InetAddress addr,int port,DatagramSocket socket) throws IOException
	{
		int valor1=Integer.parseInt(numClientes);
		for(int x = 0; x < valor1;x++)
		{
			try {

				if(valor1 < 10)
				{
					System.out.println("Numero de clientes menor al especificado.");
				}
				else if(Integer.parseInt(fileSelection) == 1)
				{
					FileInputStream fr1 = new FileInputStream("./data/100MiB.txt");
					FileReader fr = new FileReader ("./data/100MiB.txt");
					byte b[] = new byte[8192];
					int valor=fr1.read(b, 0, b.length);
					BufferedReader br = new BufferedReader(fr);
					String v=br.readLine();
					String linea="";
					while(v!=null)
					{
						linea = linea+v;
						v=br.readLine();
					}
					//System.out.println(linea);
					fr.close();
					fr1.close();  
					DatagramPacket paqueteEnvio = new DatagramPacket(linea.getBytes(),
							valor, addr, port);

					// 3.2 enviar paquete...
					socket.send(paqueteEnvio);
					System.out.println("se envio");
				}
				else if(Integer.parseInt(fileSelection) == 2 )
				{
					//InputStream input = clients.get(x).getInputStream();  	                   

					FileInputStream fr = new FileInputStream("./data/250MiB.txt");
					FileReader fr1 = new FileReader ("./data/250MiB.txt");
					byte b[] = new byte[8192];
					int valor=fr.read(b, 0, b.length);
					BufferedReader br = new BufferedReader(fr1);
					String v=br.readLine();
					String linea="";
					while(v!=null)
					{
						linea = linea+v;
						v=br.readLine();
					}
					fr.close();
					fr1.close();  
					DatagramPacket paqueteEnvio = new DatagramPacket(linea.getBytes(),
							valor, addr, port);

					// 3.2 enviar paquete...
					socket.send(paqueteEnvio); 
				}
				else
				{
					System.out.println("Opcion incorrecta");
				}

			}
			catch (IOException e) {
			}

		}
	}
}