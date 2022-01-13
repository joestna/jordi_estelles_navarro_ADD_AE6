package es.florida.mongo;

import java.util.Scanner;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Test 
{	
	static boolean continuar = true;

	public static void main(String[] args) throws InterruptedException
	{
		Scanner sc = new Scanner(System.in);
		
		while( continuar ) {
			// Conexion a base de datos MongoDB
			MongoClient mongoClient = new MongoClient("localhost", 27017);
			MongoDatabase database = mongoClient.getDatabase("bibliotecaMongo");
			MongoCollection<Document> coleccion = database.getCollection("libro");
			
			Thread.sleep(1000);
			
			App app = new App();
			app.CrearUnLibro(coleccion, sc);
			
			mongoClient.close();
			
		}

		System.out.println(" >> FIN << ");
	}

}
