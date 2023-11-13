package concurrency;

/**
 * @author lzr
 * @date 5/31/2022
 */

public class RandomNumberGenerator {

    static int xorShift(int y) {
        y ^= (y << 6);
        y ^= (y >>> 21);
        y ^= (y << 7);
        return y;
    }

    public static void main(String[] args) {
        System.out.println(RandomNumberGenerator.xorShift(Long.hashCode(System.nanoTime())));
        System.out.println(RandomNumberGenerator.xorShift(Long.hashCode(System.nanoTime())));
    }
}