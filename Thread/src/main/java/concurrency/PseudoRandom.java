package concurrency;

/**
 * @author lzr
 * @date 6/24/2022
 */

public class PseudoRandom {
    int calculateNext(int prev) {
        prev ^= prev << 6;
        prev ^= prev >>> 21;
        prev ^= (prev << 7);
        return prev;
    }
}