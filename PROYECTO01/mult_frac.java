import java.util.Scanner; 

public class mult_frac{
	public static void main(String[] args){
		int a,b,c,d,num,div;
		double result;
		Scanner teclado =  new Scanner(System.in);

		System.out.println("Ingresa a: ");
		a = teclado.nextInt();

		System.out.println("Ingresa b: ");
		b = teclado.nextInt();

		System.out.println("Ingresa c: ");
		c = teclado.nextInt();
		
		System.out.println("Ingresa d: ");
		d = teclado.nextInt();

		num = a*c;
		div = b*d;
		result = ((double)num)/((double)div);

		System.out.println("El resultado de la multiplicación es: "+num+"/"+div+" ("+result+")");
	}
}