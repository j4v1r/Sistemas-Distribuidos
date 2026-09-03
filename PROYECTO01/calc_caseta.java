import java.util.Scanner; 

public class calc_caseta{
	public static void main(String[] args){
		int ejes, monto;
		Scanner teclado =  new Scanner(System.in);

		System.out.println("Ingresa el número de ejes: ");
		ejes = teclado.nextInt();

		switch(ejes){
			case 1:
				monto=20;
				break;
			case 2:
				monto=40;
				break;
			case 3:
				monto=60;
				break;
			default:
				monto=250;
				if(ejes>6){
					monto+=50*(ejes-6);
				}
				break;
		}

		System.out.println("El monto a cobrar es: $"+monto);
	}
}