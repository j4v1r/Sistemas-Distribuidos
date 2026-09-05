public class nums_perf{
    public static void main(String[] args){

        for(int numero=1;numero<=10000;numero++){

            int suma = 0;

            for(int divisor=1;divisor<numero;divisor++){
                if(numero%divisor==0){
                	suma+=divisor;
                }
            }

            if(suma==numero){
                System.out.println(numero);
            }
        }
    }
}