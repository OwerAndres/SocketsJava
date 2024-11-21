package basedatos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;

public class JDBCconnection {

	public static final String url = "jdbc:mysql://localhost:3306/juego";
	public static final String user = "root";
	public static final String password = "root";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	public static void actualizarPuntos(int indicador, String jugador, int idpartida) throws SQLException {

		/**
		 * con jugador me refiero a que mando j1 ,j2 o maquina para que nos diga si es
		 * el jugador 1 o 2
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
			 * ronda y hacemos la conexion
			 ***/
			String points = "select puntosj1,puntosj2,jugador1,jugador2 from partidasm where id_partida like ?;";
			try (Connection conn = DriverManager.getConnection(url, user, password);) {
				PreparedStatement consultap = conn.prepareStatement(points);

				/*** metemos el valor del id de la partida en la consulta ***/
				consultap.setInt(1, idpartida);

				/** ejecutamos la consulta y la guardamos **/
				ResultSet rs = consultap.executeQuery();

				/*** guardamos los puntos de cada jugador en varibales **/
				int p1 = rs.getInt("puntosj1");
				int p2 = rs.getInt("puntosj2");

				/**
				 * si los puntos del jugador que se debe actualizar son el del jugador1 aka el
				 * parametro que se ha pasado como jugador es j1 el valor de i ahora son los
				 * puntos que tenia el jugador1
				 **/
				if (jugador.equalsIgnoreCase("j1")) {
					i = p1;
				} else if (jugador.equalsIgnoreCase("j2")) {
					/**
					 * si los puntos del jugador que se debe actualizar son el del 2 aka el
					 * parametro jugador es igual a j2 el valor de i ahora son los puntos que tenia
					 * el jugador2
					 **/
					i = p2;
				}

				/** guardamos las id de los jugadores para llamada de otro metodo ***/
				int j1 = rs.getInt("jugador1");
				int j2 = rs.getInt("jugador2");

				/** cerramos la consulta que usamos para obtener todos los datos **/
				consultap.close();
				rs.close();

				/***
				 * si el jugador uno tiene 3 o más puntos le pone como ganador, si el jugador 2
				 * tiene 3 puntos o más le pone a el de ganador sino sumara un punto al jugador
				 * que sea que ha marcado (aka el que se actualiza que habiamos indicado con la
				 * variable puntuaje)
				 **/
				if (p1 >= 3) {

					/***
					 * actualizamos la tabla de partidas multijugador y mediante un inner join
					 * obtenemos el usuario del jugador ganador y lo ponemos en ganador
					 **/
					String quaery = "UPDATE partidasm SET GANADOR = (SELECT usuarios FROM JUGADORES WHERE JUGADORES.id = PARTIDASM.JUGADOR1 LIMIT 1) where ID_PARTIDA=?;";
					PreparedStatement pstmt = conn.prepareStatement(quaery);
					pstmt.setInt(1, idpartida);

					/*** ejectuamos la consulta ***/
					pstmt.executeUpdate();

					/**
					 * llamamos a actualizarPerfil pasandole el id del jugador1 para que actualice
					 * el perfil del jugador1 el indicador que os dira si hay que actualizar en la
					 * tabla de partidas single-player o multi-player
					 ***/
					ActualizarPerfil(j1, indicador);

					/*** cerramos recursos ***/
					pstmt.close();

				} else if (p2 >= 3) {
					/***
					 * actualizamos la tabla de partidas multijugador y mediante un inner join
					 * obtenemos el usuario del jugador ganador y lo ponemos en ganador
					 **/
					String quaery = "UPDATE partidasm SET GANADOR = (SELECT usuarios FROM JUGADORES WHERE JUGADORES.id = PARTIDASM.JUGADOR2 LIMIT 1) where ID_PARTIDA=?;";
					PreparedStatement pstmt = conn.prepareStatement(quaery);
					pstmt.setInt(1, idpartida);
					/*** ejectuamos la consulta ***/
					pstmt.executeUpdate();
					/**
					 * llamamos a actualizarPerfil pasandole el id del jugador2 para que actualice
					 * el perfil del jugador2 y el indicador que os dira si hay que actualizar en la
					 * tabla de partidas single-player o multi-player
					 ***/
					ActualizarPerfil(j2, indicador);
					/*** cerramos recursos ***/
					pstmt.close();
				} else {

					/***
					 * hacdemos la consulta donde le sumamos a i(el contador de puntos del que ha
					 * marcado) uno más
					 ***/
					String quaery = "update partidasm set ? =" + (i + 1) + "where ID_PARTIDA=?;";
					PreparedStatement pstmt = conn.prepareStatement(quaery);
					/** le ponemos el valor de las inconnitas ***/
					pstmt.setString(1, puntuaje);
					pstmt.setInt(2, idpartida);

					/** ejecutamos la consulta ***/
					pstmt.executeUpdate();

					/*** cerramos recursos ****/
					pstmt.close();
				}
			}catch(Exception e) {
				/***en caso de que algo vaya mal nos imprime el error****/
				e.printStackTrace();
			}

			break;

		case 2:

			/***
			 * cuando el jugador juega contra la maquina(single-player), primer obtenemos
			 * los puntos de la maquina y el jugador y la id del jugador1
			 ***/
			String pointss = "select puntosj1,puntosmaquina,jugador1 from partidass where idpartida like ?;";

			/*** hacemos la conexion con la base de datos ****/
			try (Connection conn = DriverManager.getConnection(url, user, password);) {
				PreparedStatement consultap = conn.prepareStatement(pointss);

				/*** rellenamos los valores de las inconitas ***/
				consultap.setInt(1, idpartida);

				/*** ejecutamos las consultas y guardamos el resultado ***/
				ResultSet rs = consultap.executeQuery();

				/***
				 * guardamos los puntos que tenia ya el jugador antes de actualizar y lo mismo
				 * con los puntos de la maquina
				 ***/
				int p1 = rs.getInt("PUNTOSJ1");
				int pmaquina = rs.getInt("PUNTOSMAQUINA");

				/***
				 * Si el parametro por pantalla jugador es igual a j1 i tomara el valor de los
				 * puntos del jugador1, pero si jugador= maquina i tomara el valor del contador
				 * de los puntos de la maquina
				 ***/
				if (jugador.equalsIgnoreCase("j1")) {
					i = p1;
				} else if (jugador.equalsIgnoreCase("maquina")) {
					i = pmaquina;
				}

				/** conseguimos la id para llamada de otro metodo ***/
				int j1 = rs.getInt("jugador1");
				/***
				 * cerramos recursos de esa primera consulta donde hemos obtenidos los datos de
				 * los contadores y id
				 ***/
				rs.close();

				/***
				 * si el jugador uno tiene 3 o más le pone como ganador, si la maquina tiene 3 o
				 * más le pone a el de ganador sino sumara un punto al jugador que sea que ha
				 * marcado
				 **/

				if (p1 >= 3) {

					/**
					 * actualizamos la tabla de partidas single-player y ponemos al jugador1 como
					 * ganador
					 ***/
					String quaery = "UPDATE partidass SET GANADOR = (SELECT usuarios FROM JUGADORES WHERE JUGADORES.id = PARTIDASS.JUGADOR1 LIMIT 1) where ID_PARTIDA=?;";
					PreparedStatement pstmt = conn.prepareStatement(quaery);

					/** rellenamos las inconitas ***/
					pstmt.setInt(1, idpartida);

					/*** ejecutamos la consulta ***/
					pstmt.executeUpdate();

					/***
					 * llamamos al metodo de actualizar perfil pasandole la id del jugador 1 y el
					 * indicador de si debe ser en la tabla de single player o multiplayer
					 ***/
					ActualizarPerfil(j1, indicador);

					/*** cerramos recursos ***/
					pstmt.close();

				} else if (pmaquina >= 3) {
					/** hacemos la consulta donde ponemos a maquina como ganador **/
					String quaery = "update partidass set ganador = 'maquina' where ID_PARTIDA=?;";
					PreparedStatement pstmt = conn.prepareStatement(quaery);

					/*** rellenamos las inconitas ***/
					pstmt.setInt(1, idpartida);
					/*** ejecutamos la consulta **/
					pstmt.executeUpdate();
					/*** cerramos recursos ***/
					pstmt.close();

				} else {

					/**
					 * atualizamos los puntos de la tabla singleplayer para añadir al valor de i un
					 * punto ya que i tiene el valor del contador del jugador que marca
					 ***/
					String quaerys = "update partidass set ? =" + (i + 1) + "where ID_PARTIDA=?;";
					PreparedStatement pstmt = conn.prepareStatement(quaerys);

					/***
					 * rellenamos las inconitas con puntuaje que indica que jugador ha marcado y
					 * idpartida que su nombre indica que es
					 ***/
					pstmt.setString(1, puntuaje);
					pstmt.setInt(2, idpartida);

					/*** ejecutamos las consultas ***/
					pstmt.executeUpdate();

					/*** cerramos recursos ***/
					pstmt.close();
				}
			}catch(Exception e) {
				/***en caso de que algo vaya mal nos imprime el error****/
				e.printStackTrace();
			}
			break;
		}
	}

	public static void ActualizarPerfil(int id_jugador, int indicador) throws SQLException {

		/**
		 * primero obtenemos la conexion y el valor de las partidas ganadas que ya tenia
		 * el usuario a actualizar
		 ***/
		String quaeryconsulta = "select PG_multijugador,PG_maquina from JUGADORES where id = ?";
		try (Connection conn = DriverManager.getConnection(url, user, password);) {
			PreparedStatement pstmt = conn.prepareStatement(quaeryconsulta);

			/*** rellenamos las inconitas ***/
			pstmt.setInt(1, id_jugador);

			/*** ejecutamos la consulta y guardamos los resultados ***/
			ResultSet rs = pstmt.executeQuery();

			/***
			 * en dos variables guardamos los contadores de todas las veces que ha ganado en
			 * partidas multijugador y en partidas single-player aka contra la maquina
			 ***/
			int partidasGanadasmulti = rs.getInt("PG_multijugador");
			int partidasGanadasmaquina = rs.getInt("PG_multijugador");

			/*** cerramos recursos ***/
			pstmt.close();

			/**
			 * segun el indicador modificaremos el contador de ganadas de multiplayer(1) o
			 * singleplayer(2)
			 **/
			switch (indicador) {
			case 1:
				/***
				 * actualizamos el campo de partidas ganadas de multijugador sumandole 1 a la
				 * variable donde habiamos almacenado el contador de ganadas multijugador antes
				 * de modificacion segun su id
				 ****/
				String quaeryactualizarpartidasmulti = "update JUGADORES set PG_multijugador ="
						+ (partidasGanadasmulti + 1) + "where id = ?";
				PreparedStatement pstmt2 = conn.prepareStatement(quaeryactualizarpartidasmulti);

				/*** rellenamos inconitas ***/
				pstmt2.setInt(1, id_jugador);

				/**** ejecutamos la consulta ****/
				pstmt2.executeUpdate();

				/*** cerramos recursos **/
				pstmt2.close();
				break;

			case 2:
				/***
				 * si indicador dice que 2 hay que actualizar los partidas ganadas contra la
				 * maquina donde simplemente le sumaos al contador de partidas ganadas contra la
				 * maquina +1
				 ****/
				String quaeryactualizarpartidasmaquina = "update JUGADORES set PG_maquina ="
						+ (partidasGanadasmaquina + 1) + "where id = ?";
				PreparedStatement pstmt3 = conn.prepareStatement(quaeryactualizarpartidasmaquina);

				/*** rellenamos inconitas ****/
				pstmt3.setInt(1, id_jugador);

				/*** ejeutamos la consulta ***/
				pstmt3.executeUpdate();

				/*** cerramos recursos ***/
				pstmt3.close();
				break;
			}
			/** cerramos switch **/
		}catch(Exception e) {
			/***en caso de que algo vaya mal nos imprime el error****/
			e.printStackTrace();
		}
	}

	public static void MostrarUsuario() throws SQLException {
		/***
		 * preparamos la consulta select**
		 **/
		String quaeryconsulta = "select * from JUGADORES;";
		/** hacdemos la conexion con la base de datos ***/
		try (Connection conn = DriverManager.getConnection(url, user, password);) {
			PreparedStatement pstmt = conn.prepareStatement(quaeryconsulta);

			/*** ejecutamos la consulta y guardamos sus resultados en un resulset ***/
			ResultSet rs = pstmt.executeQuery();

			/*** les damos un formato ***/
			System.out.println(String.format("%-10s %-50s %-50s %-50s %-50s", "id", "usuarios", "contraseña",
					"PG_multijugador", "PG_maquina"));
			System.out.println(
					"---------------------------------------------------------------------------------------------------------------------------------------------------------------------");

			/*** mientras haya filas que imprimir se imprimen ***/
			while (rs.next()) {
				System.out.println(
						String.format("%-10s %-50s %-50s %-50s %-50s", rs.getInt("id"), rs.getString("usuarios"),
								rs.getString("contraseña"), rs.getInt("PG_multijugador"), rs.getInt("PG_maquina")));
			}

		}catch(Exception e) {
			/***en caso de que algo vaya mal nos imprime el error****/
			e.printStackTrace();
		}
	}

	public static void MostrarPartida(int indicador) throws SQLException {
		/**
		 * segun el indicador de si esta partida a buscar ha sido multijugador(1) o
		 * single-player(2) hacemos una cosa o otra con el switch
		 ***/
		switch (indicador) {
		case 1:

			/**
			 * si es multijugador primero preparamos la consulta donde te enseña todos los
			 * datos
			 ***/
			String quaeryconsulta1 = "select * from partidasm;";

			/*** hacemos la conexion **/
			try (Connection conn = DriverManager.getConnection(url, user, password);) {
				PreparedStatement pstmt = conn.prepareStatement(quaeryconsulta1);

				/*** ejecutamos la consulta y guardamos los resultados **/
				ResultSet rs = pstmt.executeQuery();

				/**** dejamos un formato ****/
				System.out.println(String.format("%-20s %-20s  %-20s  %-50s  %-50s %-50s %-50s", "ID_PARTIDA",
						"JUGADOR1", "JUGADOR2", "PUNTOSJ1", "PUNTOSJ2", "GANADOR", "FECHA"));
				System.out.println(
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------");

				/*** imprimimos la info mientras haya filas a imprimir ***/
				while (rs.next()) {
					System.out.println(
							String.format("%-20s %-20s  %-20s  %-50s  %-50s %-50s %-50s", rs.getInt("ID_PARTIDA"),
									rs.getInt("JUGADOR1"), rs.getInt("JUGADOR2"), rs.getInt("PUNTOSJ1"),
									rs.getInt("PUNTOSJ2"), rs.getString("GANADOR"), rs.getDate("FECHA")));
				}

			}catch(Exception e) {
				/***en caso de que algo vaya mal nos imprime el error****/
				e.printStackTrace();
			}
			break;
		case 2:
			/**
			 * si es contra la maquina(single player) preparamos la consulta donde te
			 * muestra todos los datos
			 ***/
			String quaeryconsulta2 = "select * from partidass;";

			/*** hacemos la conexion **/
			try (Connection conn = DriverManager.getConnection(url, user, password);) {
				PreparedStatement pstmt = conn.prepareStatement(quaeryconsulta2);

				/*** ejecutamos la consulta y guardamos sus resultados en un resultset ***/
				ResultSet rs = pstmt.executeQuery();

				/**** le damos un formato ****/
				System.out.println(String.format("%-20s %-20s  %-20s  %-50s  %-50s %-50s %-50s", "ID_PARTIDA",
						"JUGADOR1", "PUNTOSJ1", "PUNTOSMAQUINA", "GANADOR", "FECHA"));
				System.out.println(
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				/*** imprimimos la info siempre y cuando haya filas que imprimir ***/
				while (rs.next()) {
					System.out.println(String.format("%-20s %-20s  %-20s  %-50s  %-50s %-50s %-50s",
							rs.getInt("ID_PARTIDA"), rs.getInt("JUGADOR1"), rs.getInt("PUNTOSJ1"),
							rs.getInt("PUNTOSMAQUINA"), rs.getString("GANADOR"), rs.getDate("FECHA")));
				}

			}catch(Exception e) {
				/***en caso de que algo vaya mal nos imprime el error****/
				e.printStackTrace();
			}
			break;
		}
	}

	public static void CrearUsuario(String usuario, String contraseña) throws SQLException {

		/**
		 * preparamos la consulta donde como el primary key es auto increment solo
		 * necesitamos meterle los datos de usuario y contraseña y le inicializamos lo
		 * demas a 0 para evitar posibles errores de compilación
		 ***/
		String quaery = "insert into JUGADORES (USUARIOS,CONTRASEÑA,PG_multijugador,PG_maquina) VALUES ('?','?',0,0);";
		/*** hacemos la conexion con la base de datos **/
		try (Connection conn = DriverManager.getConnection(url, user, password);) {
			PreparedStatement pstmt = conn.prepareStatement(quaery);
			pstmt.setString(1, usuario);
			pstmt.setString(2, contraseña);
			/*** ejecutamos la consulta ***/
			pstmt.executeUpdate();

		}catch(Exception e) {
			/***en caso de que algo vaya mal nos imprime el error****/
			e.printStackTrace();
		}
	}

	public static void CrearPartida(int indicador, String nombre1, String nombre2) throws SQLException {

		/***
		 * preparamos ua consulta en la que por el campo del usuario vamos a obtener las
		 * id de cada jugador
		 ***/
		String obtenerid = "select j1.id,j2.id from jugadores as j1 inner join jugadores as j2 on j2.usuarios like '?' where j1.USUARIOS like '?';";

		/*** hacemos la conexion ***/
		try (Connection conn = DriverManager.getConnection(url, user, password);) {

			/** hacemos el statement y rellenamos las inconitas ***/
			PreparedStatement pstmt = conn.prepareStatement(obtenerid);
			pstmt.setString(1, nombre2);
			pstmt.setString(2, nombre1);

			/*** ejecutamos la consulta y almacenamos su resultado en un resulset ***/
			ResultSet rs = pstmt.executeQuery();

			/**** guardamos los valores de la id de cada jugador ****/
			int id1 = rs.getInt("j1id");
			int id2 = rs.getInt("j2id");

			/*** cerramos los recursos ***/
			pstmt.close();
			rs.close();

			/****
			 * segun si debe crear una partida en multiplayer(1) o singleplayer(2) te hace
			 * casos diferentes que esto se indica cual mediante el indicador
			 *****/
			switch (indicador) {
			case 1:

				/*** obtenemos la fecha del sistema de cuando se llama el metodo ***/
				LocalDate fechaHoy = LocalDate.now();

				/***
				 * Convertimos el LocalDate en un LocalDateTime con la hora fijada a las
				 * 00:00:00 (inicio del día) en la zona horaria predeterminada del sistema.
				 * luego Convierto con el instant el LocalDateTime a un Instant, y luego ya de
				 * este a un date
				 ***/
				Date date = (Date) Date.from(fechaHoy.atStartOfDay(ZoneId.systemDefault()).toInstant());

				/** preparamos la queray de creación de la partida de multijugador ****/
				String quaery1 = "insert into partidasm (JUGADOR1,JUGADOR2,PUNTOSJ1,PUNTOSJ2,FECHA) VALUES (?,?,0,0,?);";

				PreparedStatement pstmt1 = conn.prepareStatement(quaery1);

				/*** rellenamos las inconitas ***/
				pstmt1.setInt(1, id1);
				pstmt1.setInt(2, id2);
				pstmt1.setDate(3, date);

				/** ejecutamos la consulta de creacion de partida multijugador **/
				pstmt1.executeUpdate();

				/*** cerramos recursos ****/
				pstmt1.close();

				break;

			case 2:
				/*** obtenemos la fecha del sistema de cuando se llama el metodo ***/
				LocalDate fechaHoy2 = LocalDate.now();

				/***
				 * Convertimos el LocalDate en un LocalDateTime con la hora fijada a las
				 * 00:00:00 (inicio del día) en la zona horaria predeterminada del sistema.
				 * luego Convierto con el instant el LocalDateTime a un Instant, y luego ya de
				 * este a un date
				 ***/
				Date date2 = (Date) Date.from(fechaHoy2.atStartOfDay(ZoneId.systemDefault()).toInstant());

				/** preparamos la queray de creación de la partida de singleplayer****/
				String quaery2 = "insert into partidass (JUGADOR1,PUNTOSJ1,PUNTOSMAQUINA,FECHA) VALUES (?,0,0,?);";

				PreparedStatement pstmt2 = conn.prepareStatement(quaery2);

				/*** rellenamos las inconitas ***/
				pstmt2.setInt(1, id1);
				pstmt2.setDate(2, date2);

				/** ejecutamos la consulta de creacion de partida multijugador **/
				pstmt2.executeUpdate();

				/*** cerramos recursos ****/
				pstmt2.close();
				break;
			}

		}catch(Exception e) {
			/***en caso de que algo vaya mal nos imprime el error****/
			e.printStackTrace();
		}
	}
}
