package migracionDatosBiblioteca;

public class Libro 
{
	private String _titulo;
	private String _autor;
	private String _anyoNacimiento;
	private int _anyoPublicacion;
	private String _editorial;
	private int _numeroPaginas;
	
	public Libro( String titulo, String autor, String anyoNacimiento, int anyoPublicacion, String editorial, int numeroPaginas )
	{
		_titulo = titulo;
		_autor = autor;
		_anyoNacimiento = anyoNacimiento;
		_anyoPublicacion = anyoPublicacion;
		_editorial = editorial;
		_numeroPaginas = numeroPaginas;
	}
	
	public int getAnyoPublicacion()
	{
		return _anyoPublicacion;
	}
	
	public String getTitulo() 
	{
		return _titulo;
	}
	
	public String getAutor() 
	{
		return _autor;
	}
	
	public String getAnyoNacimiento()
	{
		return _anyoNacimiento;
	}
	
	public String getEditorial()
	{
		return _editorial;
	}
	
	public int getNumeroPaginas()
	{
		return _numeroPaginas;
	}
}
