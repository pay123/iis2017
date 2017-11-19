import java.math.BigInteger;

public class Cryptography
{
	public BigInteger encode(String s)
    {
        char[] cipher = s.toCharArray();
        BigInteger f = BigInteger.valueOf(26);

        BigInteger b = BigInteger.ZERO;

        int i = 0;
        for (char u : cipher)
        {
            if (u >= 'A' && u <= 'Z')
            {
                b = b.add(BigInteger.valueOf(u - 'A').multiply(f.pow(i)));
            }
            i++;
        }
        return b;
    }

    public String decode(BigInteger s)
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

        StringBuilder message = new StringBuilder();
        for (int i = m.toString().length()-1; i >= 0; i--)
        {
            message.append(m.toString().charAt(i));
        }

        return message.toString();
    }

    public BigInteger factor(BigInteger number)
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
}
