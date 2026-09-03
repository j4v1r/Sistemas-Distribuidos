import java.util.Scanner; 

public class asig_edificio{
	public static void main(String[] args){
		int edad;
		String s,edificio;
		Scanner teclado =  new Scanner(System.in);

		System.out.println("Ingresa el sexo(H/M): ");
		s = teclado.nextLine();

		System.out.println("Ingresa la edad: ");
		edad = teclado.nextInt();

		if(s.equalsIgnoreCase(String.valueOf('H'))){
			if(edad==18){
				edificio="A";
			}else if(edad>18 && edad<23){
				edificio="C";
			}else{
				edificio="E1";
			}
		}else{
			if(edad==18){
				edificio="B";
			}else if(edad>18 && edad<23){
				edificio="D";
			}else{
				edificio="E2";
			}
		}

		System.out.println("El edificio asignado es: "+edificio);
	}
}