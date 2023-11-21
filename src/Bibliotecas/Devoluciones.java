package Bibliotecas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Devoluciones {
	private int id_libro;
	private int id_cliente;
    private String fecha;
    private String ARCHIVO_PRESTADOS = "prestado.txt";
    private String ARCHIVO_LIBROS = "libros.txt";
    private String ArchivoUsuario = "Usuarios.txt";


    public Devoluciones(int id_cliente, int id_libro, String fecha) {
        this.id_cliente = id_cliente;
        this.id_libro = id_libro;
        this.fecha = fecha;
    }


    public int getIdLibro() {
    	return id_libro;
    }
    
    public void setIdLibro(int id_libro) {
    	this.id_libro = id_libro;
    }
    public String getFecha() {
        return fecha;
    }
    public int getId() {
        return id_cliente;
    }
    public void setIdCliente(int id_cliente){
    	this.id_cliente = id_cliente;
    }
    public void setFecha(String fecha) {
    	this.fecha = fecha;
    }

}
