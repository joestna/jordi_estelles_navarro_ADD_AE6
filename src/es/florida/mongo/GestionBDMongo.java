package es.florida.mongo;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.ArrayList;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;


public class GestionBDMongo 
{
	// Analiza el fichero CSV, transforma sus elementos en objetos Libro y los almacena en una lista almacenLibros
	public ArrayList<Libro> AnalizarCSV( String rutaFichero )
	{
		ArrayList<Libro> librosContenidosEnCSV = new ArrayList<Libro>();

		try
		{
//			PATH DE FICHERO CSV
//			/home/jordi/proyectosJavaEclipse/jordi_estelles_navarro_AE6_ADD/datos/AE06_T3_1_MongoDB_Datos.csv			
			FileReader fr = new FileReader( rutaFichero );
			
			BufferedReader br = new BufferedReader( fr );
			String linea = br.readLine();
			
			int evitarTitulos = 0;
			
			while( linea != null ){
				evitarTitulos ++; // Evita que genere un libro con los titulos de los atributos ( 1ra linea = titulo, editorial... )
				
				if( evitarTitulos > 1 ) {				
					String[] datosLibro = linea.split( ";" );
					
					for( int i = 0; i < datosLibro.length; i++ ){
						if( datosLibro[ i ].equals( "" ) ){
							datosLibro[ i ] = "NC";
						}
					}
					
					Libro nuevoLibro = new Libro( datosLibro[1], datosLibro[2], datosLibro[3], Integer.valueOf( datosLibro[4] ), datosLibro[5], Integer.valueOf(datosLibro[6]), Integer.valueOf(datosLibro[0]));
					librosContenidosEnCSV.add( nuevoLibro );
				}
				
				linea = br.readLine();
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();			
		}
		
		return librosContenidosEnCSV;
	}
	
	
	
	// Utiliza la informacion de la lista almacenLibros y la exporta a la base de datos
	public void MigracionABDMySQL(ArrayList<Libro> almacenLibros, MongoCollection<Document> coleccion, MongoClient mongoClient) throws ClassNotFoundException
	{		
		// Click derecho en proyecto > build path > configure build path > libraries > module path > anyadir libreria externa > archivo .jar
		//mongo-java-driver-3.12.10.jar
		//json- .jar
		
		try 
		{				
			for( Libro libro : almacenLibros ){
				Document doc = new Document();
				
				doc.append("titulo", libro.getTitulo());
				doc.append("autor", libro.getAutor());
				doc.append("anyo_nacimiento", libro.getAnyoNacimiento());
				doc.append("anyo_publicacion", libro.getAnyoPublicacion());
				doc.append("editorial", libro.getEditorial());
				doc.append("paginas", libro.getNumeroPaginas());
				doc.append("_id", libro.getId());
				
				coleccion.insertOne(doc);
			}
			
			mongoClient.close();
			
		}
		catch( Exception e )
		{
			System.out.println( ">> Error en la conexion. " );
			e.printStackTrace();			
		}	
	}
}
