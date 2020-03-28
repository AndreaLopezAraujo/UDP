import java.net.*;
import java.util.Scanner;
import java.io.*;
public class ClienteUDP 
{
	private static LogCliente l;
	
	public static void main(String[] args) {
		  try {
			  int codigo=(int) (Math.random()*100);
		   // 1. crear el socket por donde se enviara la peticion y se recibira
		   // la respuesta..
			   Scanner reader = new Scanner(System.in);
			   String numero = "";
			   String archivo="250MiB.txt";
			   String tamaño="250MB";
			   int tamañoP=0;
			   int tamañoPl=0;
		   DatagramSocket socket = new DatagramSocket();

		   // 2. crear datagrama para enviar la info. el datagrama contiene
		   // toda la info necesaria para que llegue el msj
		   System.out.println("Numero de archivo(100->1;250->2):");
		   numero = reader.nextLine();
		   System.out.println("Numero de clientes:");
		   numero = numero+","+reader.nextLine()+",";
		   String[] datos=numero.split(",");
		   if(datos[0].equals("1"))
		   {
			   archivo="100MiB.txt";
			   tamaño="100MB";
		   }
		   l=new LogCliente(codigo, archivo, tamaño);
		   String msj = numero; // msj a enviar.
		   String ip = "127.0.0.1";
		   int port = 45000;
		   // 2.1 crear datagrama
		   long TInicio, TFin, tiempo; 
			TInicio = System.currentTimeMillis(); 
		   DatagramPacket paqueteEnvio = new DatagramPacket(msj.getBytes(),
		     msj.length(), InetAddress.getByName(ip), port);
		   	tamañoP=paqueteEnvio.getLength();
		   // 2.2 enviar paquete.
		   socket.send(paqueteEnvio);

		   // 3. recibir respuesta...
		   // 3.1 crear datagrama de recepcion.
		   byte[] resp = new byte[1024];
		   DatagramPacket paqueteRecibido = new DatagramPacket(resp,
		     resp.length);
		   
		   // 3.2 recibir paquete.
		   socket.receive(paqueteRecibido);
		   tamañoPl=paqueteRecibido.getLength();
		   // 4. mostrar info...
		   System.out.println("Server respondio desde "
		     + paqueteRecibido.getAddress().getHostAddress()
		     + " por el puerto " + paqueteRecibido.getPort());
		   String archivo1=new String(paqueteRecibido.getData()); 
		   String nombre="Archivo"+codigo+".txt";
		   String ruta = "./data/"+nombre;
           File file = new File(ruta);
           FileWriter fw = new FileWriter(file);
           BufferedWriter bw = new BufferedWriter(fw);
           bw.write(archivo1);
           bw.close();
           TFin = System.currentTimeMillis();
           tiempo = TFin - TInicio;
           l.agregarDatos(true, tiempo, tamañoPl, tamañoP, tamañoPl, tamañoP);
		   // 5. cerrar
		   socket.close();

		  } catch (IOException e) {
		   e.printStackTrace();
		  }

		 }
}