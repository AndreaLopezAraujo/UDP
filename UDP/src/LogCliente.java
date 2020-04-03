import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;

public class LogCliente 
{
	private Date fecha;
	private String nombreArchivo;
	private String tamañoArchivo;
	private boolean exitosa;
	private double tiempoTrasferencia;
	private double numeroPaquetesResividos;
	private double numeroPaquetesEnviados;
	private double numeroBytesResividos;
	private double numeroBytesEnviados;
	private int numeroCliente;
	
	public LogCliente(int NumeroCliente,
					String nombreArchivo,
					String tamañoArchivo)
	{
		this.numeroCliente=NumeroCliente;
		this.nombreArchivo=nombreArchivo;
		this.tamañoArchivo=tamañoArchivo;
		fecha = new Date();
	}
	public void agregarDatos(boolean exitosa,
							double tiempoTrasferencia,
							double numeroPaquetesResividos,
							double numeroPaquetesEnviados,
							double numeroBytesResividos,
			double numeroBytesEnviados)
	{
		this.exitosa=exitosa;
		this.tiempoTrasferencia=tiempoTrasferencia;
		this.numeroPaquetesResividos=numeroPaquetesResividos;
		this.numeroPaquetesEnviados=numeroPaquetesEnviados;
		this.numeroBytesResividos=numeroBytesResividos;
		this.numeroBytesEnviados=numeroBytesEnviados;
		try {
			CrearArchivo();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void CrearArchivo() throws FileNotFoundException
	{
		System.out.println("entra a crear archivo");
		String nombre= "./data/Logs/LogCliente"+ numeroCliente+".txt";
		File file = new File (nombre);
		PrintWriter doc = new PrintWriter (file);
		doc.println("Fecha: "+fecha);
		doc.println("Nombre archivo: "+nombreArchivo);
		doc.println("Tamaño archivo: "+tamañoArchivo);
		doc.println("Tiempo de envio: "+tiempoTrasferencia);
		doc.println("Exito en envio: "+exitosa);
		doc.println("Numero paquetes enviados: "+numeroPaquetesEnviados);
		doc.println("Numero paquetes resividos: "+numeroPaquetesResividos);
		doc.close();
	}
	
}
