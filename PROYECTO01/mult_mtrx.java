import java.util.Scanner; 

public class mult_mtrx{
	public static void main(String[] args){
		int fa,ca;
		int fb,cb;
		Scanner teclado = new Scanner(System.in);

		System.out.println("Ingresa el número de filas de la matriz A: ");
		fa = teclado.nextInt();

		System.out.println("Ingresa el número de columnas de la matriz A: ");
		ca = teclado.nextInt();

		System.out.println("Ingresa el número de filas de la matriz B: ");
		fb = teclado.nextInt();

		System.out.println("Ingresa el número de columnas de la matriz B: ");
		cb = teclado.nextInt();

		double[][] mA = new double[fa][ca];
		double[][] mB = new double[fb][cb];
		double[][] mR = new double[fa][cb];

		if(ca==fb){

			for(int i=0;i<fa;i++){
				for(int j=0;j<ca;j++){
					System.out.println("Ingrese el valor ["+i+"]["+j+"] de la matriz A: ");
					mA[i][j]=teclado.nextDouble();
				}
			}

			for(int i=0;i<fb;i++){
				for(int j=0;j<cb;j++){
					System.out.println("Ingrese el valor ["+i+"]["+j+"] de la matriz B: ");
					mB[i][j]=teclado.nextDouble();
				}
			}

			for(int i=0;i<fa;i++){
				for(int j=0;j<cb;j++){
					double sum=0; 
					for(int aux=0;aux<ca;aux++){
						sum+=mA[i][aux]*mB[aux][j];
					}
					mR[i][j] = sum;
				}
			}

		}else{
			System.out.println("El número de columnas de la matriz A debe ser igual al número de filas de la matriz B");
		}

		for(int i = 0; i < fa; i++){
		    for(int j = 0; j < cb; j++){
		        System.out.print("[" + mR[i][j] + "] ");
		    }
		    System.out.println();
		}
	}
}