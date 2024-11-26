package Cliente;

import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

public class Cliente {

	// Declaración de variables estáticas para gestionar la conexión y la
	// interacción

	static Socket socket; // Representa el socket de conexión al servidor

	static BufferedReader reader; // Para leer datos del servidor

	static PrintWriter writer; // Para enviar datos al servidor

	static String usuario, Contraseña, Contraseñadenuevo, mensajeServidor; // Variables para gestionar los datos del
																			// usuario

	public static void main(String[] args) {

		try {

			// Establece la conexión con el servidor en la IP y puerto especificados

			socket = new Socket("172.16.40.96", 5555);

			reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Para leer datos del servidor

			writer = new PrintWriter(socket.getOutputStream(), true); // Para enviar datos al servidor

			// Hilo para escuchar mensajes del servidor en segundo plano

			new Thread(new Runnable() {

				@Override

				public void run() {

					escucharServidor(); // Llama al método que escucha los mensajes del servidor

				}

			}).start(); // Inicia el hilo de escucha en paralelo

			System.out.println("Conectando al servidor 5555"); // Mensaje que indica la conexión al servidor

			// Menú principal donde el usuario elige entre iniciar sesión o registrarse

			while (true) {

				String[] inicio = { "Inicio Sesión", "Registrarse" }; // Opciones de menú

				int seleccion = JOptionPane.showOptionDialog(null, "Selecciona una opción:", "Menú de opciones",

						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, inicio, inicio[0]);

				// Procesa la opción seleccionada

				switch (seleccion) {

				case 0 -> { // Opción "Inicio Sesión"

					writer.println("Inicio Sesion"); // Informa al servidor que el usuario quiere iniciar sesión

					InicioSesion(); // Llama al método para gestionar el inicio de sesión

					String verificar = reader.readLine(); // Lee la respuesta del servidor

					if (verificar.contains("sesion Iniciada")) {

						while (true) {

							// Si la sesión fue iniciada, muestra un mensaje de advertencia

							JOptionPane.showMessageDialog(null, "Contraseña Incorrecta", "Advertencia",

									JOptionPane.WARNING_MESSAGE);

						}

					} else {

						// Si la sesión no fue iniciada, se puede procesar otro flujo

					}

				}

				case 1 -> { // Opción "Registrarse"

					writer.println("Registrarse"); // Informa al servidor que el usuario quiere registrarse

					Registrarse(); // Llama al método para gestionar el registro de un nuevo usuario

				}

				default -> {

					// Si no se selecciona ninguna opción, muestra una advertencia

					JOptionPane.showMessageDialog(null, "No seleccionaste ninguna opción.", "Información",

							JOptionPane.WARNING_MESSAGE);

				}

				}

			}

		} catch (IOException e) {

			e.printStackTrace(); // Muestra el error si ocurre alguna excepción de entrada/salida

		}

	}

	// Método para escuchar mensajes del servidor en segundo plano (hilo)

	public static void escucharServidor() {

		try {

			while (true) {

				String mensaje = reader.readLine(); // Lee el mensaje enviado por el servidor

				if (mensaje != null) {

					// Muestra el mensaje recibido en la consola

					System.out.println("Mensaje del servidor: " + mensaje);

				}

			}

		} catch (IOException e) {

			// Muestra un error si ocurre algún problema al escuchar los mensajes

			System.out.println("Error al escuchar mensajes del servidor: " + e.getMessage());

		}

	}

	// Método para gestionar el inicio de sesión del usuario

	public static void InicioSesion() throws IOException {

		// Solicita al usuario el nombre de usuario y la contraseña a través de ventanas
		// emergentes

		usuario = JOptionPane.showInputDialog(null, "Nombre de usuario", "Entrada de texto",

				JOptionPane.QUESTION_MESSAGE);

		Contraseña = JOptionPane.showInputDialog(null, "Contraseña", "Entrada de texto", JOptionPane.QUESTION_MESSAGE);

		// Crea una consulta SQL para verificar las credenciales del usuario en la base
		// de datos

		String cadena = "SELECT * FROM jugadores WHERE usuarios = '" + usuario + "' AND contraseña = '" + Contraseña

				+ "'";

		// Envía la consulta SQL al servidor

		writer.println(cadena);

		// Lee la respuesta del servidor para verificar si la sesión fue iniciada

		// correctamente

		String verificar = reader.readLine();

		if (verificar.contains("sesion Iniciada")) {

			while (true) {

			}

		} else {

			JOptionPane.showMessageDialog(null, "Contraseña Incorrecta Intentelo de nuevo", "Advertencia",
					JOptionPane.WARNING_MESSAGE); // Si la sesión no se inició, realiza alguna otra acción (aquí no está
													// definida)

		}

	}

	// Método para gestionar el registro de un nuevo usuario

	public static void Registrarse() {

		// Solicita al usuario el nombre de usuario, la contraseña y la confirmación de
		// la contraseña

		usuario = JOptionPane.showInputDialog(null, "Nombre de usuario", "Entrada de texto",

				JOptionPane.QUESTION_MESSAGE);

		Contraseña = JOptionPane.showInputDialog(null, "Introduce Contraseña", "Entrada de texto",

				JOptionPane.QUESTION_MESSAGE);

		Contraseñadenuevo = JOptionPane.showInputDialog(null, "Introduce de vuelta la contraseña", "Entrada de texto",

				JOptionPane.QUESTION_MESSAGE);

		// Verifica si la contraseña y su confirmación coinciden

		if (Contraseña.equals(Contraseñadenuevo)) {

			// Si coinciden, muestra un mensaje de éxito

			JOptionPane.showMessageDialog(null, "Contraseña Correcta", "Advertencia", JOptionPane.INFORMATION_MESSAGE);

			// Crea una consulta SQL para registrar al nuevo usuario en la base de datos

			String cadena = "INSERT INTO jugadores (usuario, contraseña) VALUES ('" + usuario + "', '" + Contraseña

					+ "')";

			// Envía la consulta SQL al servidor

			writer.println(cadena);

		} else {

			// Si las contraseñas no coinciden, muestra un mensaje de advertencia

			JOptionPane.showMessageDialog(null, "Contraseña Incorrecta", "Advertencia", JOptionPane.WARNING_MESSAGE);

		}

	}

}