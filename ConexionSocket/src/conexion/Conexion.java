package conexion;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.net.*;

public class Conexion {

	public static void main(String[] args) {
		try {
			/**Numero del puerto en el que estara escuchando**/
			ServerSocket server = new ServerSocket(5555);
			System.out.println("Servidor iniciado en el puerto 5555");
			
			/**Esperar a que un cliente se conecte*/
			Socket clientSocket = server.accept();
			System.out.println("Cliente conectado: "+server.getInetAddress().getHostAddress());
			
			/**Abrir los flujos de entrada y salida para enviar y recibir datos**/
			InputStream intput = clientSocket.getInputStream();
			OutputStream output = clientSocket.getOutputStream();
			
			/**Recibir y enviar mensajes**/
			BufferedReader reader = new BufferedReader(new InputStreamReader(intput));
			PrintWriter writer = new PrintWriter(output, true);
			
			/**Lee un mensaje del cleinte**/
			String mensajeCliente = reader.readLine();
			System.out.println("Mensaje del cleinte: "+mensajeCliente);
			
			/**Enviar respuesta al cliente**/
			writer.println("Mensaje recibido");
			
			
			/**Cerrar los puertos despues de la comunicacion**/
			server.close();
			clientSocket.close();
			
		} catch (IOException e) {
			System.out.println("A ocurrido un error: "+e.getMessage());
			e.printStackTrace();
		} 
	}

}
