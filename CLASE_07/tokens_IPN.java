import java.util.Random;

public class tokens_IPN{
	public static void main(String[] args){
		int n = Integer.parseInt(args[0]);
		int contador=0;
		char[]cadenota = new char[n*4+1];
		Random rand = new Random();

		for(int i=1;i<n*4;i++){
			if(i%4==0){
				cadenota[i]=' ';
			}else{
				int random_char=rand.nextInt(26)+65;
				cadenota[i]=(char)random_char;	
			}
		}

		for(int i=1;i<n*4;i+=4){
			if(cadenota[i]=='I' && cadenota[i+1]=='P' && cadenota[i+2]=='N'){
				contador++;
			}
		}

		System.out.println(contador);
	}
}