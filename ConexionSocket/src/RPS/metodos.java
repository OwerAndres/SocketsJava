package RPS;

import javax.swing.JOptionPane;

public class metodos {

	public static int logica(int take1, int take2) {
		// 0 = piedra --- 1 = papel --- 2 = tijera
		if (take1 == take2) {
			return 0; // empate
		} else {
			switch (take1) {
			case 0:
				if (take2 == 1) {
					return 2; // gana J2
				} else if (take2 == 2) {
					return 1; // gana J1
				}
				break;
			case 1:
				if (take2 == 0) {
					return 1; // gana J1
				} else if (take2 == 2) {
					return 2; // gana J2
				}
				break;
			case 2:
				if (take2 == 0) {
					return 2; // gana J2
				} else if (take2 == 1) {
					return 1; // gana J1
				}
				break;
			default:
				break;
			}
		}
		return 0;
	}

//	public static int choose() {
//		String plays[] = { "Piedra", "Papel", "Tijera" };
//
//		int chosen = JOptionPane.showOptionDialog(null, "Elige una opcion", null, 0, 0, null, plays, plays);
//
//		return chosen;
//	}

	public static int maquina() {

		return (int) Math.floor(Math.random() * 3);
	}

	/*
	 * public static void game(int played1, int played2,int points1,int points2) {
	 * 
	 * 
	 * String plays [] = {"Piedra","Papel","Tijera"};
	 * 
	 * System.out.println("J1 "+points1+" - "+points2+" J2");
	 * 
	 * if(points1>=3) { System.out.println("Jugador 1 GANA."); } else
	 * if(points2>=3){ System.out.println("Jugador 2 GANA."); } else { //played1 = ?
	 * //played2 = ? int jugada=logica(played1,played2);
	 * 
	 * System.out.println("Jugador 1 jugó "+plays[played1]+". Jugador 2 jugó "+plays
	 * [played2]+". "); switch (jugada) { case 0: //empate
	 * System.out.println("Empate"); break; case 1: //gana J1
	 * System.out.println("Jugador 1 gana."); points1++; break; case 2: //gana J2
	 * System.out.println("Jugador 2 gana."); points2++; break; default: break; } }
	 * 
	 * }
	 */
	
	
	public static void ganador() {
	    if (points1 > points2) {
	        System.out.println("Jugador 1 GANA el juego.");
	    } else if (points2 > points1) {
	        System.out.println("Jugador 2 GANA el juego.");
	    } else {
	        System.out.println("El juego termina en empate.");
	    }
	}

	private static int points1 = 0; 
	private static int points2 = 0;

	public static void game(int ply1, int ply2) {
	    int played1, played2;
	    String plays[] = { "Piedra", "Papel", "Tijera" };

	    played1 = ply1;
	    played2 = ply2;

	    int jugada = logica(played1, played2);

	    System.out.println("Jugador 1 jugó " + plays[played1] + ". Jugador 2 jugó " + plays[played2] + ". ");
	    switch (jugada) {
	        case 0: // empate
	            System.out.println("Empate");
	            break;
	        case 1: // gana J1
	            System.out.println("Jugador 1 gana.");
	            points1++;
	            break;
	        case 2: // gana J2
	            System.out.println("Jugador 2 gana.");
	            points2++;
	            break;
	        default:
	            break;
	    }

	    System.out.println("J1 " + points1 + " - " + points2 + " J2\n");
	}


//	public static void main(String[] args) {
//		game();
//	}

}