import java.math.BigInteger;

public class Helper {
    public BigInteger factor(BigInteger number)
    {
        BigInteger x_fixed = BigInteger.TWO;
        BigInteger x = BigInteger.TWO;
        BigInteger cycle_size = BigInteger.TWO;
        BigInteger factor = BigInteger.ONE;
        BigInteger count;

        while (factor.equals(BigInteger.ONE))
        {
            //for (int count=1;count <= cycle_size && factor <= 1;count++) {
            count = BigInteger.ONE;
            while(count.compareTo(cycle_size) < 1 && factor.compareTo(BigInteger.ONE) < 1)
            {
                x = (x.multiply(x.add(BigInteger.ONE))).mod(number);
                //factor = gcd(x - x_fixed, number);
                factor = number.gcd(x.subtract(x_fixed));
                count = count.add(BigInteger.ONE);
            }

            cycle_size = cycle_size.multiply(BigInteger.TWO);
            x_fixed = x;
        }
        return factor;
    }
}
