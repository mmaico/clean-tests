package mmaico.clean_tests.infratest.snapshot;

import au.com.origin.snapshots.comparators.SnapshotComparator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.skyscreamer.jsonassert.comparator.CustomComparator;

import java.util.ArrayList;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public class SnapshotComparatorTest implements SnapshotComparator {

    private CustomComparator comparators;

    public SnapshotComparatorTest(CustomComparator comparators) {
        this.comparators = comparators;
    }

    @Override
    public boolean matches(String prefix, String dataSnap, String dataService) {
        try {
            assertEquals(asObject(prefix, dataSnap), asObject(prefix, dataService), comparators);
        } catch (AssertionError | JSONException e) {
            System.out.println(e.getMessage());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private static String asObject(String snapshotName, String json) {
        try {
            return new ObjectMapper().readValue(json.replaceFirst(snapshotName, ""), ArrayList.class).get(0).toString();
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    public static SnapshotComparatorTest c(CustomComparator comparators) {
        return new SnapshotComparatorTest(comparators);
    }
}
