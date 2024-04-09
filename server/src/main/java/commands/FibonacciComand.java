package commands;

public class FibonacciComand extends Command{
    
    @Override
    public void excecuteCommand(String s) {

        String out = "";

        try {
            Number num = new Number(s);

            if (num.isPositiveInteger()) {
                out = "Fibonacci: " + fibonacci(num.getNumber()) + " And ";
                out += num.primeFactors();
            } else {
                out = "Negative Number hasn't prime factors";
            }
        } catch (Exception e) {
            out = "Error processing input: " + e.getMessage();
        }

        setOutput(out);
    }

    public Integer fibonacci(Integer num){
        if (num <= 1) {
            return num;
        }
        return fibonacci(num - 1) + fibonacci(num - 2);
    }

    
}

class Number {
    private Integer num;
    public Number(String n){
        num = Integer.parseInt(n);
    }

    public Integer getNumber(){
        return num;
    }

    public String primeFactors() {
        String primeFacts = "";

        if (num == 0 || num == 1) {
            primeFacts = num + " hasn't prime factors";
        } else {
            int cPrime = 2;
            primeFacts += "Prime factors: ";

            while (num > 1) {
                if (num % cPrime == 0) {
                    primeFacts += cPrime + ", ";
                    num = num / cPrime;
                } else {
                    cPrime = nextPrime(cPrime);
                }
            }
        }

        return primeFacts;
    }

    private Integer nextPrime(int prime) {
        for (int i = prime + 1; i <= prime * 10; i++) {
            if (isPrime(i)) {
                return i;
            }
        }
        return -1;
    }

    public Boolean isPrime(int n) {
        for (int i = 2; i < n; i++) {
            if (n % i == 0) {return false;}
        }
        return true;
    }

    public boolean isPositiveInteger(){
        return (num >= 0);
    }
}
