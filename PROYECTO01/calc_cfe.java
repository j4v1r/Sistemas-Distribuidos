import java.util.Scanner; 

public class calc_cfe{
	public static void main(String[] args){
		int kW,rango;
		String tipo;
		double monto;
		Scanner teclado =  new Scanner(System.in);

		System.out.println("Ingresa el tipo de inmueble (Hogar/Negocio): ");
		tipo = teclado.nextLine();

		System.out.println("Ingresa la cantidad de kW consumidos: ");
		kW = teclado.nextInt();

		if(tipo.equalsIgnoreCase("Hogar")){
			if(kW<=250){
				monto=kW*0.65;
			}else{
				monto=250*0.65;
				kW-=250;
				//+250
				if(kW<=250){
					monto+=kW*0.85;
				}else{
					kW-=250;
					monto+=250*0.85;
					//+500
					if(kW<=700){
						monto+=kW*1.5;
					}else{
						kW-=700;
						monto+=700*1.5;
						//+1200
						if(kW<=900){
							monto+=kW*2.5;
						}else{
							kW-=900;
							monto+=900*2.5;
							//+2100
							monto+=kW*3;
						}
					}
				}
			}
		}else{
			monto=kW*5;
		}

		System.out.println("El total a pagar es: $"+monto);
	}
}