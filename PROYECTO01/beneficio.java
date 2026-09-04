import java.util.Scanner; 

public class beneficio{
	public static void main(String[] args){
		double ahorro=0;

		for(int i=1;i<=20;i++){
			ahorro +=10000;
			ahorro+=ahorro*0.05;
		}

		System.out.println("El ahorro toal después de 20 años es: "+ahorro);
	}
}