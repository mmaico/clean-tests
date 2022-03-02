package mmaico.smoothtest.infratest.snapshot;

import au.com.origin.snapshots.comparators.SnapshotComparator;

public class SnapshotComparatorTest implements SnapshotComparator {
    @Override
    public boolean matches(String s, String s1, String s2) {
        return false;
    }
}
