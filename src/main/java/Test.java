import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/9/17.
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
        DateTime begin = new DateTime();
        Thread.sleep(2000);
        DateTime end = new DateTime();

        Duration diff = new Duration(begin, end);
        System.out.println(diff.getStandardSeconds());

        Period p = new Period().withMinutes(5);
    }
}
