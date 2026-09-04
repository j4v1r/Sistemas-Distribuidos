import java.util.Scanner; 

public class sum_for{
	public static void main(String[] args){
		int num, sum=0;
		Scanner teclado = new Scanner(System.in);

		System.out.println("Ingresa un número entero: ");
		num = teclado.nextInt();

		for(int i=1;i<=num;i++){
			sum+=i;
		}

		System.out.println("El resultado es: "+sum);
	}
}