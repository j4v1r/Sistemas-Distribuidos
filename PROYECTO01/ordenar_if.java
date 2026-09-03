import java.util.Scanner; 

public class ordenar_if{
	public static void main(String[] args){
		double a,b,c;
		Scanner teclado =  new Scanner(System.in);

		System.out.println("Ingresa el primer número: ");
		a = teclado.nextDouble();

		System.out.println("Ingresa el segundo número: ");
		b = teclado.nextDouble();

		System.out.println("Ingresa el tercer número: ");
		c = teclado.nextDouble();

		System.out.println("Los números ordenados de mayor a menor son: ");
		
		if(a>b && a>c){
			System.out.println(a);
			if(b>c){
				System.out.println(b);
				System.out.println(c);
			}else{
				System.out.println(c);
				System.out.println(b);
			}
		}else if(b>a && b>c){
			System.out.println(b);
			if(a>c){
				System.out.println(a);
				System.out.println(c);
			}else{
				System.out.println(c);
				System.out.println(a);
			}
		}else{
			System.out.println(c);
			if(b>a){
				System.out.println(b);
				System.out.println(a);
			}else{
				System.out.println(a);
				System.out.println(b);
			}
		}
	}
}