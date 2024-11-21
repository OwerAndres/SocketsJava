package Inicio;

import java.util.Scanner;

import conexion.Conexion;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Bienvenido al sistema de juego. Selecciona una opción:");
		System.out.println("1. Multijugador");
		System.out.println("2. Contra la máquina");

		int opcion = scanner.nextInt();

		switch (opcion) {
		case 1:
			System.out.println("Iniciando modo multijugador...");
			Conexion conexion = new Conexion();
			conexion.iniciarServidor();
			break;
		case 2:
			System.out.println("Iniciando modo contra la máquina...");
			iniciarModoContraMaquina();
			break;
		default:
			System.out.println("Opción no válida. Por favor, selecciona 1 o 2.");
		}

		scanner.close();
	}

	private static void iniciarModoContraMaquina() {
		System.out.println("Juego contra la máquina no implementado aún. Aquí iría la lógica.");
		// Implementar lógica del juego contra la máquina aquí
	}
}
