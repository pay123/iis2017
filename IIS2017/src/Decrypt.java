import java.math.BigDecimal;
import java.math.BigInteger;

import static java.lang.Math.pow;

public class Decrypt
{
    public static final BigInteger E = BigInteger.valueOf(65537);
    public static final BigInteger N = new BigInteger("581696833723949335177");
    public static final String CIPHERTEXT = "GVHIUXNYQXWUGB";

	private static BigInteger encode(String s)
    {
        char[] cipher = s.toCharArray();
        StringBuilder m = new StringBuilder();

        BigInteger b = BigInteger.ZERO;

        int i = 0;
        for (char u : cipher)
        {
            if (u >= 'A' && u <= 'Z')
            {
                m.append(new BigDecimal((u - 'A') * pow(26, i)).toBigInteger());
                b = b.add(BigInteger.valueOf(u - 'A'));
            }
            i++;
        }
        return new BigInteger(m.toString());
    }

    private static String decode(BigInteger s)
    {
        StringBuilder m = new StringBuilder();
        BigInteger f = BigInteger.valueOf(26);
        Integer k = 0;

        while (s.divide(f.pow(k)).compareTo(BigInteger.ONE) >= 0)
        {
//            System.out.println(f.pow(k) + " < " + s);
            k++;
        }
        k--;

        System.out.println("k: " + k);


        while (k >= 0)
        {
            BigInteger letter = BigInteger.ZERO;

            while (letter.multiply(f.pow(k)).compareTo(s) < 1)
            {
                BigInteger tmp = letter.multiply(f.pow(k));

                letter = letter.add(BigInteger.ONE);

//                if (letter.compareTo(BigInteger.valueOf(25)) == 0)
//                {
//                    break;
//                }
            }
            letter = letter.subtract(BigInteger.ONE);

            m.append((char)(letter.intValue() + 'A'));

            s = s.subtract(letter.multiply(f.pow(k)));
            k--;
        }

        return m.toString();
    }

    private static BigInteger factor(BigInteger number)
    {
        BigInteger xFixed = BigInteger.valueOf(2);
        BigInteger x = BigInteger.valueOf(2);
        BigInteger cycleSize = BigInteger.valueOf(2);
        BigInteger factor = BigInteger.ONE;
        BigInteger count;

        while (factor.equals(BigInteger.ONE))
        {
            count = BigInteger.ONE;
            while(count.compareTo(cycleSize) < 1 && factor.compareTo(BigInteger.ONE) < 1)
            {
                x = (x.multiply(x.add(BigInteger.ONE))).mod(number);
                factor = number.gcd(x.subtract(xFixed));
                count = count.add(BigInteger.ONE);
            }

            cycleSize = cycleSize.multiply(BigInteger.valueOf(2));
            xFixed = x;
        }
        return factor;
    }

    public static void main(String[] args)
    {
        BigInteger c = encode(CIPHERTEXT);

        BigInteger p = factor(N);
        System.out.println("p: " + p);
        BigInteger q = N.divide(p);
        System.out.println("q: " + q);

        BigInteger phi_n = (p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)));

        BigInteger d = E.modInverse(phi_n);

        System.out.println("d: " + d);

        BigInteger m = c.modPow(d, N);
        System.out.println("m: " + m);

        System.out.println("Decoding " + m + " ... " + decode(m));

//        System.out.println("Decoding " + 6921 + " ... " + decode(BigInteger.valueOf(6921)));

//        BigInteger k = BigInteger.valueOf(CIPHERTEXT.length()-1);
//
//        System.out.println(m.modPow(BigInteger.ONE.add(phi_n.multiply(k).mod(phi_n)), N));
    }
}
