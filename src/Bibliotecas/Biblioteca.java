package Bibliotecas;
import java.util.*;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.Date;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import Bibliotecas.Devoluciones;
import Bibliotecas.Libro;
import Bibliotecas.Prestamos;
import Bibliotecas.Usuarios;

import java.text.ParseException;
import java.text.SimpleDateFormat;




public class Biblioteca {
    
    private String ARCHIVO_PRESTADOS = "prestado.txt";
    private String ARCHIVO_LIBROS = "libros.txt";
    private String ArchivoUsuario = "Usuarios.txt";
    private String ArchivoRegistro = "Registro.txt";
    private String ArchivoBaja = "Baja.txt";
	Scanner entrada = new Scanner(System.in);
    
    
	public static void AltaUsuario(String ArchivoUsuario) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(ArchivoUsuario, true));

            Scanner entrada = new Scanner(System.in);
            System.out.println("Alta de usuario:");

            int id = 0;
            String nombre = "";
            String apellido = "";
            int dni = 0;

            // Solicitar el ID del usuario (con manejo de excepciones y validación de unicidad)
            boolean idValido = false;
            while (!idValido) {
                try {
                    System.out.print("ID (hasta 5 dígitos): ");
                    id = entrada.nextInt();

                    // Validar que el ID tenga hasta 5 dígitos
                    if (id >= 0 && id <= 99999) {
                        // Validar unicidad del ID
                        if (!existeUsuarioConID(id, ArchivoUsuario)) {
                            idValido = true;
                        } else {
                            System.out.println("Error: Ya existe un usuario con el mismo ID. Intente con otro.");
                        }
                    } else {
                        System.out.println("Error: El ID debe tener hasta 5 dígitos.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Error: Debe ingresar un número entero para el ID.");
                    entrada.nextLine();  // Limpiar el buffer de entrada
                }
            }

            // Validar unico del DNI
            boolean dniValido = false;
            while (!dniValido) {
                try {
                    System.out.print("DNI (8 dígitos numéricos): ");
                    dni = entrada.nextInt();

                    if (String.valueOf(dni).matches("\\d{8}")) {
                        // Validar unicidad del DNI
                        if (!existeUsuarioConDNI(dni, ArchivoUsuario)) {
                            dniValido = true;
                        } else {
                            System.out.println("Error: Ya existe un usuario con el mismo DNI. Intente con otro.");
                        }
                    } else {
                        System.out.println("Error: Debe ingresar un DNI válido con 8 dígitos numéricos.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Error: Debe ingresar un número entero para el DNI.");
                    entrada.nextLine();  // Limpiar el buffer de entrada
                }
            }

            // Solicitar el nombre del usuario (con validación de tipo de datos)
            System.out.print("Nombre: ");
            nombre = entrada.next();

            // Solicitar el apellido del usuario (con validación de tipo de datos)
            System.out.print("Apellido: ");
            apellido = entrada.next();

            // Crear un objeto Usuario con el constructor
            Usuarios nuevoUsuario = new Usuarios(id, nombre, apellido, dni);

            // Escribir los datos del usuario en el archivo
            writer.write(nuevoUsuario.getId() + "," + nuevoUsuario.getNombre() + "," + nuevoUsuario.getApellido() + "," + nuevoUsuario.getDni());
            writer.newLine();
            writer.close();
            System.out.println("Usuario dado de alta exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al dar de alta el usuario: " + e.getMessage());
        }
    }

    private static boolean existeUsuarioConID(int id, String archivoUsuarios) throws IOException {
        // verifica si existe el mismo id
    	
    	try (BufferedReader reader = new BufferedReader(new FileReader(archivoUsuarios))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] campos = linea.split(",");
                if (campos.length >= 1) {
                    int idUsuario = Integer.parseInt(campos[0]);
                    if (idUsuario == id) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean existeUsuarioConDNI(int dni, String archivoUsuarios) throws IOException {
        // Verifica si existe el mismp DNI
    	try (BufferedReader reader = new BufferedReader(new FileReader(archivoUsuarios))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] campos = linea.split(",");
                if (campos.length >= 4) {
                    int dniUsuario = Integer.parseInt(campos[3]);
                    if (dniUsuario == dni) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


   
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static void BajaUsuario(String ArchivoUsuarios, String ARCHIVO_PRESTADOS) {
        try {
            File archivoOriginal = new File(ArchivoUsuarios);
            Scanner entrada = new Scanner(System.in);

            System.out.println("Eliminar un usuario:");
            String idEliminar;

            do {
                System.out.print("Ingrese el ID del usuario a eliminar: ");
                idEliminar = entrada.next().trim();
            } while (idEliminar.isEmpty());

            String linea;
            boolean usuarioEncontrado = false;
            boolean tienePrestamos = false;

            // Crear un nuevo archivo temporal para almacenar los datos sin el usuario a eliminar
            File archivoTemp = new File("temporal.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(archivoTemp));

            // Verificar si el usuario está en el archivo de usuarios
            try (BufferedReader reader = new BufferedReader(new FileReader(archivoOriginal))) {
                while ((linea = reader.readLine()) != null) {
                    String[] campos = linea.split(",");
                    if (campos.length >= 2) {
                        String idUsuario = campos[0].trim(); // Suponiendo que la primera posición es el ID del usuario
                        if (idUsuario.equals(idEliminar)) {
                            usuarioEncontrado = true;
                            continue; 
                        }
                    }
                    writer.write(linea);
                    writer.newLine();
                }
            }

            writer.close();

            // Verificar si el usuario tiene préstamos pendientes
            try (BufferedReader prestadosReader = new BufferedReader(new FileReader(ARCHIVO_PRESTADOS))) {
                String prestamoLinea;
                while ((prestamoLinea = prestadosReader.readLine()) != null) {
                    String[] prestamoCampos = prestamoLinea.split(",");
                    if (prestamoCampos.length >= 2 && prestamoCampos[1].trim().equals(idEliminar)) {
                        tienePrestamos = true;
                        break;
                    }
                }
            }

            if (usuarioEncontrado) {
                if (tienePrestamos) {
                    System.out.println("El usuario tiene libros prestados y no se puede eliminar.");
                } else {
                    System.out.print("¿Está seguro de que desea eliminar al usuario? (Sí o No): ");
                    String confirmacion = entrada.next();

                    if (confirmacion.equalsIgnoreCase("Sí") || confirmacion.equalsIgnoreCase("Si")) {
                        // Renombrar el archivo temporal como el archivo original
                        archivoOriginal.delete();
                        archivoTemp.renameTo(archivoOriginal);
                        System.out.println("Usuario eliminado exitosamente.");
                    } else {
                        System.out.println("Eliminación cancelada.");
                    }
                }
            } else {
                System.out.println("No se encontró ningún usuario con el ID proporcionado.");
            }
        } catch (IOException e) {
            System.err.println("Error al eliminar el usuario: " + e.getMessage());
        }
    }
	
    
    
    
    
    
    
    
    
    
    public static void ModificarUsuario(String ArchivoUsuarios) {
        try {
            File archivoOriginal = new File(ArchivoUsuarios);
            Scanner entrada = new Scanner(System.in);

            List<String> usuarios = new ArrayList<>();

            // Leer la información del archivo y almacenarla en la lista
            try (BufferedReader reader = new BufferedReader(new FileReader(archivoOriginal))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    usuarios.add(linea);
                }
            }

            System.out.print("Ingrese el ID del usuario a modificar: ");
            int idModificar = entrada.nextInt();

            boolean usuarioEncontrado = false;
            for (int i = 0; i < usuarios.size(); i++) {
                String linea = usuarios.get(i);
                String[] campos = linea.split(",");
                if (campos.length >= 4) {
                    int idUsuario = Integer.parseInt(campos[0]);
                    if (idUsuario == idModificar) {
                        usuarioEncontrado = true;
                        System.out.print("Nuevo Nombre: ");
                        String nuevoNombre = obtenerTextoSoloLetras(entrada);
                        campos[1] = nuevoNombre;

                        System.out.print("Nuevo Apellido: ");
                        String nuevoApellido = obtenerTextoSoloLetras(entrada);
                        campos[2] = nuevoApellido;

                        // Validar único del DNI
                        boolean dniValido = false;
                        int nuevoDNI=0;

                        while (!dniValido) {
                            try {
                                System.out.print("DNI (8 dígitos numéricos): ");
                                nuevoDNI = entrada.nextInt();

                                if (String.valueOf(nuevoDNI).matches("\\d{8}") && !existeUsuarioConDNI(idModificar, nuevoDNI, ArchivoUsuarios)) {
                                    dniValido = true;
                                } else {
                                    System.out.println("Error: Ya existe un usuario con el mismo DNI o el formato no es válido. Intente con otro.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Error: Debe ingresar un número entero para el DNI.");
                                entrada.nextLine();  // Limpiar el buffer de entrada
                            }
                        }

                        // Convertir nuevoDNI a String antes de asignarlo al array
                        campos[3] = String.valueOf(nuevoDNI);

                        // Modificar los datos del usuario en la lista
                        usuarios.set(i, String.join(",", campos));
                    }
                }
            }

            if (usuarioEncontrado) {
                // Sobreescribir el archivo original con la información modificada
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoOriginal))) {
                    for (String usuario : usuarios) {
                        writer.write(usuario);
                        writer.newLine();
                    }
                }
                System.out.println("Datos de usuario modificados exitosamente.");
            } else {
                System.out.println("No se encontró ningún usuario con el ID proporcionado.");
            }
        } catch (IOException e) {
            System.err.println("Error al modificar los datos del usuario: " + e.getMessage());
        }
    }

    private static String obtenerTextoSoloLetras(Scanner entrada) {
        String texto;
        do {
            System.out.print("Ingrese texto (solo letras): ");
            texto = entrada.next();
        } while (!texto.matches("[a-zA-Z]+"));
        return texto;
    }

    private static boolean existeUsuarioConDNI(int idModificar, int nuevoDNI, String archivoUsuarios) {
        // Implementa la lógica para verificar si ya existe un usuario con el nuevo DNI
    	try (BufferedReader reader = new BufferedReader(new FileReader(archivoUsuarios))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] campos = linea.split(",");
                if (campos.length >= 4) {
                    int idUsuario = Integer.parseInt(campos[0]);
                    int dniUsuario = Integer.parseInt(campos[3]);
                    if (idUsuario != idModificar && dniUsuario == nuevoDNI) {
                        // Ya existe un usuario con el mismo DNI
                        return true;
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al verificar la existencia de usuario con DNI: " + e.getMessage());
        }
        // No se encontró un usuario con el mismo DNI o hubo un error
        return false;
    }
      
		
	
	
	
	
	
	

    
    
    public static void AgregarAltaLibro(String ARCHIVO_LIBROS) {
			
			 int id = 0;
		        String Titulo = "";
		        String Autor = "";
		        int AñoDePublicacion = 0;
		        Scanner entrada = new Scanner(System.in);
		        try {
		            File archivo = new File("libros.txt");

		            if (!archivo.exists()) {
		                // El archivo no existe, así que aquí puedes crearlo y agregar encabezados si es necesario.
		                // Luego, registra el libro.
		            	archivo.createNewFile();

		            }

		            BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_LIBROS, true));
		            boolean codigoExistente = false;
		            while (true) {
		                System.out.println("Id libro (hasta 5 dígitos): ");
		                String idStr = entrada.next();

		                if (idStr.matches("\\d{1,5}")) {
		                    id = Integer.parseInt(idStr);
		                    codigoExistente = existeCodigoLibro(id, ARCHIVO_LIBROS);

		                    if (codigoExistente) {
		                        System.out.println("Error: El código del libro ya está en uso. Intente con otro.");
		                    } else {
		                        break; // Salir del bucle si el ID es válido
		                    }
		                } else {
		                    System.out.println("Error: Debe ingresar un número entero de hasta 5 dígitos.");
		                }
		                
			            entrada.nextLine(); 
		            }
		        
		            entrada.nextLine(); 

		         // Solicitar el título del libro (con validación)
		         boolean tituloValido = false;

		         while (!tituloValido) {
		             System.out.println("Título del libro: ");
		             Titulo = entrada.nextLine();
		             if (Titulo.matches("^[a-zA-Z0-9_ ]+$")) {
		                 tituloValido = true;
		             } else {
		                 System.out.println("Error: Debes ingresar un título válido (solo letras, números y espacios).");
		             }
		         }

		            // Solicitar el autor del libro (validación)
		            boolean autorValido = false;

		            while (!autorValido) {
		                System.out.println("Autor: ");
		                Autor = entrada.nextLine();
		                if (Autor.matches("^[a-zA-Z ]+$")) {
		                    autorValido = true;
		                } else {
		                    System.out.println("Error: Debes ingresar un autor válido (solo letras y espacios).");
		                }
		            }

		            // Solicitar el año de publicación (validación)
		            boolean añoValido = false;

		            while (!añoValido) {
		                try {
		                    System.out.println("Año de publicación (entre 0 y 2023): ");
		                    AñoDePublicacion = entrada.nextInt();
		                    if (AñoDePublicacion >= 0 && AñoDePublicacion <= 2023) {
		                        añoValido = true;
		                    } else {
		                        System.out.println("Error: Debes ingresar un año válido dentro del rango.");
		                    }
		                } catch (InputMismatchException e) {
		                    System.out.println("Error: Debes ingresar un número entero para el año de publicación.");
		                    entrada.nextLine(); // Limpiar el buffer de entrada
		                }
		            }

		            // Resto del código para escribir los datos en el archivo de libros
		            String estado = "disponible";
		            writer.write(id + "," + Titulo + "," + Autor + "," + AñoDePublicacion + "," + estado);
		            writer.newLine(); // Asegura que haya un salto de línea después de cada libro
		            writer.close();
		            System.out.println("Libro cargado exitosamente.");
		        } catch (IOException e) {
		            System.out.println("Error al escribir en el archivo de la biblioteca: " + e.getMessage());
		        }
		}	
	
        private static boolean existeCodigoLibro(int codigo, String ARCHIVO_LIBROS) {
            try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_LIBROS))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    String[] campos = linea.split(",");
                    if (campos.length > 0) {
                        int codigoExistente = Integer.parseInt(campos[0]);
                        if (codigoExistente == codigo) {
                            return true;
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error al verificar el código del libro: " + e.getMessage());
            }
            return false;
        }


        
        
        






        public static void BajaLibro(String ArchivoLibro, String ArchivoPrestado, String ArchivoRegistro) {
            try {
                File archivoOriginal = new File(ArchivoLibro);
                File archivoTemporal = new File("LibroTemporal.txt");
                BufferedReader reader = new BufferedReader(new FileReader(archivoOriginal));
                BufferedWriter writer = new BufferedWriter(new FileWriter(archivoTemporal));
                Scanner entrada = new Scanner(System.in);

                System.out.println("Eliminar un libro:");
                System.out.print("Ingrese el código del libro a eliminar: ");
                String codigoEliminar = entrada.next();
                String linea;
                boolean libroEncontrado = false;

                // Verificar si el libro está prestado
                File archivoPrestadoFile = new File(ArchivoPrestado);
                Scanner scannerPrestado = new Scanner(archivoPrestadoFile);

                while (scannerPrestado.hasNextLine()) {
                    String lineaPrestado = scannerPrestado.nextLine();
                    if (lineaPrestado.startsWith(codigoEliminar + ",")) {
                        libroEncontrado = true;
                        break;
                    }
                }

                scannerPrestado.close();

                // Continuar con la eliminación del libro
                while ((linea = reader.readLine()) != null) {
                    String[] campos = linea.split(",");
                    if (campos.length >= 4) {
                        String codigolibro = campos[0];
                        if (!codigolibro.equals(codigoEliminar)) {
                            writer.write(linea);
                            writer.newLine();
                        } else {
                            libroEncontrado = true;
                        }
                    }
                }

                reader.close();
                writer.close();

                if (libroEncontrado) {
                    System.out.print("¿Está seguro de que desea eliminar el libro? (Sí o No): ");
                    String confirmacion = entrada.next();
                    if (confirmacion.equalsIgnoreCase("Sí") || confirmacion.equalsIgnoreCase("Si")) {
                        // Eliminar el libro del archivo prestado.txt
                        quitarLibroPrestado(ArchivoPrestado, codigoEliminar);

                        // Eliminar el libro del archivo registro.txt
                        quitarLibroRegistro(ArchivoRegistro, codigoEliminar);

                        // Renombrar el archivo temporal como el archivo original
                        archivoOriginal.delete();
                        archivoTemporal.renameTo(archivoOriginal);
                        System.out.println("Libro eliminado exitosamente.");
                    } else {
                        System.out.println("Eliminación cancelada.");
                    }
                } else {
                    System.out.println("No se encontró ningún libro con el código proporcionado.");
                }
            } catch (IOException e) {
                System.err.println("Error al eliminar el libro: " + e.getMessage());
            }
        }

        private static void quitarLibroPrestado(String ArchivoPrestado, String codigoEliminar) {
            try {
                File archivoPrestado = new File(ArchivoPrestado);
                File archivoTemporal = new File("PrestadoTemporal.txt");
                BufferedReader reader = new BufferedReader(new FileReader(archivoPrestado));
                BufferedWriter writer = new BufferedWriter(new FileWriter(archivoTemporal));

                String linea;
                while ((linea = reader.readLine()) != null) {
                    if (!linea.startsWith(codigoEliminar + ",")) {
                        writer.write(linea);
                        writer.newLine();
                    }
                }

                reader.close();
                writer.close();

                // Renombrar el archivo temporal como el archivo original
                archivoPrestado.delete();
                archivoTemporal.renameTo(archivoPrestado);
            } catch (IOException e) {
                System.err.println("Error al quitar el libro prestado: " + e.getMessage());
            }
        }
    
        private static void quitarLibroRegistro(String ArchivoRegistro, String codigoEliminar) {
            try {
                File archivoRegistro = new File(ArchivoRegistro);
                File archivoTemporal = new File("RegistroTemporal.txt");
                BufferedReader reader = new BufferedReader(new FileReader(archivoRegistro));
                BufferedWriter writer = new BufferedWriter(new FileWriter(archivoTemporal));

                String linea;
                while ((linea = reader.readLine()) != null) {
                    if (!linea.startsWith(codigoEliminar + ",")) {
                        writer.write(linea);
                        writer.newLine();
                    }
                }

                reader.close();
                writer.close();

                // Renombrar el archivo temporal como el archivo original
                archivoRegistro.delete();
                archivoTemporal.renameTo(archivoRegistro);
            } catch (IOException e) {
                System.err.println("Error al quitar el libro del registro: " + e.getMessage());
            }
        }
        
        
        
        
        
        
        public static void ModificarLibro(String ARCHIVO_LIBROS) {
            try {
                File archivoOriginal = new File(ARCHIVO_LIBROS);
                BufferedReader reader = new BufferedReader(new FileReader(archivoOriginal));
                ArrayList<String> lineas = new ArrayList<>();
                Scanner entrada = new Scanner(System.in);

                System.out.println("Modificar un libro:");
                System.out.print("Ingrese el código del libro a modificar: ");
                String codigoModificar = entrada.next();
                entrada.nextLine(); // Limpiar el buffer de entrada

                String linea;

                boolean libroEncontrado = false;

                while ((linea = reader.readLine()) != null) {
                    String[] campos = linea.split(",");
                    if (campos.length >= 5) {
                        String codigolibro = campos[0];
                        String estado = campos[4];
                        if (codigolibro.equals(codigoModificar)) {
                            libroEncontrado = true;
                            if (estado.equalsIgnoreCase("disponible")) {
                                // Solicitar el título del libro (con validación)
                                boolean tituloValido = false;

                                while (!tituloValido) {
                                    System.out.print("Título del libro: ");
                                    String nuevoTitulo = entrada.nextLine();

                                    if (nuevoTitulo.matches("^[a-zA-Z0-9_ ]+$")) {
                                        tituloValido = true;
                                        campos[1] = nuevoTitulo;
                                    } else {
                                        System.out.println("Error: Debes ingresar un título válido (solo letras, números y espacios).");
                                    }
                                }

                                // Solicitar el autor del libro (con validación)
                                boolean autorValido = false;

                                while (!autorValido) {
                                    System.out.print("Autor: ");
                                    String nuevoAutor = entrada.nextLine();

                                    if (nuevoAutor.matches("^[a-zA-Z ]+$")) {
                                        autorValido = true;
                                        campos[2] = nuevoAutor;
                                    } else {
                                        System.out.println("Error: Debes ingresar un autor válido (solo letras y espacios).");
                                    }
                                }

                                // Solicitar el año de publicación (con validación de rango)
                                boolean añoValido = false;

                                while (!añoValido) {
                                    try {
                                        System.out.print("Año de publicación (entre 0 y 2023): ");
                                        int nuevoAñoDePublicacion = entrada.nextInt();

                                        if (nuevoAñoDePublicacion >= 0 && nuevoAñoDePublicacion <= 2023) {
                                            añoValido = true;
                                            campos[3] = String.valueOf(nuevoAñoDePublicacion);
                                        } else {
                                            System.out.println("Error: Debes ingresar un año válido dentro del rango.");
                                        }
                                    } catch (InputMismatchException e) {
                                        System.out.println("Error: Debes ingresar un número entero para el año de publicación.");
                                        entrada.nextLine(); // Limpiar el buffer de entrada
                                    }
                                }

                                // Generar la línea modificada
                                linea = String.join(",", campos);
                                System.out.println("Libro modificado exitosamente.");
                            } else {
                                // El libro no está disponible para modificación
                                System.out.println("No se puede modificar el libro porque está prestado.");
                            }
                        }
                    }
                    lineas.add(linea);
                }

                if (!libroEncontrado) {
                    System.out.println("El libro no existe !!!");
                }

                reader.close();

                // Sobrescribir el archivo original con las líneas modificadas
                BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_LIBROS));
                for (String l : lineas) {
                    writer.write(l);
                    writer.newLine();
                }
                writer.close();

            } catch (IOException e) {
                System.err.println("Error al modificar el libro: " + e.getMessage());
            }
        }
   
        
        
        
        
        
        
        
        public void prestarLibro() {
            try {
                // Verificar si el archivo "prestado.txt" existe y crearlo si no
                File archivoPrestados = new File(ARCHIVO_PRESTADOS);
                if (!archivoPrestados.exists()) {
                    archivoPrestados.createNewFile();
                }

                File archivoRegistro = new File("registro.txt");
                if (!archivoRegistro.exists()) {
                    archivoRegistro.createNewFile();
                }

                Scanner entrada = new Scanner(System.in);
                String idLibro;
                int idLibroInt;

                boolean entradaValida = false;

                do {
                    System.out.print("Ingrese el ID del libro a prestar (hasta 5 dígitos): ");
                    idLibro = entrada.next();

                    try {
                        idLibroInt = Integer.parseInt(idLibro);
                        if (idLibroInt >= 0 && idLibroInt <= 99999) {
                            entradaValida = true;
                        } else {
                            System.out.println("Error: El ID debe ser un número entero de hasta 5 dígitos.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Debe ingresar un número entero válido.");
                    }
                } while (!entradaValida);

                // Leer información de libros desde "libro.txt"
                boolean encontrado = false;
                String linea;

                List<String> lineasLibros = new ArrayList<>(); // Lista para almacenar las líneas de libros

                try (BufferedReader librosReader = new BufferedReader(new FileReader(ARCHIVO_LIBROS));
                     BufferedWriter prestadosWriter = new BufferedWriter(new FileWriter(ARCHIVO_PRESTADOS, true));
                     BufferedWriter registroWriter = new BufferedWriter(new FileWriter("registro.txt", true))) {

                    while ((linea = librosReader.readLine()) != null) {
                        String[] campos = linea.split(",");
                        if (campos.length >= 5 && campos[0].equals(idLibro) && campos[4].equals("disponible")) {
                            // Cambiar la disponibilidad del libro a "no disponible"
                            campos[4] = "no disponible";

                            System.out.print("Ingrese el ID del cliente (hasta 5 dígitos): ");
                            String idCliente = entrada.next();

                            boolean clienteValido = false;

                            do {
                                try {
                                    int idClienteInt = Integer.parseInt(idCliente);
                                    if (idClienteInt >= 0 && idClienteInt <= 99999) {
                                        clienteValido = true;
                                    } else {
                                        System.out.println("Error: El ID del cliente debe ser un número entero de hasta 5 dígitos.");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Error: Debe ingresar un número entero válido para el ID del cliente.");
                                }

                                if (!clienteValido) {
                                    System.out.print("Ingrese el ID del cliente (hasta 5 dígitos): ");
                                    idCliente = entrada.next();
                                }
                            } while (!clienteValido);

                            if (usuarioExiste(idCliente)) {
                                // Obtener la fecha actual
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                String fechaPrestamo = sdf.format(new Date());

                                // Buscar el libro en el archivo de registro y actualizar la cantidad
                                boolean encontradoEnRegistro = false;
                                List<String> lineasRegistro = new ArrayList<>();

                                try (BufferedReader registroReader = new BufferedReader(new FileReader("registro.txt"))) {
                                    String registroLinea;
                                    while ((registroLinea = registroReader.readLine()) != null) {
                                        String[] registroCampos = registroLinea.split(",");
                                        if (registroCampos.length >= 2 && registroCampos[0].equals(idLibro)) {
                                            // El libro ya está en el registro, incrementar la cantidad
                                            int cantidadPrestada = Integer.parseInt(registroCampos[1]) + 1;
                                            registroCampos[1] = String.valueOf(cantidadPrestada);
                                            encontradoEnRegistro = true;
                                        }
                                        lineasRegistro.add(String.join(",", registroCampos));
                                    }
                                }

                                // Si el libro no está en el registro, agregarlo con cantidad 1
                                if (!encontradoEnRegistro) {
                                    lineasRegistro.add(idLibro + ",1");
                                }

                                // Actualizar el archivo de registro con la nueva información
                                try (BufferedWriter registroWriter1 = new BufferedWriter(new FileWriter("registro.txt"))) {
                                    for (String registroLinea : lineasRegistro) {
                                        registroWriter1.write(registroLinea);
                                        registroWriter1.newLine();
                                    }
                                }

                                // Registrar el préstamo en prestado.txt
                                String registroPrestamo = idLibro + "," + idCliente + "," + fechaPrestamo;
                                prestadosWriter.write(registroPrestamo);
                                prestadosWriter.newLine();

                                System.out.println("Libro prestado exitosamente.");
                            } else {
                                System.out.println("El usuario con ID " + idCliente + " no está registrado.");
                            }
                        }
                        lineasLibros.add(String.join(",", campos));
                    }

                    // Reemplazar el contenido del archivo "libro.txt" con la lista actualizada
                    try (BufferedWriter librosWriter = new BufferedWriter(new FileWriter(ARCHIVO_LIBROS))) {
                        for (String libroLinea : lineasLibros) {
                            librosWriter.write(libroLinea);
                            librosWriter.newLine();
                        }
                    }

                }

                if (!encontrado) {
                    System.out.println("El libro con ID " + idLibro + " ya no está disponible para préstar.");
                }
            } catch (IOException e) {
                System.err.println("Error en la operación de préstamo: " + e.getMessage());
            }
        }
    
   
    
   
        
       
        
        
        
        
        public void devolverLibro() {
    	try {
            // Verificar si el archivo "prestado.txt" existe y crearlo si no
            File archivoPrestados = new File(ARCHIVO_PRESTADOS);
            if (!archivoPrestados.exists()) {
                archivoPrestados.createNewFile();
            }

            Scanner entrada = new Scanner(System.in);
            String idLibro;
            int idLibroInt;

            boolean entradaValida = false;

            do {
                System.out.print("Ingrese el ID del libro a devolver (hasta 5 dígitos): ");
                idLibro = entrada.next();

                try {
                    idLibroInt = Integer.parseInt(idLibro);
                    if (idLibroInt >= 0 && idLibroInt <= 99999) {
                        entradaValida = true;
                    } else {
                        System.out.println("Error: El ID debe ser un número entero de hasta 5 dígitos.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error: Debe ingresar un número entero válido.");
                }
            } while (!entradaValida);

            // Leer información de libros prestados desde "prestado.txt"
            boolean encontrado = false;
            String linea;

            List<String> librosPrestados = new ArrayList<>(); // Lista para libros prestados

            try (BufferedReader prestadosReader = new BufferedReader(new FileReader(ARCHIVO_PRESTADOS))) {
                while ((linea = prestadosReader.readLine()) != null) {
                    String[] campos = linea.split(",");
                    if (campos.length >= 3 && campos[0].equals(idLibro)) {
                        encontrado = true;
                        // Realiza cualquier lógica necesaria para registrar la devolución aquí, por ejemplo, registrar la fecha de devolución
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        
                        boolean fechaValida = false;
                        Date fechaDevolucion = null;

                        do {
                            System.out.print("Ingrese la fecha de devolución (dd/MM/yyyy): ");
                            String fechaDevolucionStr = entrada.next();

                            try {
                                fechaDevolucion = dateFormat.parse(fechaDevolucionStr);
                                if (fechaDevolucionStr.equals(dateFormat.format(fechaDevolucion))) {
                                    fechaValida = true;
                                } else {
                                    System.out.println("Error: Formato de fecha incorrecto. Debe ser (dd/MM/yyyy).");
                                }
                            } catch (ParseException e) {
                                System.err.println("Error: Formato de fecha incorrecto. Debe ser (dd/MM/yyyy).");
                            }
                        } while (!fechaValida);

                        System.out.println("Fecha de devolución válida: " + dateFormat.format(fechaDevolucion));
                        // Continuar con el proceso de devolución...

                        // Modificar la disponibilidad del libro en "libro.txt" a "disponible"
                        actualizarDisponibilidadEnLibro(idLibro, "disponible");

                        System.out.println("Libro devuelto exitosamente.");
                    } else {
                        librosPrestados.add(linea);
                    }
                }
            }

            // Guardar la lista de libros prestados actualizada en el archivo "prestado.txt"
            try (BufferedWriter prestadosWriter = new BufferedWriter(new FileWriter(ARCHIVO_PRESTADOS))) {
                for (String libroPrestado : librosPrestados) {
                    prestadosWriter.write(libroPrestado);
                    prestadosWriter.newLine();
                }
            }

            if (!encontrado) {
                System.out.println("El libro con ID " + idLibro + " no está registrado como prestado.");
            }
        } catch (IOException e) {
            System.err.println("Error en la operación de devolución: " + e.getMessage());
        }
    }

    
   
        
        
        
        
        
        
        
    private void actualizarDisponibilidadEnLibro(String idLibro, String disponibilidad) {
        // Método para actualizar la disponibilidad de un libro en "libro.txt"

    	List<String> lineasLibros = new ArrayList<>();

        try (BufferedReader librosReader = new BufferedReader(new FileReader(ARCHIVO_LIBROS))) {
            String linea;
            while ((linea = librosReader.readLine()) != null) {
                String[] campos = linea.split(",");
                if (campos.length >= 5 && campos[0].equals(idLibro)) {
                    campos[4] = disponibilidad;
                }
                lineasLibros.add(String.join(",", campos));
            }
        } catch (IOException e) {
            System.err.println("Error al actualizar la disponibilidad del libro en libro.txt: " + e.getMessage());
        }

        try (BufferedWriter librosWriter = new BufferedWriter(new FileWriter(ARCHIVO_LIBROS))) {
            for (String libroLinea : lineasLibros) {
                librosWriter.write(libroLinea);
                librosWriter.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al escribir la actualización en libro.txt: " + e.getMessage());
        }
    }
    
    private boolean usuarioExiste(String idCliente) {
        // el usuario con el ID proporcionado existe en el archivo de usuarios (usuarios.txt).
       
    	try (BufferedReader reader = new BufferedReader(new FileReader(ArchivoUsuario))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] campos = linea.split(",");
                if (campos.length >= 4 && campos[0].equals(idCliente)) {
                    return true;  // El usuario con el ID proporcionado existe
                }
            }
        } catch (IOException e) {
            System.err.println("Error al buscar el usuario: " + e.getMessage());
        }
        return false;  // El usuario con el ID proporcionado no existe
    }
    
    
    
   
    
    public void mostrarDisponibilidadLibros() {
        try {
            BufferedReader librosReader = new BufferedReader(new FileReader(ARCHIVO_LIBROS));
            String linea;
            int disponibles = 0;
            int noDisponibles = 0;

            System.out.println("Libros disponibles:");
            System.out.println("-------------------");

            while ((linea = librosReader.readLine()) != null) {
                String[] campos = linea.split(",");
                if (campos.length >= 5) {
                    if (campos[4].equals("disponible")) {
                        System.out.println("ID: " + campos[0] + ", Título: " + campos[1]);
                        disponibles++;
                    } else if (campos[4].equals("no disponible")) {
                        noDisponibles++;
                    }
                }
            }

            System.out.println();
            System.out.println("Libros no disponibles:");
            System.out.println("-----------------------");

            librosReader.close();
            librosReader = new BufferedReader(new FileReader(ARCHIVO_LIBROS));

            while ((linea = librosReader.readLine()) != null) {
                String[] campos = linea.split(",");
                if (campos.length >= 5 && campos[4].equals("no disponible")) {
                    System.out.println("ID: " + campos[0] + ", Título: " + campos[1]);
                }
            }

            System.out.println();
            System.out.println("Total de libros disponibles: " + disponibles);
            System.out.println("Total de libros no disponibles: " + noDisponibles);

            librosReader.close();
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de libros: " + e.getMessage());
        }
    }
    
  
    
    
      
    
    public String[] generarInforme() {
    	try {
            File ARCHIVO_PRESTADOS = new File(ArchivoRegistro);

            // Verificar si el archivo "prestamo.txt" existe y crearlo si no
            if (!ARCHIVO_PRESTADOS.exists()) {
            	ARCHIVO_PRESTADOS.createNewFile();
            }
	        try (BufferedReader registroReader = new BufferedReader(new FileReader(ArchivoRegistro))) {
	            Map<String, Integer> estadisticas = new HashMap<>();
	
	            String linea;
	            while ((linea = registroReader.readLine()) != null) {
	                String[] campos = linea.split(",");
	                if (campos.length >= 2) {
	                    String idLibro = campos[0];
	                    int cantidadPrestamos = Integer.parseInt(campos[1]);
	                    estadisticas.put(idLibro, cantidadPrestamos);
	                }
	            }
	
	            if (!estadisticas.isEmpty()) {
	                return estadisticas.entrySet().stream()
	                        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
	                        .map(entry -> "Libro con ID " + entry.getKey() + ": " + entry.getValue() + " préstamos")
	                        .toArray(String[]::new);
	            } else {
	                return new String[]{"No hay libros prestados registrados en el archivo."};
	            }
	        }
	        } catch (IOException e) {
	            System.err.println("Error al generar el informe de estadísticas: " + e.getMessage());
	            return new String[]{"Error al generar el informe de estadísticas."};
	        }
    	
    }

      
    
    


    
    public void contarLibrosUnicos() {
	        // Cantidad total de libros que hay en la biblioteca incluye todos de la lista de libros
    	try (BufferedReader librosReader = new BufferedReader(new FileReader("libros.txt"))) {
	            Set<String> librosUnicos = new HashSet<>();
	            String linea;
	
	            while ((linea = librosReader.readLine()) != null) {
	                String[] campos = linea.split(",");
	                if (campos.length >= 1) {
	                    String idLibro = campos[0].trim(); // Asegúrate de eliminar espacios en blanco
	                    librosUnicos.add(idLibro);
	                }
	            }

            int cantidadLibrosUnicos = librosUnicos.size();
            System.out.println("Cantidad de libros únicos en la biblioteca: " + cantidadLibrosUnicos);
        } catch (IOException e) {
            System.err.println("Error al contar libros: " + e.getMessage());
        }
    }

    
   
    
    
    
    
    
    
    public void usuariosConMasLibrosPorDevolver() {
        try {
            File ARCHIVO_PRESTADOS = new File("prestado.txt");

            // Verificar si el archivo "prestado.txt" existe y crearlo si no
            if (!ARCHIVO_PRESTADOS.exists()) {
                ARCHIVO_PRESTADOS.createNewFile();
            }

            // Crear un mapa para almacenar la cantidad de libros por devolver por usuario
            Map<String, Integer> usuariosLibrosPorDevolver = new HashMap<>();

            // Leer el archivo "prestado.txt" para obtener los IDs de los usuarios con libros por devolver
            try (BufferedReader prestadosReader = new BufferedReader(new FileReader(ARCHIVO_PRESTADOS))) {
                String linea;
                while ((linea = prestadosReader.readLine()) != null) {
                    String[] campos = linea.split(",");
                    if (campos.length >= 2) {
                        String idUsuario = campos[1].trim();
                        usuariosLibrosPorDevolver.put(idUsuario, usuariosLibrosPorDevolver.getOrDefault(idUsuario, 0) + 1);
                    }
                }
            }

            // Leer el archivo "usuarios.txt" para obtener los nombres de los usuarios
            try (BufferedReader usuariosReader = new BufferedReader(new FileReader("usuarios.txt"))) {
                Map<String, String> usuariosNombres = new HashMap<>();
                String linea;
                while ((linea = usuariosReader.readLine()) != null) {
                    String[] campos = linea.split(",");
                    if (campos.length >= 2) {
                        String idUsuario = campos[0].trim();
                        String nombreUsuario = campos[1].trim();

                        usuariosNombres.put(idUsuario, nombreUsuario);
                    }
                }

                // Crear una lista de usuarios ordenados de mayor a menor según la cantidad de libros por devolver
                List<Map.Entry<String, Integer>> usuariosOrdenados = new ArrayList<>(usuariosLibrosPorDevolver.entrySet());
                usuariosOrdenados.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

                if (!usuariosOrdenados.isEmpty()) {
                    System.out.println("Usuarios con libros por devolver (de mayor a menor):");
                    for (Map.Entry<String, Integer> entry : usuariosOrdenados) {
                        String idUsuario = entry.getKey();
                        String nombreUsuario = usuariosNombres.get(idUsuario);
                        int librosPorDevolver = entry.getValue();
                        System.out.println("Usuario ID: " + idUsuario + " - Nombre: " + nombreUsuario + " - Libros por devolver: " + librosPorDevolver);
                    }
                } else {
                    System.out.println("No hay libros por devolver en este momento.");
                }
            }

        } catch (IOException e) {
            System.err.println("Error al obtener usuarios con libros por devolver: " + e.getMessage());
        }
    }
	
	
    
    
    
    
    
    
    public static void mostrarMenuPrincipal() {
        System.out.println("Menú Principal");
        System.out.println("1. Gestión de Usuarios");
        System.out.println("2. Gestión de Libros/Revistas");
        System.out.println("3. Préstamos");
        System.out.println("4. Devoluciones");
        System.out.println("5. Disponibilidad de libros");
        System.out.println("6. Generación de Informes");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");

    }

    public static void mostrarMenuGestionUsuarios() {
        System.out.println("Menú de Gestión de Usuarios");
        System.out.println("1. Alta de Usuario");
        System.out.println("2. Baja de Usuario");
        System.out.println("3. Modificación de Usuario");
        System.out.println("0. Volver al Menú Principal");
        System.out.print("Seleccione una opción: ");
    }

    public static void mostrarMenuGestionLibros() {
        System.out.println("Menú de Gestión de Libros/Revistas");
        System.out.println("1. Alta de Libro/Revista");
        System.out.println("2. Baja de Libro/Revista");
        System.out.println("3. Modificación de Libro/Revista");
        System.out.println("0. Volver al Menú Principal");
        System.out.print("Seleccione una opción: ");
    }

    public static void mostrarMenuPrestamos() {
        System.out.println("Menú de Préstamos y Devoluciones");
        System.out.println("1. Realizar Préstamo");
        System.out.println("2. Realizar Devolución");
        System.out.println("0. Volver al Menú Principal");
        System.out.print("Seleccione una opción: ");
    }
    
    public static void GestionInformes() {
        System.out.println("Menú de Gestión de informes");
        System.out.println("1. Libros mas prestados");
        System.out.println("2. Cantidad de libros de la biblioteca");
        System.out.println("3. Usuarios con mas libros a devolver");
        System.out.println("0. Volver al Menú Principal");
        System.out.print("Seleccione una opción: ");
    }
    
}
