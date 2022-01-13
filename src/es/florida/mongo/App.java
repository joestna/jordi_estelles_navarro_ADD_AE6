package es.florida.mongo;

import java.io.Serializable;
import java.util.Scanner;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;

public class App {
	
	static boolean continuar = true;

	// Realiza la conexion a la base de datos utilizando hibernate y asegura las query
	public static void main(String[] args) throws InterruptedException
	{
		Scanner sc = new Scanner(System.in);
		
		while( continuar ) {
			// Conexion a base de datos MongoDB
			MongoClient mongoClient = new MongoClient("localhost", 27017);
			MongoDatabase database = mongoClient.getDatabase("bibliotecaMongo");
			MongoCollection<Document> coleccion = database.getCollection("libro");
			
			Thread.sleep(1000);
			
			//Controlador(coleccion, sc);
			
			mongoClient.close();
			
		}

		System.out.println(" >> FIN << ");
	}
	
	/*
	// Controlador de la app, gestiona el control de la conexion a la base de datos
	static void Controlador( Session session, Scanner sc )
	{	
		boolean appContinua = true;

		System.out.println( "\n> QUE DESEA HACER? <" );
		System.out.println( "1 = Mostrar todos los libros" );
		System.out.println( "2 = Mostrar un libro" );
		System.out.println( "3 = Anyadir libro a biblioteca" );
		System.out.println( "4 = Modificar atributos de un libro" );
		System.out.println( "5 = Borrar un libro" );
		System.out.println( "6 = Cerrar APP" );
		
		System.out.print("> ");
		String opcion = sc.next();
		
		
		switch( opcion )
		{
			// Mostrar todos los libros
			case "1":
				try
				{
					List<Libro> biblioteca = new ArrayList(); //Diferencia entre list y arraylist??
					biblioteca = session.createQuery("FROM Libro").list();
					
					System.out.println("");
					for( Libro libro : biblioteca )
					{
						System.out.println( libro.toString() );	
					}
					
					System.out.println(">> Libros mostrados correctamente de la Base de Datos\n");
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
					System.out.print("\nIntroduce el ID del libro : ");
					int id = sc.nextInt();
					
					Libro libro = (Libro) session.get(Libro.class, id);
					
					System.out.println( libro.toString() );						
					System.out.println(">> Libro mostrado correctamente de la Base de Datos\n");
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
					Libro libro = CrearUnLibro( sc );
					Serializable id = session.save( libro ); // devuelve el id del objeto que ha creado en la base de datos
					
					System.out.println(">> ID : " + id + " | 1Libro anyadido correctamente de la Base de Datos\n");
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
					System.out.print("\nIntroduce el ID del libro : ");
					int id = sc.nextInt();
					
					Libro libroModificar = (Libro) session.get(Libro.class, id); // almacena la informacion de un libro de la base de datos en el libro
					Libro libroModificado = CrearUnLibro( sc );// Crea un nuevo libro que reescribe al libro en bd
					session.update(libroModificado);
					
					System.out.println(">> Libro actualizado correctamente de la Base de Datos\n");
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
					System.out.print("\nIntroduce el ID del libro : ");
					int id = sc.nextInt();
					
					Libro libro = (Libro) session.get(Libro.class, id);
					session.delete(libro);
					
					System.out.println(">> Libro eliminado correctamente de la Base de Datos\n");
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
				
				break;
			
			// Cerrar la app
			case "6" :
				continuar = false;
				
				break;
				
			default :
				System.out.println(">> Valor introducido incorrecto!\n");
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
	*/
	
	
	// Crea un libro, se utiliza para crear y anyadir libros a la base de datos pero tambien para modificar un libro ya existente
	// Devuelve un libro
	public static Document CrearLibro( Scanner sc )
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
		doc.append("titulo:", libro.getTitulo());
		doc.append("autor:", libro.getAutor());
		doc.append("anyo_nacimiento:", libro.getAnyoNacimiento());
		doc.append("anyo_publicacion:", libro.getAnyoPublicacion());
		doc.append("editorial:", libro.getEditorial());
		doc.append("paginas:", libro.getNumeroPaginas());
		
		return doc;
	}
	
	
	public void AnyadirLibro( MongoCollection<Document> coleccion, Document doc )
	{
		coleccion.insertOne(doc);
	}
	
	
	public static Bson MostrarLibro( MongoCollection<Document> coleccion, Scanner sc )
	{
		System.out.println("Introduce el titulo del libro : ");
		String titulo = sc.next();		
		Bson query;
		
		try
		{
			query = eq("titulo", titulo);			
			MongoCursor<Document> cursor = coleccion.find(query).iterator();
			
			while(cursor.hasNext()) {
				System.out.println(cursor.next().toJson());
			}
			
			return query;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return query = null;
		
		// Se puede parseal el Json para mostrar los elementos que queramos
		/*
		while(cursor.hasNext()){
			JSONObject objeto = new JSONObject(cursor.next().toJson());
			System.out.println(objeto.getString("titulo"));
		}
		*/
	}
	
	
	public static void ActualizarLibro( MongoCollection<Document> coleccion, Scanner sc )
	{
		try
		{
			Bson query = MostrarLibro(coleccion, sc);
			
			System.out.println("> Esta seguro de que quiere actualizar el libro? s \n");
			String actualizar = sc.next();
			
			if(actualizar.equals("s") || actualizar.equals("S")) {
				Document nuevoLibro;
				nuevoLibro = CrearLibro(sc);
				coleccion.updateOne(query, new Document("$set", new Document(nuevoLibro)));

				System.out.println("Libro actualizado con exito");
			}
			else {
				System.out.println("Libro no actualizado");
			}	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void EliminarLibro( MongoCollection<Document> coleccion, Scanner sc )
	{
		try 
		{
			Bson query = MostrarLibro(coleccion, sc);
			
			System.out.println("> Esta seguro de que quiere eliminar el libro? s / n\n");
			String eliminar = sc.next();
			
			if(eliminar.equals("s") || eliminar.equals("S")) {
				coleccion.deleteOne(query);		
				coleccion.drop();
				
				System.out.println("Libro eliminado con exito");
			}
			else {
				System.out.println("Libro no eliminado");
			}	
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}
}
