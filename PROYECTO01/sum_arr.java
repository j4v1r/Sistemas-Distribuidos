import java.util.Scanner; 

public class sum_arr{
	public static void main(String[] args){
		double sum=0;
		double[][] mtrx = new double[5][5];
		Scanner teclado = new Scanner(System.in);

		for(int i=0;i<5;i++){
			for(int j=0;j<5;j++){
				System.out.println("Ingrese el valor ["+i+"]["+j+"] de la matriz: ");
				mtrx[i][j]=teclado.nextDouble();
				sum+=mtrx[i][j];
			}
		}

		for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                System.out.print("["+mtrx[i][j]+"]");
            }
            System.out.println();
        }

        System.out.println("\nLa suma total del arreglo es: "+sum);
	}
}