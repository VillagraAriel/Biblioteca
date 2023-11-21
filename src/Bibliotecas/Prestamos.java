package Bibliotecas;

public class Prestamos {
    private int id_cliente;
    private int id_libro;
    private String fecha;
    private String archivoRegistro;

    public Prestamos(int id_cliente, int id_libro, String fecha) {
        this.id_cliente = id_cliente;
        this.id_libro = id_libro;
        this.fecha = fecha;
    }


    public int getId() {
        return id_cliente;
    }
    public void setIdCliente(int id_cliente){
    	this.id_cliente = id_cliente;
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
    
    public void setFecha(String fecha) {
    	this.fecha = fecha;
    }

      
}