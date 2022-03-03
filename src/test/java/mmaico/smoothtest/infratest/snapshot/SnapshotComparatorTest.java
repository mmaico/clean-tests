package mmaico.smoothtest.infratest.snapshot;

import au.com.origin.snapshots.comparators.SnapshotComparator;
import org.json.JSONException;
import org.skyscreamer.jsonassert.comparator.CustomComparator;

import static mmaico.smoothtest.infratest.helpers.SnapshotHelper.getJson;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public class SnapshotComparatorTest implements SnapshotComparator {

    private CustomComparator comparators;

    public SnapshotComparatorTest(CustomComparator comparators) {
        this.comparators = comparators;
    }

    @Override
    public boolean matches(String prefix, String dataSnap, String dataService) {
        try {
            assertEquals(getJson(prefix, dataSnap), getJson(prefix, dataService), comparators);
        } catch (AssertionError | JSONException e) {
            System.out.println(e.getMessage());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public static SnapshotComparatorTest c(CustomComparator comparators) {
        return new SnapshotComparatorTest(comparators);
    }
}
