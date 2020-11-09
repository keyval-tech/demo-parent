import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
public class Tests {

    static AtomicLong consumeTimes = new AtomicLong(0);

    public static void main(String[] args) {
        System.out.println(consumeTimes.get());
        System.out.println(consumeTimes.incrementAndGet());
        System.out.println(consumeTimes.get());
    }
}
