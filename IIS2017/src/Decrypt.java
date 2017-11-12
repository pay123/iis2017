import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

import static java.lang.Math.pow;

// pubkey (e, n) = (65537, 581696833723949335177)
// private key d?

// https://stackoverflow.com/questions/16310871/in-rsa-encryption-how-do-i-find-d-given-p-q-e-and-c
// d = e.modInverse(p_1.multiply(q_1))

public class Decrypt
{
    public static final BigInteger E = BigInteger.valueOf(65537);
    public static final BigInteger N = new BigInteger("581696833723949335177");

	private static void buildInt(String s)
    {
        char[] cipher = s.toCharArray();
        StringBuilder m = new StringBuilder();
        StringBuilder c = new StringBuilder();

        int i = 0;
        for (char u : cipher)
        {
            if (u >= 'A' && u <= 'Z')
            {
                c.append(u - 'A');
                m.append(new BigDecimal((u - 'A') * pow(26, i)).toBigInteger());
            }
            i++;
        }

        System.out.println("c: " + c);
        System.out.println("m: " + m);
    }
	
    public static void main(String[] args)
    {
        buildInt("GVHIUXNYQXWUGB");
//        computeRSAFactors(E, N);
    }

    private static void computeRSAFactors(BigInteger e, BigInteger n)
    {
//        n = BigInteger.valueOf(10142789312725007L);
//        e = BigInteger.valueOf(8114231289041741L);
        final BigInteger d = BigInteger.valueOf(5);

        final long t0 = System.currentTimeMillis();

        final BigInteger kTheta = d.multiply(e).subtract(BigInteger.ONE);
        final int exponentOfTwo = kTheta.getLowestSetBit();

        final Random random = new Random();
        BigInteger factor = BigInteger.ONE;
        do
        {
            final BigInteger a = nextA(n, random);

            for (int i = 1; i <= exponentOfTwo; i++)
            {
                final BigInteger exponent = kTheta.shiftRight(i);
                final BigInteger power = a.modPow(exponent, n);

                final BigInteger gcd = n.gcd(power.subtract(BigInteger.ONE));
                if (!factor.equals(BigInteger.ONE))
                {
                    break;
                }
            }
        }
        while (factor.equals(BigInteger.ONE));

        final long t1 = System.currentTimeMillis();

        System.out.printf("%s %s (%dms)\n", factor, n.divide(factor), t1 - t0);
    }

    private static BigInteger nextA(final BigInteger n, final Random random)
    {
        BigInteger r;
        do
        {
            r = new BigInteger(n.bitLength(), random);
        }
        while (r.signum() == 0 || r.compareTo(n) >= 0);
        return r;
    }

}
