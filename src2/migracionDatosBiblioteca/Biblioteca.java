package migracionDatosBiblioteca;

import java.util.ArrayList;

public class Biblioteca 
{
	private ArrayList<Libro> _almacenLibros = new ArrayList<Libro>();
	
	
	public ArrayList<Libro> getAlmacenLibros()
	{
		return _almacenLibros;
	}
	
	public void setAlmacenLibros( ArrayList<Libro> almacenLibros )
	{
		_almacenLibros = almacenLibros;
	}
}
