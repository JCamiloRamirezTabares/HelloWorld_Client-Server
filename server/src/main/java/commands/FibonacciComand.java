package commands;

import AppInterfaces.RequesterPrx;

public class FibonacciComand extends Command{

    private Number number;

    public FibonacciComand(String s){
        try {
            number = new Number(s);
        } catch (Exception e) {
            setOutput("Error processing input: " + e.getMessage());
        }
    }
    
    @Override
    protected void executeCommand(String s, RequesterPrx proxy) {

        String out = "";

        if (number.isPositiveInteger()) {
            out = fibonacci(number.getNumber()) + "\n";
            out += number.primeFactors();
        } else {
            out = "Negative Number hasn't prime factors";
        }

        setOutput(out);
    }

    public long fibonacci(long num){
        if (num <= 1) {
            return num;
        }
        return fibonacci(num - 1) + fibonacci(num - 2);
    }

    
}

class Number {
    private long num;
    public Number(String n){
        num = Long.parseLong(n);
    }

    public long getNumber(){
        return num;
    }

    public String primeFactors() {
        String primeFacts = "";

        if (num == 0 || num == 1) {
            primeFacts = num + " hasn't prime factors";
        } else {
            int cPrime = 2;
            primeFacts += "Prime factors of ("+num+") ==> ";

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
