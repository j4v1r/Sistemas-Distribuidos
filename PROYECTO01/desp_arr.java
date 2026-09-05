import java.util.Scanner; 

public class desp_arr{
	public static void main(String[] args){
		double[] nums = new double[10];
		Scanner teclado = new Scanner(System.in);

		for(int i=0;i<10;i++){
			System.out.println("Ingresa el número: ");
			nums[i]=teclado.nextDouble();
		}

		double aux=nums[9];
		for(int i=0;i<=9;i++){
			double x=nums[i];
			nums[i]=aux;
			aux=x;
		}

		System.out.println("Desplazamiento:");
		for(int i=0;i<10;i++){
			System.out.println(nums[i]);
		}
	}
}
//0123456789
//1234567890
//0123456789