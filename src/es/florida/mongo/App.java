package es.florida.mongo;

import java.io.Serializable;
import java.util.Scanner;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;

import static com.mongodb.client.model.Filters.*;

public class App {
	
	static boolean continuar = true;
	static boolean contieneElementos = false;
	static List<JSONObject> json = new ArrayList<JSONObject>();

	// Realiza la conexion a la base de datos utilizando hibernate y asegura las query
	public static void main(String[] args) throws InterruptedException
	{
		while( continuar ) {
			Scanner sc = new Scanner(System.in).useDelimiter("\n");
			
			// Conexion a base de datos MongoDB
			MongoClient mongoClient = new MongoClient("localhost", 27017);
			MongoDatabase database = mongoClient.getDatabase("bibliotecaMongo");
			MongoCollection<Document> coleccion = database.getCollection("libro");
			
			Thread.sleep(2000);
			
			Controlador(coleccion, mongoClient, sc);
			mongoClient.close();
			contieneElementos = false;
						
			System.out.println("");			
			Thread.sleep(2000);
		}

		System.out.println(" >> Gracias por su tiempo << ");
	}
	
	
	// Controlador de la app, gestiona el control de la conexion a la base de datos
	static void Controlador( MongoCollection<Document> coleccion, MongoClient mongoClient, Scanner sc)
	{	
		System.out.println( "\n> QUE DESEA HACER? <" );
		System.out.println( "1 = Mostrar todos los libros" );
		System.out.println( "2 = Mostrar un libro" );
		System.out.println( "3 = Anyadir libro a biblioteca" );
		System.out.println( "4 = Modificar atributos de un libro" );
		System.out.println( "5 = Borrar un libro" );
		System.out.println( "6 = Importar libros del CSV a la BD" );
		System.out.println( "7 = Cerrar APP" );
		
		System.out.print("> ");
		String opcion = sc.next();
		
		
		switch( opcion )
		{
			// Mostrar todos los libros
			case "1":
				try
				{
					MostrarTodosLibros(coleccion);
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
				
				break;
				
			// Mostrar un libro
			case "2":
				try
				{
					MostrarLibro(coleccion, sc);
				}				
				catch( Exception e )
				{
					e.printStackTrace();
				}
				
				break;
			
			// Anyadir libro a biblioteca
			case "3":
				try
				{
					AnyadirLibro(coleccion, sc);
				}				
				catch( Exception e )
				{
					e.printStackTrace();
				}
				
				break;
		
			// Modificar atributos de un libro
			case "4":
				try
				{
					ActualizarLibro(coleccion, sc);
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
				
				break;
				
			// Borrar un libro
			case "5":
				try
				{
					EliminarLibro(coleccion, sc);
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
				
				break;
			
			// Importar libros del CSV a la BD
			case "6":
				try
				{
					String rutaficheroCSV = "/home/jordi/Documents/eclipse-workspace/jordi_estelles_navarro_AE6_ADD/datos/AE06_T3_1_MongoDB_Datos.csv";
					
					GestionBDMongo gestor = new GestionBDMongo();
					gestor.MigracionABDMySQL(gestor.AnalizarCSV(rutaficheroCSV), coleccion, mongoClient);
				}
				catch( Exception e)
				{
					e.printStackTrace();
				}
				break;
				
			// Cerrar la app
			case "7" :
				continuar = false;
				
				break;
				
			default :
				System.out.println(">> Valor introducido incorrecto!\n");
				
				break;
		}
		
		// Pausa la app 2 segundos para que si se lanza alguna exception en la seleccion anterior no rompa el menu
		try
		{
			Thread.sleep(2000);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	
	
	// Pide al usuario que indique los datos del libro a registrar
	// Devuelve el libro como Document
	static Document CrearLibro( Scanner sc )
	{		
		Document doc = new Document();
		
		System.out.print( "\nIntroduce el titulo del libro : " );
		String titulo = sc.next();
		
		System.out.print( "Introduce el autor del libro : " );
		String autor = sc.next();
		
		System.out.print( "Introduce el anyo de nacimiento del autor del libro : " );
		String anyoNacimiento = sc.next();
		
		System.out.print( "Introduce el anyo de publicacion del libro : " );
		int anyoPublicacion = sc.nextInt();
		
		System.out.print( "Introduce la editorial del libro : " );
		String editorial = sc.next();
		
		System.out.print( "Introduce el numero de paginas del libro : " );
		int numPaginas = sc.nextInt();
		
		Libro libro = new Libro( titulo, autor, anyoNacimiento, anyoPublicacion, editorial, numPaginas );				
		doc.append("titulo", libro.getTitulo());
		doc.append("autor", libro.getAutor());
		doc.append("anyo_nacimiento", libro.getAnyoNacimiento());
		doc.append("anyo_publicacion", libro.getAnyoPublicacion());
		doc.append("editorial", libro.getEditorial());
		doc.append("paginas", libro.getNumeroPaginas());
		
		return doc;
	}
	
	
	// Anyade un libro previamente creado a la base de datos
	public static void AnyadirLibro( MongoCollection<Document> coleccion, Scanner sc )
	{
		Document doc = CrearLibro(sc);
		
		coleccion.insertOne(doc);
	}
	
	
	// Muestra todos los libros que contiene la base de datos
	public static void MostrarTodosLibros( MongoCollection<Document> coleccion) 
	{
		Bson projectionFields = Projections.fields(Projections.include("_id", "titulo"));
				
		try
		{
			MongoCursor<Document> cursor = coleccion.find().projection(projectionFields).iterator();
			
			
			while(cursor.hasNext()) {
				contieneElementos = true;
				System.out.println(cursor.next().toJson());
				//database.libro;
				
				//coleccion.find({"_id" : "$id", "titulo" : "$titulo"});
			}
			
			if(!contieneElementos) {
				System.out.println("No se encuentran libros.");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	// Introduciendo un titulo muestra los libros con ese mismo titulo que contiene la base de datos
	public static Bson MostrarLibro( MongoCollection<Document> coleccion, Scanner sc )
	{
		System.out.print("\nIntroduce el id del libro : ");
		String id = sc.next();
		//System.out.print("\nIntroduce el titulo del libro : ");
		//String titulo = sc.next();
		
		Bson query;		
		try
		{
			query = eq("_id", Integer.valueOf(id));	
			//query = eq("titulo", titulo);		
			MongoCursor<Document> cursor = coleccion.find(query).iterator(); // Verificar que es lo que mostraria cursor si no existe el elemento buscado
			
			while(cursor.hasNext()) {
				contieneElementos = true;				
				System.out.println(cursor.next().toJson());
				System.out.println("entra");
				// Se puede parseal el Json para mostrar los elementos que queramos, sustituir la linea anterior por :
				//JSONObject objeto = new JSONObject(cursor.next().toJson());
				//System.out.println(objeto.getString("titulo"));
			}
			
			if(!contieneElementos) {
				System.out.println("El libro buscado no existe");
			}
			
			return query;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return query = null;
	}
	
	
	// Pide al usuario que indique el libro que quiere modificar, lo muestra y pregunta al usuario si quiere editarlo
	// Actualiza el libro anterior con los nuevos datos
	public static void ActualizarLibro( MongoCollection<Document> coleccion, Scanner sc )
	{
		try
		{
			sc = new Scanner(System.in);
			Bson query = MostrarLibro(coleccion, sc);
		
			if(contieneElementos) {
				System.out.print("> Esta seguro de que quiere actualizar el libro? s/n : ");
				String actualizar = sc.next();
				
				if(actualizar.equals("s") && contieneElementos || actualizar.equals("S") && contieneElementos) {
					Document nuevoLibro = CrearLibro(sc);
					coleccion.updateOne(query, new Document("$set", new Document(nuevoLibro)));
					// coleccion.updateMany(query, new Document("$set", new Document(nuevoLibro))) // actualiza todos los elementos que coincidan con la query de find

					System.out.println("Libro actualizado con exito");
				}
				else {
					System.out.println("Libro no actualizado");
				}
			}			
			
			contieneElementos = false;
		}
		catch(Exception e)
		{
			contieneElementos = false;
			e.printStackTrace();
		}
	}
	
	
	// Pide al usuario que indique el titulo del libro, lo muestra y pregunta al usuario si quiere eliminarlo
	// Elimina el libro seleccionado
	public static void EliminarLibro( MongoCollection<Document> coleccion, Scanner sc )
	{
		try 
		{
			Bson query = MostrarLibro(coleccion, sc);
			
			if(contieneElementos) {
				System.out.print("> Esta seguro de que quiere eliminar el libro? s/n : ");
				String eliminar = sc.next();
				
				if(eliminar.equals("s") && contieneElementos || eliminar.equals("S") && contieneElementos ) {
					coleccion.deleteOne(query);	
					// coleccion.deleteMany(query); // Elimina todos los elementos que coincidan con la query
					// coleccion.drop();
					
					System.out.println("Libro eliminado con exito");
				}
				else {
					System.out.println("Libro no eliminado");
				}	
			}
			
			contieneElementos = false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}
}
