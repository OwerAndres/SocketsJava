package basedatos;

import java.sql.SQLException;
import java.util.Scanner;

public class prueba {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		Scanner lector = new Scanner(System.in);
		
		JDBCconnection nueva = new JDBCconnection();
		/*****
		nueva.MostrarUsuario();
		****/
		/*****
		nueva.CrearUsuario("marimar", "marimar");
		****/
		/****nueva.CrearPartida(1, "maria", "marimar");
		/**
		nueva.MostrarPartida(1);
		****56/
		/*****nueva.ActualizarPerfil(5, 2);***/
		nueva.actualizarPuntos(2, "maquina", 1);
	}

}
