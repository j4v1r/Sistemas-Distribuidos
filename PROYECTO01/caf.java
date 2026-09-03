import java.util.Scanner; 

public class caf{
	public static void main(String[] args){
		double celcius;
		double farenheit;
		Scanner teclado =  new Scanner(System.in);

		System.out.println("Ingresa los grados en C°: ");
		String input = teclado.nextLine();

		celcius = Float.parseFloat(input);
		farenheit = (celcius*1.8)+32.0;
		
		System.out.println("El equivalente en grados F° es: "+farenheit);
	}
}