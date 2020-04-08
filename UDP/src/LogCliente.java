import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Date;

public class LogCliente 
{
	private Date fecha;
	private String nombreArchivo;
	private String tamanoArchivo;
	private boolean exitosa;
	private double tiempoTrasferencia;
	private double numeroPaquetesResividos;
	private double numeroPaquetesEnviados;
	private double numeroBytesResividos;
	private double numeroBytesEnviados;
	private int numeroCliente;
	
	public LogCliente(int NumeroCliente,
					String nombreArchivo,
					String tamanoArchivo)
	{
		this.numeroCliente=NumeroCliente;
		this.nombreArchivo=nombreArchivo;
		this.tamanoArchivo=tamanoArchivo;
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
		String filePath= "./data/Logs/LogClientes.txt";
		String msg = "Fecha: "+fecha + "\n"+
				"Cliente: "+numeroCliente + "\n"+
				"Nombre archivo: "+nombreArchivo + "\n" +
				"Tamaño archivo: "+tamanoArchivo + "\n" + 
				"Tiempo de envio: "+tiempoTrasferencia + "\n" +
				"Exito en envio: "+exitosa + "\n" +
				"Numero paquetes enviados: "+numeroPaquetesEnviados + "\n" +
				"Numero paquetes resividos: "+numeroPaquetesResividos + "\n"
				;
		try {
		    final Path path = Paths.get(filePath);
		    Files.write(path, Arrays.asList("New line to append"), StandardCharsets.UTF_8,
		        Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
		} catch (final IOException ioe) {
		    System.out.println("Logging error with client " + numeroCliente);
		}
	}
	
}
