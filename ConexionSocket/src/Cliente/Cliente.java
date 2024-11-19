package Cliente;
import java.io.*;
import java.net.*;

public class Cliente {

	public static void main(String[] args) {
		try {
			/**localhost para conectar al mismo puerto 5555**/
			Socket socket = new Socket("172.16.40.203",5555);
			System.out.println("Conectado al servidor en el puerto 5555");
			
			/**abrir flujos de entrada y salida para enviar los datos**/
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
			
			/**Enviar y recibir mensaje**/
			writer.println("Hola desde el cliente");
			
			String respuestaServer = reader.readLine();
			System.out.println("Respuesta del servidor: "+respuestaServer);
			
			socket.close();
			
			
		} catch (IOException e) {
			System.out.println("A ocurrido un error: "+e.getMessage());
			e.printStackTrace();
		}
		
		
		
	}

}
