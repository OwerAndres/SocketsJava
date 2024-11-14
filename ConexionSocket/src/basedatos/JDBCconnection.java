package basedatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCconnection {

	public static final String url = "jdbc:mysql://localhost:3306/juego";
	public static final String user = "root";
	public static final String password = "root";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	public static void actualizarPuntos(int indicador, String jugador, int idpartida) throws SQLException {

		/**
		 * con jugador me refiero a que mando 1 o 2 para que nos diga si es el jugador 1
		 * o 2
		 **/
		/**
		 * i es la variable que vamos a utilizar en java para contar y sumar sobre los
		 * puntos
		 ***/
		int i = 0;

		/***
		 * para poder modificar los puntos del jugador 1 o 2 guardamos en un string el
		 * jugador que es
		 ***/
		String puntuaje = "puntos" + jugador;

		/***
		 * primero indicador nos dira si esta jugando en modo multijugador y solitario
		 ***/
		switch (indicador) {
		case 1:
			/** si juega en multijugador ***/
			/***
			 * obtenemos cuantos puntos y id de jugador tiene cada uno al inicio de cada
			 * ronda y la conexion
			 ***/
			String points = "select puntosj1,puntosj2,jugador1,jugador2 from partidasm where idpartida like ?";
			try (Connection conn = DriverManager.getConnection(url, user, password);) {
				PreparedStatement consultap = conn.prepareStatement(points);
				consultap.setInt(1, idpartida);
				ResultSet rs = consultap.executeQuery();

				/*** guardamos los puntos de cada uno en varibales **/
				int p1 = rs.getInt("puntosj1");
				int p2 = rs.getInt("puntosj2");

				/**
				 * si los puntos del jugador que se debe actualizar son el del 1 el valor de i
				 * ahora son los puntos que tenia el 1
				 **/
				if (jugador.equalsIgnoreCase("j1")) {
					i = p1;
				} else if (jugador.equalsIgnoreCase("j2")) {
					/**
					 * si los puntos del jugador que se debe actualizar son el del 2 el valor de i
					 * ahora son los puntos que tenia el 2
					 **/
					i = p2;
				}

				/** guardamos las id de los jugadores para llamada de otro metodo ***/
				int j1 = rs.getInt("jugador1");
				int j2 = rs.getInt("jugador2");

				rs.close();

				/***
				 * si el jugador uno tiene 3 puntos le pone como ganador, si el jugador 2 tiene
				 * 3 puntos le pone a el de ganador sino sumara un punto al jugador que sea que
				 * ha marcado (aka el que se actualiza)
				 **/
				if (p1 >= 3) {
					String quaery = "update partidasm set ganador = jugador1" + "where ID_PARTIDA=?";
					PreparedStatement pstmt = conn.prepareStatement(quaery);
					pstmt.setInt(1, idpartida);
					pstmt.executeUpdate();
					ActualizarPerfil(j1);
					pstmt.close();

				} else if (p2 >= 3) {
					String quaery = "update partidasm set ganador = jugador2" + "where ID_PARTIDA=?";
					PreparedStatement pstmt = conn.prepareStatement(quaery);
					pstmt.setInt(1, idpartida);
					pstmt.executeUpdate();
					ActualizarPerfil(j2);
					pstmt.close();
				} else {
					String quaery = "update partidasm set ? =" + (i + 1) + "where ID_PARTIDA=?";
					PreparedStatement pstmt = conn.prepareStatement(quaery);
					pstmt.setString(1, puntuaje);
					pstmt.setInt(2, idpartida);
					pstmt.executeUpdate();
					pstmt.close();
				}
			}

			break;

		case 2:
			/***
			 * cuando el jugador juega contra la maquina, obtenemos los puntos de la maquina
			 * y el jugador y la id del jugador
			 ***/
			String pointss = "select puntosj1,puntosmaquina,jugador1 from partidass where idpartida like ?";
			try (Connection conn = DriverManager.getConnection(url, user, password);) {
				PreparedStatement consultap = conn.prepareStatement(pointss);
				consultap.setInt(1, idpartida);
				ResultSet rs = consultap.executeQuery();

				/***
				 * guardamos los puntos que tenia ya el jugador antes de actualizar y lo mismo
				 * con los puntos de la maquina
				 ***/
				int p1 = rs.getInt("puntosj1");
				int pmaquina = rs.getInt("puntosmaquina");

				if (jugador.equalsIgnoreCase("j1")) {
					i = p1;
				} else if (jugador.equalsIgnoreCase("maquina")) {
					/**
					 * si los puntos del jugador que se debe actualizar son el del 2 el valor de i
					 * ahora son los puntos que tenia el 2
					 **/
					i = pmaquina;
				}

				/** conseguimos las id para llamada de otro metodo ***/
				int j1 = rs.getInt("jugador1");
				rs.close();
				/***
				 * si el jugador uno tiene 3 le pone como ganador, si la maquina tiene 3 le pone
				 * a el de ganador sino sumara un punto al jugador que sea que ha marcado
				 **/
				if (p1 <= 3) {
					String quaery = "update partidass set ganador = jugador1" + "where ID_PARTIDA=?";
					PreparedStatement pstmt = conn.prepareStatement(quaery);
					pstmt.setInt(1, idpartida);
					pstmt.executeUpdate();
					ActualizarPerfil(j1);
					pstmt.close();

				} else if (pmaquina <= 3) {
					String quaery = "update partidass set ganador = maquina" + "where ID_PARTIDA=?";
					PreparedStatement pstmt = conn.prepareStatement(quaery);
					pstmt.setInt(1, idpartida);
					pstmt.executeUpdate();
				} else {
					String quaerys = "update partidass set ? =" + (i + 1) + "where ID_PARTIDA=?";
					PreparedStatement pstmt = conn.prepareStatement(quaerys);
					pstmt.setString(1, puntuaje);
					pstmt.setInt(2, idpartida);
					pstmt.executeUpdate();
					pstmt.close();
				}
			}
			break;
		}
	}

	public static void ActualizarPerfil(int id_jugador) throws SQLException {

		/**
		 * primero obtenemos la conexion y el valor de las partidas ganadas que ya tenia
		 * el usuario
		 ***/
		String quaeryconsulta = "select PG from usuarios where id like ?";
		try (Connection conn = DriverManager.getConnection(url, user, password);) {
			PreparedStatement pstmt = conn.prepareStatement(quaeryconsulta);
			pstmt.setInt(1, id_jugador);
			pstmt.executeUpdate();
			ResultSet rs = pstmt.executeQuery();
			int partidasGanadas = rs.getInt("PG");
			pstmt.close();

			/***
			 * una vez que tenemos el valor antes de modificacion hacemos la consulta de
			 * modificacion, y le sumamos una mas a la spartidas ganadas
			 ****/
			String quaeryactualizar = "update usuarios set PG =" + (partidasGanadas + 1) + "where id like ?";
			PreparedStatement pstmt2 = conn.prepareStatement(quaeryactualizar);
			pstmt2.setInt(1, id_jugador);
			pstmt2.executeUpdate();
			pstmt2.close();
		}
	}

	public static void MostrarPerfil() {

	}

	public static void MostrarPartida() {

	}
}
