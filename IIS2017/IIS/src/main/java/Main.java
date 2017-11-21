import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.hash.*;
import javafx.util.Pair;

import static java.lang.Math.pow;

public class Main
{
    public static final BigInteger E = BigInteger.valueOf(65537);
    public static final BigInteger N = new BigInteger("581696833723949335177");
    public static final String CIPHERTEXT = "GVHIUXNYQXWUGB";

    public static String h(String s)
    {
        HashCode referenceHash = Hashing.sha256().hashString(s, StandardCharsets.UTF_8);
//        HashCode referenceHash = Hashing.sha256().hashInt(x);
//        System.out.println(referenceHash.toString());

        byte[] hashBytes = referenceHash.asBytes();

//        System.out.println(hashBytes.length);

        byte[] upper = new byte[4];
        byte[] lower = new byte[4];

        List<byte[]> blocks = new ArrayList<>();
        for (int i = 0; i < 8; i++)
        {
            byte[] dest = new byte[4];
            for (int j = 0; j < 4; j++)
            {
                dest[j] = hashBytes[i*4+j];
            }
            blocks.add(dest);
        }

        for (int i = 0; i < 4; i++)
        {
            int b1 = (int)blocks.get(4)[i];
            int b2 = (int)blocks.get(5)[i];
            int b3 = (int)blocks.get(6)[i];
            int b4 = (int)blocks.get(7)[i];
            upper[i] = (byte)(0xff & (b1 ^ b2 ^ b3 ^ b4));
        }

        for (int i = 0; i < 4; i++)
        {
            int b1 = (int)blocks.get(0)[i];
            int b2 = (int)blocks.get(1)[i];
            int b3 = (int)blocks.get(2)[i];
            int b4 = (int)blocks.get(3)[i];
            lower[i] = (byte)(0xff & (b1 ^ b2 ^ b3 ^ b4));
        }

        byte[] bytes = new byte[upper.length + lower.length];
        System.arraycopy(upper, 0, bytes, 0, upper.length);
        System.arraycopy(lower, 0, bytes, upper.length, lower.length);

//        System.out.println(HashCode.fromBytes(bytes).toString());

//        return HashCode.fromBytes(bytes).asInt();
        return HashCode.fromBytes(upper).toString();
//        return HashCode.fromBytes(upper).asInt();
    }

//    private static boolean getFirstBit(String s)
//    {
//        int test = Integer.parseInt(s);
//        BitSet bs = new BitSet(test);
//        System.out.println(bs.get(0, 0).get(0));
//        return bs.get(0, 0).get(0);
//    }

    private static int getFirstBit(int n, String s)
    {
        String b = new BigInteger(s.getBytes()).toString(2);

//        System.out.println(b);
//        int k = Integer.parseInt(s);
//        int k = HashCode.fromBytes(s.getBytes()).asInt();
//        int bit = (n >> k) & 1;
////        System.out.println(bit);
//        return bit;
        return b.charAt(0);
    }

    private static String meetInTheMiddleAttack(String s)
    {
        List<String> S = new ArrayList<>();
        List<String> evaluated = new ArrayList<>();

        String y = h(s);
        String x0 = "111135453453123123123123123123";
        String xi = h(x0);
        String xs = null;  // x'
        String xj = h(x0);

        int i = 0;
        while (true)
        {
//            if (S.contains(xj))
            if (S.contains(xi))
            {
                if (getFirstBit(0,S.get(i-1)) != getFirstBit(0,evaluated.get(i-1)))
                {
                    if (getFirstBit(0,S.get(i-1)) == 0)
                    {
                        xs = S.get(i-1) + evaluated.get(i-1);
                    }
                    else
                    {
                        xs = evaluated.get(i-1) + S.get(i-1);
                    }
                    break;
                }
                else
                {
                    System.out.println(S.get(i-1));
                    System.out.println(evaluated.get(i-1));
                    return null;
                }
            }
            else
            {
//                System.out.println(h(p + xi));
                S.add(xi);
                if (getFirstBit(0,xi) == 0)
                {
                    xi = h(xi);
                }
                else
                {
                    xi = h(y);
                }
            }
            i++;
            xj = xi;
            evaluated.add(xj);
        }
        return xs;
    }

    private static Pair<Integer, Integer> brentsAlgorithm(String x0)
    {
        int i = 0;
        int l = 1;
        String m = x0;
        String x = x0;

        while (true)
        {
            i++;
            x = h(x);
            if (x.equals(m))
            {
                break;
            }
            if (i >= (2 * l - 1))
            {
                m = x;
                l = 2 * l;
                while ((double)i < ((double)(3 / 2) * l - 1))
                {
                    x = h(x);
                    i++;
                }
            }
        }
        return new Pair<>(i, l - 1);
    }

    private static void hashCollisionSearch()
    {
        String x = "015301234015305678015311234015315678";
//        List<String> S = new ArrayList<>();

        HashMap<String, String> S = new HashMap<>();

        Pair<Integer, Integer> p = brentsAlgorithm(x);
        System.out.println("i: " + p.getKey() + ", j: " + p.getValue());

        int i;
        for (i = 0; i < p.getKey(); i++)
        {
            String xs = "015301234015305678015311234015315678" + x;
            x = h(xs);
//            x = h("015301234015305678015311234015315678" + x);
            if (i < p.getValue())
            {
//                S.add(x);
                S.put(x, xs);
            }
            else
            {
                if (S.containsKey(x))
                {
                    System.out.println("COLLISION");
                    System.out.println("x': " + x + " (" + xs + ")");
                    System.out.println("x: " + x + " (" + S.get(x) + ")");
                    System.out.println("S size: " + S.size());
                    break;
                }
                S.put(x, xs);
            }
        }

////        System.out.println("x' = " + meetInTheMiddleAttack(""));
//
//        String x = "123123123123";
//        String xs = "123123123123";
//        String x0 = "123123123123";
//        int i;
//        for (i = 0; i <= pow(2, 16); i++)
//        {
//            x = h(x);
//            xs = h(h(xs));
//
////            System.out.println(x + " : " + xs);
//
//            if (x.equals(xs))
//            {
//                break;
//            }
//        }
//
//        if (!x.equals(xs))
//        {
//            System.out.println("FAIL");
//            return;
//        }
//        xs = x;
//        x = x0;
//        for (int j = 1; j < i; j++)
//        {
//            if (h(x).equals(h(xs)))
//            {
//                System.out.println(x0 + " : " + x);
//                System.out.println(h(x0));
//                System.out.println(h(x));
//
//                break;
//            }
//            x = h(x);
//            xs = h(xs);
//        }
    }

    public static void main(String[] args)
    {
        //----------------------------------------------------------------------
        // 1 Public-Key Cryptography - RSA
        //
        Cryptography c = new Cryptography();
        BigInteger cipher = c.encode(CIPHERTEXT);

        BigInteger p = c.factor(N);
        System.out.println("p: " + p);

        BigInteger q = N.divide(p);
        System.out.println("q: " + q);

        BigInteger phi_n = (p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)));
        BigInteger d = E.modInverse(phi_n);
        System.out.println("d: " + d);

        BigInteger m = cipher.modPow(d, N);
        System.out.println("m: " + m);

        System.out.println("Decoding " + m + " ... " + c.decode(m));

        //----------------------------------------------------------------------
        // 2 Hash-Collision Search
        //
        hashCollisionSearch();
    }
}
