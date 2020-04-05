import java.net.*;
import java.nio.ByteBuffer;
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
			   String tamano="250MB";
			   int tamanoP=0;
			   int tamanoPl=0;
		   DatagramSocket socket = new DatagramSocket();

		   // 2. crear datagrama para enviar la info. el datagrama contiene
		   // toda la info necesaria para que llegue el msj
		   System.out.println("Strating Client:");
		   System.out.println("Requesting message:");
		   System.out.println("Id del cliente: "+codigo);
		   String msj = "SendMeAFilePlease,cliente"+codigo; // msj a enviar.
		   String ip = "127.0.0.1";
		   int port = 5555;
		   
		   byte[] bufferEnvio = new byte[1024];
		   byte[] bufferRecept = new byte[1024];
		   
		   // Crear datagrama 1
		   DatagramPacket paqueteEnvio = new DatagramPacket(msj.getBytes(), msj.length(), InetAddress.getByName(ip), port);
		   socket.send(paqueteEnvio); //Envio el mensaje para indicar que le envie el archivo
		   tamanoP=paqueteEnvio.getLength();
		   	
		   // enviar paquete 1.
		   //recibe paquete 2
		   DatagramPacket paqueteServidor = new DatagramPacket(bufferRecept, bufferRecept.length);
		   socket.receive(paqueteServidor); //Recibe el numero de fragmentos
		   ByteBuffer wrapped = ByteBuffer.wrap(bufferRecept);
		   int numFragmentos = wrapped.getInt();

		   //Contar tiempo
		   long TInicio, TFin, tiempo; 
		   TInicio = System.currentTimeMillis(); 
		   //Recibir numero de fragmentos
		   tamano = numFragmentos*1024/1000000 + "B";
		   //envia confirmacion de recibido
		   msj="OK1";
		   System.out.println("msj: "+msj);
		   paqueteEnvio = new DatagramPacket(msj.getBytes(), msj.length(), InetAddress.getByName(ip), port);
		   socket.send(paqueteEnvio); 
		   // Recibir archivo...
		   int numeroPaquetesResividos = 0;
		   for(int i = 0; i < numFragmentos; i++) {
			   socket.receive(paqueteServidor);
			   //System.out.println(new String(paqueteServidor.getData()));
			   //System.out.println("indice "+ i);
			   numeroPaquetesResividos++;
		   }
		   
		   //Log iniatiates recibe Nombrearchivo y Tamaño
		   l=new LogCliente(codigo, archivo, tamano);
		   		   
		   
		   //Tiempo De transimision
		   TFin = System.currentTimeMillis();
		   tiempo = TFin - TInicio;
		   
		   //Envio Confirmacion TODO falta hash
		   msj = "OK";
		   paqueteEnvio = new DatagramPacket(msj.getBytes(), msj.length(), InetAddress.getByName(ip), port);
		   socket.send(paqueteEnvio);
		   
		   tamanoPl=paqueteServidor.getLength();
		   
		   System.out.println("numeroPaquetesResividos"+numeroPaquetesResividos);
		   // 4. mostrar info...
		   
		   //String archivo1=new String(paqueteServidor.getData()); 
		   String nombre="Archivo"+codigo+".txt";
		   String ruta = "./data/ArchivosEnviados/"+nombre;
           File file = new File(ruta);
           FileWriter fw = new FileWriter(file);
           BufferedWriter bw = new BufferedWriter(fw);
           //bw.write(archivo1);
           bw.close();
           l.agregarDatos(true, tiempo, tamanoPl, tamanoP, tamanoPl, tamanoP);
		   // 5. cerrar
		   socket.close();

		  } catch (IOException e) {
		   e.printStackTrace();
		  }

		 }
}