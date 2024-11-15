package conexion;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Conexion {

    private static final int PORT = 5555;
    private static final int NUM_CLIENTES = 2;

    public void iniciarServidor() {
        List<Socket> clientes = new ArrayList<>();

        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado en el puerto " + PORT);

            /**Bucle infinito que se estara ejecuntado hasta que se unan 2 jugadores**/
            while (clientes.size() < NUM_CLIENTES) {
                System.out.println("Esperando conexión de cliente " + (clientes.size() + 1) + "...");
                Socket clientSocket = server.accept();
                clientes.add(clientSocket);
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());
            }

            System.out.println("Se han conectado " + NUM_CLIENTES + " clientes. Iniciando comunicación...");

            /**Obtener los sockets de los dos clientes**/
            Socket cliente1 = clientes.get(0);
            Socket cliente2 = clientes.get(1);

            try (
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(cliente1.getInputStream()));
                PrintWriter writer1 = new PrintWriter(cliente1.getOutputStream(), true);
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(cliente2.getInputStream()));
                PrintWriter writer2 = new PrintWriter(cliente2.getOutputStream(), true)
            ) {
                while (true) {
                	/**El cliente 1 enviara un mensaje y el cliente 2 esperara hasta que mande dicho mensaje **/
                    writer1.println("Es tu turno, escribe un mensaje para el Cliente 2:");
                    String mensaje1 = reader1.readLine();
                    System.out.println("Cliente 1 dice: " + mensaje1);
                  

                    /**El cleinte 2 enviara un mensaje y el cliente 1 esperaraa hasta que mande dicho mensaje **/
                    writer2.println("Es tu turno, escribe un mensaje para el Cliente 1:");
                    String mensaje2 = reader2.readLine();
                    System.out.println("Cliente 2 dice: " + mensaje2);
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
