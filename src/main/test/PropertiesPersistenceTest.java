import burp.settings.PropertiesPersistence;
import burp.settings.Settings;
import com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader;
import org.junit.*;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.Assert.*;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/11/17.
 */
public class PropertiesPersistenceTest {
    private PropertiesPersistence persistence;
    private final BufferedReader br = new BufferedReader(new StringReader("intervals.1m = true\n" +
            "intervals.2m = false\n" +
            "intervals.4m = true\n" +
            "intervals.6m = true\n" +
            "intervals.8m = false\n" +
            "intervals.12m = false\n" +
            "intervals.16m = true\n" +
            "intervals.20m = false\n" +
            "intervals.24m = true\n" +
            "proxy.useBurp = true"));

    @Before
    public void setUp() throws Exception {
        persistence = new PropertiesPersistence();
    }

    @Test
    public void load() throws Exception {
//        Settings settings = persistence.load(br);
//        br.close();
//        assertEquals(settings.isIntervals1m(), true);
//        assertEquals(settings.isIntervals2m(), false);
//        assertEquals(settings.isIntervals4m(), true);
//        assertEquals(settings.isIntervals6m(), true);
//        assertEquals(settings.isIntervals8m(), false);
//        assertEquals(settings.isIntervals12m(), false);
//        assertEquals(settings.isIntervals16m(), true);
//        assertEquals(settings.isIntervals20m(), false);
//        assertEquals(settings.isIntervals24m(), true);
//        assertEquals(settings.useBurp(), true);
    }

    @Test
    public void save() throws Exception {
        Settings allFalse = new Settings();
        persistence.save(allFalse);
//
//        Settings s1 = persistence.load();
//
//        assertEquals(s1.isIntervals1m(), false);
//        assertEquals(s1.isIntervals2m(), false);
//        assertEquals(s1.isIntervals4m(), false);
//        assertEquals(s1.isIntervals6m(), false);
//        assertEquals(s1.isIntervals8m(), false);
//        assertEquals(s1.isIntervals12m(), false);
//        assertEquals(s1.isIntervals16m(), false);
//        assertEquals(s1.isIntervals20m(), false);
//        assertEquals(s1.isIntervals24m(), false);
//        assertEquals(s1.useBurp(), false);
//        s1.setUseBurp(true);
//
//        persistence.save(s1);
//
//        Settings s2 = persistence.load();
//        assertEquals(s2.isIntervals1m(), false);
//        assertEquals(s2.isIntervals2m(), false);
//        assertEquals(s2.isIntervals4m(), false);
//        assertEquals(s2.isIntervals6m(), false);
//        assertEquals(s2.isIntervals8m(), false);
//        assertEquals(s2.isIntervals12m(), false);
//        assertEquals(s2.isIntervals16m(), false);
//        assertEquals(s2.isIntervals20m(), false);
//        assertEquals(s2.isIntervals24m(), false);
//        assertEquals(s2.useBurp(), true);
    }
}