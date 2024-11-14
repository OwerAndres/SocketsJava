package basedatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCconnection {

	public static final String url="jdbc:mysql://localhost:3306/juego" ;
	public static final String user = "root";
	public static final String password = "root";
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url,user,password);
	}
	
	
	
	public static void actualizarPuntos(int indicador,int jugador, int idpartida) throws SQLException {
		int i=0;
		
		String puntuaje= "puntos"+jugador;
		
		switch(indicador) {
		case 1:
			String quaery="update partidasm set ? ="+(i+1)+"where ID_PARTIDA=?";
			try(Connection conn = DriverManager.getConnection(url, user, password);){
				PreparedStatement pstmt = conn.prepareStatement(quaery);
				pstmt.setString(1, puntuaje);
				pstmt.setInt(2, idpartida);
				pstmt.executeUpdate();	
			}
		break;
		
		case 2:
			String quaerys="update partidass set ? ="+(i+1)+"where ID_PARTIDA=?";
			try(Connection conn = DriverManager.getConnection(url, user, password);){
				PreparedStatement pstmt = conn.prepareStatement(quaerys);
				pstmt.setString(1, puntuaje);
				pstmt.setInt(2, idpartida);
				pstmt.executeUpdate();	
			}
			break;
		}
		
		
		
	}
	
	public static void ActualizarPerfil() {
		
	}
	
	public static void MostrarPerfil(){
		
	}
	
	public static void MostrarPartida() {
		
	}
}
