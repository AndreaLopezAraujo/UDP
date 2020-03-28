import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.*;
public class ServerUDP {
	static List<Socket> clients = new ArrayList<Socket>();  
	static BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
	static int x = 0;
	public static void main(String[] args) 
	{

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
 
		   // 2.3 leer el paquete como string...
		   String msj = new String(paqueteCliente.getData());
		   String[] datos=msj.split(",");
		   System.out.println("desde "
		     + paqueteCliente.getAddress().getHostAddress()
		     + " desde el puerto " + paqueteCliente.getPort()
		     + " se recibio:" + msj);

		   // 3. enviar respuesta..
		   String resp = new Date().toString();// la hora como respuesta.
		   // 3.1 crear datagrama de envio.
		   // direccion destino..
		   InetAddress addr = paqueteCliente.getAddress();// la misma del
		            // cliente.
		   int port = paqueteCliente.getPort();
		   envio(datos[0],datos[1],addr,port, socket);
		   // el datagrama contiene la información del destino.
		   
		   //4. cerrar el socket...

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
	                    	
	                    	if(valor1 > 10)
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