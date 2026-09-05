import java.util.Scanner; 

public class desc{
	public static void main(String[] args){
		int aux;
		boolean esMenor=true;
		double[] nums = new double[10];
		Scanner teclado = new Scanner(System.in);

		for(int i=0;i<10;i++){
			System.out.println("Ingresa el número: ");
			nums[i]=teclado.nextDouble();
		}

		for(int i=1;i<10;i++){
			if(nums[i]>nums[i-1]){
				esMenor=false;
				break;
			}
		}

		if(esMenor){
			System.out.println("Los números están ordenados de forma descendente");
		}else{
			System.out.println("Los números NO están ordenados de forma descendente");
		}
	}
}