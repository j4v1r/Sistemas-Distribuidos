import java.util.Scanner; 

public class serie_xy{
	public static void main(String[] args){
		int x, y, i;
		Scanner teclado = new Scanner(System.in);

		System.out.println("Ingresa el número inicial (x): ");
		x = teclado.nextInt();

		System.out.println("Ingresa el número de incremento(y): ");
		y = teclado.nextInt();

		while(x<200){
			x+=y;
			if(x<=200){
				System.out.println(x);
			}
		}
	}
}