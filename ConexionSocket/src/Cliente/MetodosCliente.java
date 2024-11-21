package Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

public class MetodosCliente {
	public static void InicioSesion(BufferedReader reader, PrintWriter writer) {
        try {
            writer.println("Inicio Sesion");
            String mensajeServidor = reader.readLine();

            if (mensajeServidor.contains("Introduce el nombre de usuario")) {
                String usuario = JOptionPane.showInputDialog(null, "Nombre de usuario", "Entrada de texto",
                        JOptionPane.QUESTION_MESSAGE);
                writer.println(usuario);
                mensajeServidor = reader.readLine();

                if (mensajeServidor.contains("Introduce la contraseña")) {
                    String contrasena = JOptionPane.showInputDialog(null, "Contraseña", "Entrada de texto",
                            JOptionPane.QUESTION_MESSAGE);
                    writer.println(contrasena);
                }

                mensajeServidor = reader.readLine();

                if (mensajeServidor.contains("Contraseña Correcta")) {
                    JOptionPane.showMessageDialog(null, "Contraseña correcta");
                    Seleccion(reader, writer);
                } else if (mensajeServidor.contains("Contraseña Incorrecta")) {
                    JOptionPane.showMessageDialog(null, "Contraseña incorrecta intentelo de nuevo", "",
                            JOptionPane.WARNING_MESSAGE, null);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Registrarse(BufferedReader reader, PrintWriter writer, BufferedReader consola) {
        try {
            writer.println("Registrarse");
            String mensajeServidor = reader.readLine();

            String usuario = JOptionPane.showInputDialog(null, "Nombre de usuario", "Entrada de texto",
                    JOptionPane.QUESTION_MESSAGE);
            String contrasena = JOptionPane.showInputDialog(null, "Introduce Contraseña", "Entrada de texto",
                    JOptionPane.QUESTION_MESSAGE);
            String contrasenaDeNuevo = JOptionPane.showInputDialog(null, "Introduce de vuelta la contraseña",
                    "Entrada de texto", JOptionPane.QUESTION_MESSAGE);

            if (contrasena.equals(contrasenaDeNuevo)) {
                if (mensajeServidor.contains("Introduce el nombre de usuario")) {
                    writer.println(usuario);
                    mensajeServidor = reader.readLine();

                    if (mensajeServidor.contains("Introduce la contraseña")) {
                        writer.println(contrasena);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Contraseña Incorrecta", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Seleccion(BufferedReader reader, PrintWriter writer) {
        String[] modos = {"Multijugador", "Contra la maquina", "Salir"};
        boolean exit = true;

        do {
            int seleccion = JOptionPane.showOptionDialog(null, "Selecciona una opción:", "Menú de opciones",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, modos, modos[0]);

            switch (seleccion) {
                case 0 -> Multijugador();
                case 1 -> Maquina();
                case 2 -> exit = false;
                default -> JOptionPane.showMessageDialog(null, "No seleccionaste ninguna opción.", "Información",
                        JOptionPane.WARNING_MESSAGE);
            }
        } while (exit);
    }

    public static void Multijugador() {
        // Implementar lógica de multijugador
    }

    public static void Maquina() {
        // Implementar lógica contra la máquina
    }
}