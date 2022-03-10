package mmaico.lazytests.infratest.helpers.template;

import java.util.UUID;

public class UUIDHelper {

    public String get() {
        return UUID.randomUUID().toString();
    }

    public static UUIDHelper getInstance() {
        return new UUIDHelper();
    }
}
