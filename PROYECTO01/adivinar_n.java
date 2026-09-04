import java.util.Scanner; 
import java.util.Random;

public class adivinar_n{
	public static void main(String[] args){
		int x;
		Scanner teclado = new Scanner(System.in);

		Random ran = new Random();
		int n = ran.nextInt(100)+1;

		do{
			System.out.println("Ingresa el número inicial (x): ");
			x = teclado.nextInt();
			if(x==0){
				System.out.println("Saliendo...");
				break;
			}else if(x>n){
				System.out.println("El número ingresado es mayor");
			}else if(x<n){
				System.out.println("El número ingresado es menor");
			}else{
				System.out.println("Adivinaste el número");
			}
		}while(n!=x);
	}
}