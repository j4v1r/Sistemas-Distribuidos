public class sum_cub_dig{
    public static void main(String[] args){

        for(int numero=1;numero<=5000;numero++){

            int aux=numero;
            int suma=0;

            while(aux>0){
                int digito=aux%10;
                suma+=digito*digito*digito;
                aux/=10;
            }

            if(suma==numero){
                System.out.println(numero);
            }
        }
    }
}