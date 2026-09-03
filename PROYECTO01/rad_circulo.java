import java.util.Scanner; 

public class rad_circulo{
	public static void main(String[] args){
		double a,b,c,semp,area,radio;
		Scanner teclado =  new Scanner(System.in);

		System.out.println("Ingresa las longitudes de los lados del tríángulo");
		System.out.println("Ingresa a: ");
		a = teclado.nextDouble();

		System.out.println("Ingresa b: ");
		b = teclado.nextDouble();

		System.out.println("Ingresa c: ");
		c = teclado.nextDouble();

		semp = (a+b+c)/2;
		area = Math.sqrt(semp*(semp-a)*(semp-b)*(semp-c));
		
		radio = area/semp;

		System.out.println("El radio del círculo inscrito es: "+radio);
	}
}