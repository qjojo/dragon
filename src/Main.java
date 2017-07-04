public class Main {
    static String v = "0.0.1";

    public static void main(String[] args) {
        System.out.println("dragon " + v + "\n");

        java.util.Random rng = new java.util.Random();

        int[] knightStatsBasic = {300, 80, 30};
        double[] knightStatsRng = {0.90, 0.33, 3.0, 0.0};

        int[] dragonStatsBasic = {1500, 120, 60};
        double[] dragonStatsRng = {0.0, 0.0, 1.0, 0.25};

        Entity p1 = new Entity("Brave Knight", knightStatsBasic, knightStatsRng, rng);
        Entity p2 = new Entity("Handsome Dragon", dragonStatsBasic, dragonStatsRng, rng);

        System.out.printf("%s vs %s!\n\n", p1.getName(), p2.getName());
        System.out.print(p1.stats());
        System.out.print(p2.stats());

        int[] counter = {0, 0};

        Main.simMany(p1, p2, counter, 100000);
    }

    public static void simOne(Entity p1, Entity p2) {
        Entity victor = null;
        String transcript = "";
        while(true) {
            transcript += p1.act(Action.ATK, p2);
            transcript += p2.act(Action.ATK, p1);

            p1.tick();
            p2.tick();
            transcript += String.format("\n%s: %dHP\t %s %dHP\n\n", p1.getName(), p1.getHp(), p2.getName(), p2.getHp());
            if(!p1.isAlive()) {
                victor = p2;
                break;
            } else if(!p2.isAlive()) {
                victor  = p1;
                break;
            }
        }
        System.out.print("\n" + transcript);
        System.out.printf("\n%s wins!\n", victor.getName());
    }

    public static void simMany(Entity p1, Entity p2, int[] counter, int n) {
        System.out.printf("%s and %s fighting %d battles:\n", p1.getName(), p2.getName(), n);
        for(int i = 0; i < n; i++) {
            while(true) {
                p1.act(Action.ATK, p2);
                p2.act(Action.ATK, p1);
                p1.tick();
                p2.tick();
                if (!p1.isAlive()) {
                    counter[1]++;
                    break;
                } else if (!p2.isAlive()) {
                    counter[0]++;
                    break;
                }
            }
            p1.setHp(p1.getMaxHp());
            p1.revive();
            p2.setHp(p1.getMaxHp());
            p2.revive();
        }
        System.out.printf(" %s: %d\n %s: %d", p1.getName(), counter[0], p2.getName(), counter[1]);
    }
}
