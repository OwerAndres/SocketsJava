package conexion;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.net.*;

public class Conexion {

	private static final int PORT = 5555;
	/**Variable del numero de clientes que esperara el servidor**/
	private static final int NUM_CLIENTES = 2; 

	public void iniciarServidor() {
		List<Socket> clientes = new ArrayList<>();

		try (ServerSocket server = new ServerSocket(PORT)) {
			System.out.println("Servidor iniciado en el puerto " + PORT);

			/**El bucle continuara hasta que se hallan conectado 2 clientes**/
			while (clientes.size() < NUM_CLIENTES) {
				System.out.println("Esperando conexión de cliente " + (clientes.size() + 1) + "...");
				Socket clientSocket = server.accept();
				clientes.add(clientSocket);
				System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());
			}

			System.out.println("Se han conectado " + NUM_CLIENTES + " clientes. Iniciando comunicación...");

			/**Recorrer la lista de clientes e iniciar la comunicacion con cada uno**/
			for (Socket clientSocket : clientes) {
				InputStream input = clientSocket.getInputStream();
				OutputStream output = clientSocket.getOutputStream();

				BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				PrintWriter writer = new PrintWriter(output, true);

				/**Leer el mensaje de cada cliente**/
				String mensajeCliente = reader.readLine();
				System.out.println("Mensaje del cliente " + clientSocket.getInetAddress().getHostAddress() + ": "
						+ mensajeCliente);

				writer.println("Mensaje recibido del servidor");
			}

			for (Socket clientSocket : clientes) {
				clientSocket.close();
			}

			System.out.println("Conexión con los clientes finalizada.");

		} catch (IOException e) {
			System.out.println("Ha ocurrido un error: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
