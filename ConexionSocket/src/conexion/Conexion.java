package conexion;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import RPS.metodos;

public class Conexion {

	private static final int PORT = 5555;
	private static final int NUM_CLIENTES = 2;

	public void iniciarServidor() {
		List<Socket> clientes = new ArrayList<>();

		try (ServerSocket server = new ServerSocket(PORT)) {
			System.out.println("Servidor iniciado en el puerto " + PORT);

			// Aceptar conexiones hasta que se conecten dos clientes
			while (clientes.size() < NUM_CLIENTES) {
				System.out.println("Esperando conexión de cliente " + (clientes.size() + 1) + "...");
				Socket clientSocket = server.accept();
				clientes.add(clientSocket);
				System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());
			}

			System.out.println("Se han conectado " + NUM_CLIENTES + " clientes. Iniciando comunicación...");

			// Obtener los sockets de los dos clientes
			Socket cliente1 = clientes.get(0);
			Socket cliente2 = clientes.get(1);

			try (BufferedReader reader1 = new BufferedReader(new InputStreamReader(cliente1.getInputStream()));
					PrintWriter writer1 = new PrintWriter(cliente1.getOutputStream(), true);
					BufferedReader reader2 = new BufferedReader(new InputStreamReader(cliente2.getInputStream()));
					PrintWriter writer2 = new PrintWriter(cliente2.getOutputStream(), true)) {
				int contador = 0;
				while (true) {
                    // Cliente 1 envía mensaje, Cliente 2 recibe
                    writer1.println("Es tu turno, escribe un mensaje para el Cliente 2:");
                    String mensaje1 = reader1.readLine();
                    System.out.println("Cliente 1 dice: " + mensaje1);
                    

                    // Cliente 2 envía mensaje, Cliente 1 recibe
                    writer2.println("Es tu turno, escribe un mensaje para el Cliente 1:");
                    String mensaje2 = reader2.readLine();
                    System.out.println("Cliente 2 dice: " + mensaje2);

//					// Obtener las opciones seleccionadas por los jugadores
//					writer1.println("Es tu turno, escribe un mensaje para el Cliente 2:");
//					String[] opciones = { "Piedra", "Papel", "Tijera" };
//					int panel = JOptionPane.showOptionDialog(null, "Elige una opcion", "jugador 1", 0, 1, null, opciones,
//							opciones);
//					int opcPLayed1 = panel;
//
//					writer2.println("Es tu turno, escribe un mensaje para el Cliente 1:");
//					String[] opciones2 = { "Piedra", "Papel", "Tijera" };
//					int panel2 = JOptionPane.showOptionDialog(null, "Elige una opcion", "jugador 2", 0, 1, null,
//							opciones2, opciones2);
//					int opcPlayed2 = panel2;
//
//					if (opcPLayed1 != 100 && opcPlayed2 != 100) {
//						metodos.game(opcPLayed1, opcPlayed2);
//						contador++;
//					}
//
//				}
//				if(contador == 3) {
//					metodos.ganador();
//				}
				}
			} catch (IOException e) {
				System.out.println("Error en la comunicación entre clientes: " + e.getMessage());
			}

		} catch (IOException e) {
			System.out.println("Ha ocurrido un error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
