package Inicio;

import java.util.Scanner;

import conexion.Conexion;

public class Main {

	public static void main(String[] args) {
	       Scanner scanner = new Scanner(System.in);
	        System.out.println("Selecciona el modo de juego:");
	        System.out.println("1. Multijugador");
	        System.out.println("2. Contra la máquina");

	        int opcion = scanner.nextInt();

	        switch (opcion) {
	            case 1:
	                System.out.println("Modo multijugador seleccionado. Iniciando servidor...");
	                Conexion conexion = new Conexion();
	                conexion.iniciarServidor();
	                break;
	            case 2:
	                System.out.println("Modo contra la máquina seleccionado");
	                /**Codigo metodo para juagr contra la maquina**/
	                break;
	            default:
	                System.out.println("Opción no válida. Por favor, selecciona 1 o 2.");
	                break;
	        }

	        scanner.close();
	    }
	}


