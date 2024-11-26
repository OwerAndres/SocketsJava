package conexion;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import BaseDatos.JDBCconnection;
import RPS.metodos;

public class Conexion {

	private static final int PORT = 5555;
	private static final int NUM_CLIENTES = 2;

	public void iniciarServidor() throws SQLException {
		/**Se crea un array de cleintes en el cual se almacenaran los clientes para poder manejar la cantidadz de jugadores 
		 * que se conectaran en el servidor, en este caso 2 jugadores, por lo cual hasta que el array no tenga dos elementos 
		 * el servidor seguira esperando jugadores**/
		List<Socket> clientes = new ArrayList<>();

		try (ServerSocket server = new ServerSocket(PORT)) {
			System.out.println("Servidor iniciado en el puerto " + PORT);

			/**En el bucle aceptamos la conexion de los clientes hasta que hallan dos clientes**/
			while (clientes.size() < NUM_CLIENTES) {
				System.out.println("Esperando conexión de cliente " + (clientes.size() + 1) + "...");
				Socket clientSocket = server.accept();
				clientes.add(clientSocket);
				System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());
			}

			System.out.println("Se han conectado " + NUM_CLIENTES + " clientes. Iniciando comunicación...");

			/**Obtenemos los sockets de los clientes**/
			Socket cliente1 = clientes.get(0);
			Socket cliente2 = clientes.get(1);

			/**Inicializamos las tuberias de conexion con el cliente, tando de escucha como de escritura**/
			try (BufferedReader reader1 = new BufferedReader(new InputStreamReader(cliente1.getInputStream()));
					PrintWriter writer1 = new PrintWriter(cliente1.getOutputStream(), true);
					BufferedReader reader2 = new BufferedReader(new InputStreamReader(cliente2.getInputStream()));
					PrintWriter writer2 = new PrintWriter(cliente2.getOutputStream(), true)) {
				
				int contador = 0;
				boolean authC2 = false;
				boolean authC1 = false;
				String usuario1 = "";
				String usuario2 = "";

				/**Bucle el cual siempre se estara ejecutnado hasta que los dos clientes no esten verificados, dicha verificacion es la que hace cuando inician
				 * sesion despues de haberse registrado en base de datos, el bucle seguira esperando o manteniendo a la espera de la respuesta en el servidor 
				 * de alguno de los clientes cuando todos esten verificados**/
				while (!authC1 || !authC2) {
					/**Leemos el mensaje del cliente 1**/
					if (!authC1) {
						/**Los wirtes con el mensaje es tu turno, es pàra inbdicar a cada cliente que ya puede hablar o escoger alguna accion**/
						writer1.println("Es tu turno, elige una acción (Registrarse o Inicio Sesion):");
						String accion1 = reader1.readLine();
						System.out.println("Cliente 1 dice: " + accion1);

						/**Comprpobamos cual opcion a escogido el cliente, en caso de haber elegido registrase se le pediran sus datos y se subira a la base de datos 
						 * en caso de escoger iniciar sesion se le pide el nombre y la password para consultar en la base de datos si dicho usuario existe 
						 * para que sea verificado**/
						if ("Registrarse".equals(accion1)) {
							writer1.println("Introduce los datos para registrarte.");
							
							/**Esperaremos una consulta completa de parte del cleinte cuando ya halla introducido los datos**/
							String registroSQL1 = reader1.readLine(); 
							System.out.println("Cliente 1 envía registro: " + registroSQL1);
							try {
								/**llamamos al metodo de proceso del registro el cual recorrera la respuesta del cleinte mandada al servidor, en la cual recogeremos sus 
								 * datos y los enviaremos a la base de datos mediante los metodos de la clase JDBCconnection**/
								procesarRegistro(registroSQL1);
								usuario1 = procesarRegistro2(registroSQL1);
								System.out.println("Cliente 1 registrado correctamente.");
								writer1.println("Registro completado exitosamente.");
							} catch (Exception e) {
								System.out.println("Error al registrar cliente 1: " + e.getMessage());
								writer1.println("Error en el registro.");
							}
						} else if ("Inicio Sesion".equals(accion1)) {
							writer1.println("Introduce tus credenciales para iniciar sesión.");
							/**Se espera la consulta completa de parte del cliente en el inicio de sesion**/
							String loginSQL1 = reader1.readLine(); 
							System.out.println("Cliente 1 envía inicio de sesión: " + loginSQL1);
							try {
								/**llamamos al metodo para procesar la respuesta del cleinte del inicio de sesion, en dodne recorremos la respuesta y obtenemos 
								 * los datos para posteriormente enviarlos a la base de datos para comprobar que existe para ser verificado**/
								authC1 = procesarInicioSesion(loginSQL1);
								if (authC1) {
									System.out.println("Cliente 1 autenticado correctamente.");
									writer1.println("Autenticación exitosa.");
								} else {
									System.out.println("Error en la autenticación de cliente 1.");
									writer1.println("Autenticación fallida.");
								}
							} catch (Exception e) {
								System.out.println("Error al autenticar cliente 1: " + e.getMessage());
								writer1.println("Error en el inicio de sesión.");
							}
						} else {
							writer1.println("Acción no reconocida.");
						}
					}

					/**leer mensaje del cliente 2**/
					if (!authC2) {
						/**Los wirtes con el mensaje es tu turno, es pàra inbdicar a cada cliente que ya puede hablar o escoger alguna accion**/
						writer2.println("Es tu turno, elige una acción (Registrarse o Inicio Sesion):");
						String accion2 = reader2.readLine();
						System.out.println("Cliente 2 dice: " + accion2);

						/**Comprpobamos cual opcion a escogido el cliente, en caso de haber elegido registrase se le pediran sus datos y se subira a la base de datos 
						 * en caso de escoger iniciar sesion se le pide el nombre y la password para consultar en la base de datos si dicho usuario existe 
						 * para que sea verificado**/
						if ("Registrarse".equals(accion2)) {
							writer2.println("Introduce los datos para registrarte.");
							
							/**Esperaremos una consulta completa de parte del cleinte cuando ya halla introducido los datos**/
							String registroSQL2 = reader2.readLine();
							System.out.println("Cliente 2 envía registro: " + registroSQL2);
							try {
								/**llamamos al metodo de proceso del registro el cual recorrera la respuesta del cleinte mandada al servidor, en la cual recogeremos sus 
								 * datos y los enviaremos a la base de datos mediante los metodos de la clase JDBCconnection**/
								procesarRegistro(registroSQL2);
								usuario2 = procesarRegistro2(registroSQL2);
								System.out.println("Cliente 2 registrado correctamente.");
								writer2.println("Registro completado exitosamente.");
							} catch (Exception e) {
								System.out.println("Error al registrar cliente 2: " + e.getMessage());
								writer2.println("Error en el registro.");
							}
						} else if ("Inicio Sesion".equals(accion2)) {
							writer2.println("Introduce tus credenciales para iniciar sesión.");
							/**Se espera la consulta completa de parte del cliente en el inicio de sesion**/
							String loginSQL2 = reader2.readLine(); 
							System.out.println("Cliente 2 envía inicio de sesión: " + loginSQL2);
							try {
								/**llamamos al metodo para procesar la respuesta del cleinte del inicio de sesion, en dodne recorremos la respuesta y obtenemos 
								 * los datos para posteriormente enviarlos a la base de datos para comprobar que existe para ser verificado**/
								authC2 = procesarInicioSesion(loginSQL2);
								if (authC2) {
									System.out.println("Cliente 2 autenticado correctamente.");
									writer2.println("Autenticación exitosa.");
								} else {
									System.out.println("Error en la autenticación de cliente 2.");
									writer2.println("Autenticación fallida.");
								}
								
								
				
							} catch (Exception e) {
								System.out.println("Error al autenticar cliente 2: " + e.getMessage());
								writer2.println("Error en el inicio de sesión.");
							}
						} else {
							writer2.println("Acción no reconocida.");
						}
					}
				}
				/**despues de que los dos cleintes esten verificados se comprobara si es correcto, caso de serlo se enviara un mensaje al cliente
				 * para indicarle que ha iniciado sesion correctamente, en caso contrario se enviara un vacio en donde internamente el cliente seguira con el bucle 
				 * de seleccion para iniciar sesion o registrarse**/
				if (authC1 && authC2) {
					writer1.print("sesion Iniciada");
					writer2.print("sesion Iniciada");
				}else {
					writer1.print("");
					writer2.print("");
				}

				if(authC1 && authC2) {
					try {
						int indicador = 1;
						JDBCconnection.CrearPartida(indicador, usuario1, usuario2);
					} catch (Exception e) {
						System.out.println("Error en crear la partida: " + e.getMessage());
					}
				}

				/**Despues de que los dos cleintes esten verificados, se activa la logica del juego**/
				if (authC1 && authC2) {
					while (contador <= 3) {
						/**Mediante un option panel, le mostraremos las opciones al cliente**/
						writer1.println("Es tu turno, escribe un mensaje para el Cliente 2:");
						String[] opciones = { "Piedra", "Papel", "Tijera" };
						int panel = JOptionPane.showOptionDialog(null, "Elige una opcion", "jugador 1", 0, 1, null,
								opciones, opciones);
						int opcPLayed1 = panel;

						writer2.println("Es tu turno, escribe un mensaje para el Cliente 1:");
						String[] opciones2 = { "Piedra", "Papel", "Tijera" };
						int panel2 = JOptionPane.showOptionDialog(null, "Elige una opcion", "jugador 2", 0, 1, null,
								opciones2, opciones2);
						int opcPlayed2 = panel2;

						/**todas las opciones se recojen en la variable opcPlayed[numero jugador] y se envia al metodo.game, el cual tiene la logica que se encargara 
						 * de comprobar quien gana o quien pierde y depsues se envia al cada cliente su resusltado**/
						if (opcPLayed1 != 100 && opcPlayed2 != 100) {
							metodos.game(opcPLayed1, opcPlayed2);
							contador++;
						}

					}
					if (contador >= 3) {
						metodos.ganador();
					}
				} else {
					System.out.println("Error en la autenticacion de los usuarios");
				}

			} catch (IOException e) {
				System.out.println("Error en la comunicación entre clientes: " + e.getMessage());
			}

		} catch (

		IOException e) {
			System.out.println("Ha ocurrido un error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void procesarRegistro(String mensaje) throws Exception {
		int indexValues = mensaje.indexOf("VALUES");
		if (indexValues != -1) {
			/**Se extraen y se limpial los valores de la respuesta del cleinte hacia el servidor**/
			String parts = mensaje.substring(indexValues + 7).trim();
			/**Quitamos los paretensis**/
			parts = parts.substring(1, parts.length() - 1);
			/**Separamos los valores para posteriromente guardarlos en euna variable**/
			String[] values = parts.split(", ");
			String usuario = values[0].replace("'", "").trim();
			String contraseña = values[1].replace("'", "").trim();

			/**Enviamos los valores a la bas e de datos para crear el jugador**/
			JDBCconnection.CrearUsuario(usuario, contraseña);
		} else {
			throw new IllegalArgumentException("Formato de mensaje incorrecto para registro.");
		}
	}
	
	private String procesarRegistro2(String mensaje) throws Exception {
		int indexValues = mensaje.indexOf("VALUES");
		if (indexValues != -1) {
			/**Se extraen y se limpial los valores de la respuesta del cleinte hacia el servidor**/
			String parts = mensaje.substring(indexValues + 7).trim();
			/**Quitamos los paretensis**/
			parts = parts.substring(1, parts.length() - 1);
			/**Separamos los valores para posteriromente guardarlos en euna variable**/
			String[] values = parts.split(", ");
			String usuario = values[0].replace("'", "").trim();


			/**Enviamos el noimbre dle usuario**/
			return usuario;
		} else {
			throw new IllegalArgumentException("Formato de mensaje incorrecto para registro.");
		}
	}
	
	public String Usuarios(String mensaje) throws Exception {
		int indexValues = mensaje.indexOf("VALUES");
		if (indexValues != -1) {
			/**Se extraen y se limpial los valores de la respuesta del cleinte hacia el servidor**/
			String parts = mensaje.substring(indexValues + 7).trim();
			/**Quitamos los paretensis**/
			parts = parts.substring(1, parts.length() - 1);
			/**Separamos los valores para posteriromente guardarlos en euna variable**/
			String[] values = parts.split(", ");
			String nomUsuario = values[0].replace("'", "").trim();


			/**Enviamos el noimbre dle usuario**/
			return nomUsuario;
		} else {
			throw new IllegalArgumentException("Formato de mensaje incorrecto para registro.");
		}
	}

	private boolean procesarInicioSesion(String mensaje) throws Exception {
		String[] parts = mensaje.split("WHERE");
		if (parts.length == 2) {
			/**Extraemos los valores con los cuales localizaremos al usuario para verificarlo en el inicio de sesion **/
			String condicion = parts[1].trim();
			String[] campos = condicion.split("AND");

			/**Separamos el valor del usuario y la contraselña ty lo guardamos en variables**/
			String usuario = campos[0].split("=")[1].replace("'", "").trim();
			String contraseña = campos[1].split("=")[1].replace("'", "").trim();

			/**enviamos la variables para verificar el suaurio**/
			return JDBCconnection.InitSesion(usuario, contraseña) == 1;
		} else {
			throw new IllegalArgumentException("Formato de mensaje incorrecto para inicio de sesión.");
		}
	}

}
