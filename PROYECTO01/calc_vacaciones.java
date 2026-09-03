import java.util.Scanner; 

public class calc_vacaciones{
	public static void main(String[] args){
		int ant, dias;
		Scanner teclado =  new Scanner(System.in);

		System.out.println("Ingresa el número de años de antigüedad: ");
		ant = teclado.nextInt();

		if(ant>0 && ant<6){
			dias=5;
		}else if(ant>5 && ant<11){
			dias=10;
		}else if(ant>10 && ant<20){
			dias=ant;
		}else{
			dias=ant*2;
			if(dias>45){
				dias=45;
			}
		}	

		System.out.println("El número de días de vacaciones correspondiente es: "+dias);
	}
}