package Bibliotecas;

public class Libro {
	private int id;
	private String titulo;
	private String autor;
	private int AñoDePublicacion;
	private String estado;




	public Libro(int id, String titulo, String autor, int AñoDePublicacion) {
	    this.id = id;
	    this.titulo = titulo;
	    this.autor = autor;
	    this.AñoDePublicacion = AñoDePublicacion;
	    this.estado = "Disponible";
	}


	public int getId() {
	    return id;
	
	}public void SetId(int id){
		this.id = id;
	}
	
	
	public String getTitulo() {
	    return titulo;
	}
	
	public void SetTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getAutor() {
	    return autor;
	}
	
	public void SetAutor(String autor) {
		this.autor = autor;
	}
	public String getEstado() {
        return estado;
    }
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
	public int getAñoDePublicacion() {
	    return AñoDePublicacion;
	}
	
	
	public void setAñoDdePublicacion(int AñoDePublicacion){
		this.AñoDePublicacion = AñoDePublicacion;
	}
	    
}
