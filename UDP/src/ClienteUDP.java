import java.net.*;
import java.util.Scanner;
import java.io.*;
public class ClienteUDP 
{
	private static LogCliente l;
	
	public static void main(String[] args) {
		  try {
			  int codigo=(int) (Math.random()*1000);
		   // 1. crear el socket por donde se enviara la peticion y se recibira
		   // la respuesta..
			   String archivo="250MiB.txt";
			   String tamaño="250MB";
			   int tamañoP=0;
			   int tamañoPl=0;
		   DatagramSocket socket = new DatagramSocket();

		   // 2. crear datagrama para enviar la info. el datagrama contiene
		   // toda la info necesaria para que llegue el msj
		   System.out.println("Strating Client:");
		   System.out.println("Requesting message:");
		   System.out.println("Id del cliente: "+codigo);
		   String msj = "SendMeAFilePlease,cliente"+codigo; // msj a enviar.
		   String ip = "127.0.0.1";
		   int port = 45000;
		   
		   // Crear datagrama 1
		   DatagramPacket paqueteEnvio = new DatagramPacket(msj.getBytes(),
		     msj.length(), InetAddress.getByName(ip), port);
		   	tamañoP=paqueteEnvio.getLength();
		   	
		   // enviar paquete 1.
		   socket.send(paqueteEnvio);
		   //recibe paquete 2
		   byte[] buffer = new byte[1024];
		   DatagramPacket paqueteServidor = new DatagramPacket(buffer, 1024);
		   socket.receive(paqueteServidor);
		   String[] datos=new String(paqueteServidor.getData()).split(",");
		   if(datos[0].equals("1"))
			{
				archivo="100MiB.txt";
				tamaño="100MB";
			}
		   System.out.println("datos en el paq 2: "+ datos[0]+", "+datos[1]);
			//Log iniatiates recibe Nombrearchivo y Tamaño
		   l=new LogCliente(codigo, archivo, tamaño);
		   

		   // Recibir archivo...
		   // Crear datagrama de recepcion.
		   byte[] resp = new byte[100000000];
		   DatagramPacket paqueteRecibido = new DatagramPacket(resp,
		     resp.length);
		   long TInicio, TFin, tiempo; 
			TInicio = System.currentTimeMillis(); 
		   // 3.2 recibir paquete.
		   socket.receive(paqueteRecibido);
		   //System.out.println(paqueteRecibido);
		   tamañoPl=paqueteRecibido.getLength();
		   System.out.println("numeroPaquetesResividos"+tamañoPl);
		   // 4. mostrar info...
		   System.out.println("Server respondio desde "
		     + paqueteRecibido.getAddress().getHostAddress()
		     + " por el puerto " + paqueteRecibido.getPort());
		   String archivo1=new String(paqueteRecibido.getData()); 
		   String nombre="Archivo"+codigo+".txt";
		   String ruta = "./data/ArchivosEnviados/"+nombre;
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