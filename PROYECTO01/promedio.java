import java.util.Scanner;

public class promedio{
	public static void main(String[] args){
		int n=0;
		double x,sum=0;
		double prom;
		Scanner teclado = new Scanner(System.in);

		while(true){
			System.out.println("Ingresa el número: ");
			x = teclado.nextInt();
			
			if(x==0){
				break;
			}else{
				sum+=x;
				n++;
			}
		}

		prom = sum/n;
		System.out.println("El promedio de los números ingresados es: "+prom);
	}
}