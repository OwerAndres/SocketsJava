package Cliente;

import java.io.*;

import java.net.*;

import javax.imageio.IIOException;

import javax.swing.JOptionPane;

public class Cliente {

	static Socket socket;

	static BufferedReader reader, consola;

	static PrintWriter writer;

	static String usuario, Contraseña, Contraseñadenuevo, mensajeServidor;

	public static void main(String[] args) {

		try {

			socket = new Socket("172.16.41.149", 5555);

			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			writer = new PrintWriter(socket.getOutputStream(), true);

			consola = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Conectando al servidor 5555");
			while (true) {
				String[] inicio = { "Inicio Sesión", "Registrarse" };
				int seleccion = JOptionPane.showOptionDialog(null, "Selecciona una opción:", "Menú de opciones",
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, inicio, inicio[0]);
				switch (seleccion) {
				case 0 -> {
					InicioSesion();
				}
				case 1 -> {
					Registrarse();
				}
				default -> {
					JOptionPane.showMessageDialog(null, "No seleccionaste ninguna opción.", "Información",
							JOptionPane.WARNING_MESSAGE);
				}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Inicio Sesion
	public static void InicioSesion() {
		try {
			writer.println("Inicio Sesion");
			mensajeServidor = reader.readLine();
			if (mensajeServidor.contains("Introduce el nombre de usuario")) {
				usuario = JOptionPane.showInputDialog(null, "Nombre de usuario", "Entrada de texto",
						JOptionPane.QUESTION_MESSAGE);
				writer.println(usuario);
				mensajeServidor = reader.readLine();

				if (mensajeServidor.contains("Introduce la contraseña")) {
					Contraseña = JOptionPane.showInputDialog(null, "Contraseña", "Entrada de texto",
							JOptionPane.QUESTION_MESSAGE);
					writer.println(Contraseña);

				}
				mensajeServidor = reader.readLine();

				if (mensajeServidor.contains("Contraseña Correcta")) {
					JOptionPane.showMessageDialog(null, "Contraseña correcta");
					Seleccion();

				} else if (mensajeServidor.contains("Contraseña Incorrecta")) {
					JOptionPane.showMessageDialog(null, "Contraseña incorrecta intentelo de nuevo", "",
							JOptionPane.WARNING_MESSAGE, null);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Registrarse
	public static void Registrarse() {

		try {
			writer.println("Registrarse");
			mensajeServidor = reader.readLine();

			usuario = JOptionPane.showInputDialog(null, "Nombre de usuario", "Entrada de texto",

					JOptionPane.QUESTION_MESSAGE);

			Contraseña = JOptionPane.showInputDialog(null, "Introduce Contraseña", "Entrada de texto",

					JOptionPane.QUESTION_MESSAGE);

			Contraseñadenuevo = JOptionPane.showInputDialog(null, "Introduce de vuelta la contraseña",
					"Entrada de texto", JOptionPane.QUESTION_MESSAGE);

			if (Contraseña.equals(Contraseñadenuevo)) {

				writer.println("Usuario: "+usuario);

				writer.println("Contraseña: "+Contraseña);

			} else {

				JOptionPane.showMessageDialog(null, "Contraseña Incorrecta", "Advertencia",
						JOptionPane.WARNING_MESSAGE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Seleccion
	public static void Seleccion() {
		String[] Modos = { "Multijugador", "Contra la maquina", "Salir" };
		boolean exit = true;
		do {
			int seleccion = JOptionPane.showOptionDialog(null, "Selecciona una opción:", "Menú de opciones",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, Modos, Modos[0]);
			switch (seleccion) {
			case 0 -> {
				Multijugador();
			}
			case 1 -> {
				Maquina();
			}
			case 2 -> {
				exit = false;
			}
			default -> {
				JOptionPane.showMessageDialog(null, "No seleccionaste ninguna opción.", "Información",
						JOptionPane.WARNING_MESSAGE);
			}

			}

		} while (exit);
	}

	public static void Maquina() {
		boolean exitlocal = true;
	}

	public static void Multijugador() {
	}

}
