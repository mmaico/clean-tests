package mmaico.lazytests.infratest.helpers.template;

public class MathHelper {


    public int addMoreOne(Integer value) {
        return value + 1;
    }

    public static MathHelper getInstance() {
        return new MathHelper();
    }
}
