package cli;

import java.util.Vector;

public class Main {

    class Runnies {
        final Runnable runny0 = incubate4.Incubator;
    }

    Runnies allRunnies = new Runnies();
    Vector<Runnable> runnies = new Vector<>();

    public Main() {
        runnies.add(allRunnies.runny0);
    }

    public static void main(String[] args) {

    }

}
