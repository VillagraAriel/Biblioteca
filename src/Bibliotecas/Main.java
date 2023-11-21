package Bibliotecas;
import java.util.*;

import Bibliotecas.Prestamos;
import Bibliotecas.Usuarios;
import Bibliotecas.Devoluciones;
import Bibliotecas.Libro;
import Bibliotecas.Biblioteca;
import Bibliotecas.Biblioteca;



public class Main {
	

	public static void main(String[] args) {
    		
		
		
		Biblioteca metodos = new Biblioteca();
	    	String archivoUsuarios = "usuarios.txt"; 
	    	String archivoLibro = "libros.txt";
	    	String archivoPrestamo = "Prestado.txt";
	    	String ArchivoRegistro = "Registro.txt"; 
	    	String ArchivoBajaLibro = "BajaLibri.txt"; 

	    	Scanner scanner = new Scanner(System.in);
		 
		    boolean salir = false;		   
		    boolean continuar = true;
		    int opcion = 0;
		    
		    while (!salir) {
		        do {
		            try {
		                Biblioteca.mostrarMenuPrincipal();
		                continuar = true;
		                opcion = scanner.nextInt();
		                if (opcion < 0 || opcion >= 7) {
		                	System.out.println("Opcion incorrecta...");
		                	continuar = false;
		                }
		            } catch (InputMismatchException e) {
		                System.out.println("Debe ser numérico\n");
		                scanner.next();
		                continuar = false;
		            }
		        } while (!continuar);

		        switch (opcion) {
			        case 1: 
					    boolean salirUsuarios = false;
					    // Menú de gestión de usuarios
			            while (!salirUsuarios) {
			                Biblioteca.mostrarMenuGestionUsuarios();
			                boolean continuarUsuarios = true;
			                int opcionUsuarios = -1;
			                do {
			                    try {
			                        opcionUsuarios = scanner.nextInt();
			                        continuarUsuarios = true;
			                        if (opcion < 0 || opcion > 3) {
					                	System.out.println("Opcion incorrecta...");
					                	continuarUsuarios = false;
					                }
			                        
			                    } catch (InputMismatchException e) {
			                        System.out.println("Debe ser numérico\n");
			                        scanner.next();
			                        continuarUsuarios = false;
			                    }
			                } while (!continuarUsuarios);
	
			                switch (opcionUsuarios) {
			                    case 1:

			                        Biblioteca.AltaUsuario(archivoUsuarios);
			                        break;
	
			                    case 2:
						        	System.out.println("\n\n");

			                        Biblioteca.BajaUsuario(archivoUsuarios, archivoPrestamo );
			                        break;
	
			                    case 3:
			                        Biblioteca.ModificarUsuario("usuarios.txt");
			                        
			                        break;
	
			                    case 0:
			                        salirUsuarios = true;
			                        break;
	
			                    default:
			                        System.out.println("Opción no válida. Intente de nuevo.");
			                }
			            }
		            break;

		            case 2:
		            	boolean salirLibro = false;
		            	while(!salirLibro) {
		            		
		            		boolean continuarLibro = true;
			                Biblioteca.mostrarMenuGestionLibros();
			                int opcionLibro2 =0;
		                	do {
		                		try {
		                			opcionLibro2 = scanner.nextInt();
					                continuarLibro = true;
					                if (opcion < 0 || opcion > 3) {
					                	System.out.println("Opcion incorrecta...");
					                	continuarLibro = false;
					                }
					                
		                		}catch(InputMismatchException e) {
			                        System.out.println("Debe ser numérico\n");
			                        scanner.next();
			                        continuarLibro = false;
		                			
		                		}
		                	} while(!continuarLibro);
		                	
			                switch (opcionLibro2) {
			                    case 1:
			                        Biblioteca.AgregarAltaLibro("libros.txt");
			                        break;
	
			                    case 2:
			                        Biblioteca.BajaLibro(archivoLibro, archivoPrestamo, ArchivoRegistro);

			                        break;
				                       
			                    case 3:
			                    	Biblioteca.ModificarLibro(archivoLibro);
			                    	break;
	
			                    case 0:
			                        salirLibro = true;
			                        break;
			                    default:
			                        System.out.println("Opción no válida. Intente de nuevo.");
			                }
			                
		            	}break;

		            case 3:
		            	

		            	metodos.prestarLibro();
		            	
		                break;

		            case 4:
		            	
		            	metodos.devolverLibro();
		            	
		                break;

		            case 5:
		            	metodos.mostrarDisponibilidadLibros();
		                break;
		                

		            case 6:
		                boolean salir1 = false;
		                while (!salir1) {
		                    boolean continuar1 = true;
		                    Biblioteca.GestionInformes();
		                    int opc = 0;
		                    do {
		                        try {
		                            opc = scanner.nextInt();
		                            continuar1 = true;
		                            if (opc < 0 || opc > 3) { 
		                                System.out.println("Opcion incorrecta...");
		                                continuar1 = false;
		                            }

		                        } catch (InputMismatchException e) {
		                            System.out.println("Debe ser numérico\n");
		                            scanner.next();
		                            continuar1 = false;

		                        }
		                    } while (!continuar1);

		                    switch (opc) {
		                        case 1:
		                            String[] estadisticas = metodos.generarInforme();
		                            for (String estadistica : estadisticas) {
		                                System.out.println(estadistica);
		                            }
		                            break;

		                        case 2:
		                            metodos.contarLibrosUnicos();
		                            break;

		                        case 3:
		                            metodos.usuariosConMasLibrosPorDevolver();
		                            break;

		                        case 0:
		                            salir1 = true; 
		                            break;
		                        default:
		                            System.out.println("Opción no válida. Intente de nuevo.");
		                    }
		                }
		                break;
		            case 0:
		                salir = true;
		                break;

		            default:
		                System.out.println("Opción no válida. Intente de nuevo.");
		        }
		    }

		    System.out.print("¿Desea salir ? (Sí o No): ");
		    String respuesta = scanner.next().toLowerCase();
		    if (respuesta.equals("si")) {
		        System.out.println("Has salido......");
		    }

		    scanner.close();
		}
}