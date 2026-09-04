import java.util.Scanner; 
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class arch_imp{
	public static void main(String[] args){
		int cont=0;
		File arch = new File("archivo.txt");

		try(Scanner leer = new Scanner(arch);
		
			FileWriter copia = new FileWriter("copia.txt")){

		    	while(leer.hasNextLine()) {
		      		String data = leer.nextLine();
		      		cont++;
			      	if(cont%2!=0){	
			        	copia.write(data+System.lineSeparator());
			      	}
		     	}

	    }catch(FileNotFoundException e){
	    	System.out.println("Error.");
	    	e.printStackTrace();
	    }catch(IOException e){
			System.out.println("Error al escribir.");
			e.printStackTrace();
		}

		System.out.println("Se copiaron únicamente las líneas impares en el archivo copia.txt");
	}
}