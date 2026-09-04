import java.util.Scanner; 

public class capicua{
	public static void main(String[] args){
		int inv=0;
		int num, res, red;
		boolean esCap=false;
		Scanner teclado =  new Scanner(System.in);

		System.out.println("Ingresa un número entre 0 y 9999: ");
		num = teclado.nextInt();

		if(num>9999){
			System.out.println("Ingrese un número válido.");
		}else{
			res = num%10;
			inv	= (inv*10) + res;
			
			if(num==inv){
				esCap=true;
			}else{
				red = num/10;
				res = red%10;
				inv	= (inv*10) + res;
				if(num==inv){
					esCap=true;
				}else{
					red = red/10;
					res = red%10;
					inv	= (inv*10) + res;
					if(num==inv){
						esCap=true;
					}else{
						red = red/10;
						res = red%10;
						inv	= (inv*10) + res;
						if(num==inv){
							esCap=true;
						}else{
							red = red/10;
							res = red%10;
							inv	= (inv*10) + res;
							if(num==inv){
								esCap=true;
							}
						}
					}
				}
			}

			if(esCap){
				System.out.println("El número "+num+" es capicua.");
			}else{
				System.out.println("El número "+num+" NO es capicua.");
			}
		}
	}
}