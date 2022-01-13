package migracionDatosBiblioteca;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class App {

	// Metodo : main
	// Parametros : String[] 
	// Funcionalidad : Consultas en bucle para ejecutar funcionalidades
	// Return : void
	public static void main(String[] args) throws ClassNotFoundException, FileNotFoundException
	{
		Scanner sc = new Scanner( System.in );
		
		Biblioteca biblioteca = new Biblioteca();
		GestionBD gestorBD = new GestionBD();
		
		boolean continuidadAPP = true;
		
		while( continuidadAPP )
		{
			System.out.println( "\nQue desea hacer? : " );
			System.out.println( "Migrar datos de un CSV --> 1" );
			System.out.println( "Consultar base de datos --> 2" );
			System.out.println( "Consultas requeridas en enunciado --> 3" );
			System.out.println( "Cerrar APP --> 4" );
			System.out.print( "> " );
			String respuesta = sc.next();
			System.out.print( "\n" );
			
			switch( respuesta ) 
			{
				case "1" :
					System.out.println( "Introduce la ruta del fichero CSV : " );
					String rutaFichero = sc.next();

					biblioteca.setAlmacenLibros( gestorBD.AnalizarCSV( rutaFichero ) );				
					gestorBD.MigracionABDMySQL( biblioteca.getAlmacenLibros() );
					break;
					
				case "2" :					
					String consulta = gestorBD.GenerarConsulta( sc );					
					gestorBD.ConsultarBD( consulta );
					break;
					
				case "3" :
					gestorBD.ConsultasRequeridas();
					break;
					
				case "4" :
					System.out.println( ">> Nos vemos pronto ;)" );
					continuidadAPP = false;
					break;						
					
				default :
					System.out.println( ">> Valor introducido no valido." );
					break;				
			}
		}
	}
}
