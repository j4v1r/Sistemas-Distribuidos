import java.util.Scanner; 

public class arr_inv{
	public static void main(String[] args){
		int[] arr = new int[10];
		Scanner teclado = new Scanner(System.in);

		for(int i=9;i>=0;i--){
			System.out.println("Ingresa un número entero: ");
			int num = teclado.nextInt();

			arr[i]=num;
		}

		System.out.println("Inversión: ");
		for(int j=0;j<=9;j++){
			System.out.println(arr[j]);
		}
	}
}