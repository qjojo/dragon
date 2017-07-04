public class Entity {
    private boolean isAlive;
    private String name;
    private java.util.Random rngStream;

    // basic combat stats
    private int hp;
    private int maxHp;
    private int atk;
    private int def;

    // rng related stats (chances are in [0, 1])
    private double evaChance;
    private double crtChance;
    private double crtMult;
    private double ohkChance;

    public Entity(String eName, int[] basics, double[] rng, java.util.Random battleRng) {
        isAlive = true;
        rngStream = battleRng;
        name = eName;
        hp = basics[0];
        maxHp = basics[0];
        atk = basics[1];
        def = basics[2];

        evaChance = rng[0];
        crtChance = rng[1];
        crtMult = rng[2];
        ohkChance = rng[3];
    }

    public String act(Action action, Entity target) {
        switch(action) {
            case NOP:
                return(this.name + " waits.\n");
            case ATK:
                return(this.attack(target));
        }
        return "error: no action";
    }

    public String attack(Entity target) {
        if(target.checkEvasion()) {
            return(target.getName() + " evades " + this.name + "'s attack!\n");
        }
        int fnAtk = -1 * this.atk;
        double x = this.rngStream.nextDouble();
        if(x <= this.ohkChance) {
            target.setHp(0);
            return(this.name + " hits " + target.getName() + " for a one hit kill!\n");
        }
        x = this.rngStream.nextDouble();
        if(x <= this.crtChance) {
            fnAtk *= this.crtMult;
        }
            fnAtk -= target.getDef();
            target.modHp(fnAtk);
            return(this.name + " hits " + target.getName() + " for " + (-1 * fnAtk) + "HP!\n");
    }

    public void tick() {
        if(this.hp == 0) {
            this.isAlive = false;
        }
    }

    public String stats() {
        String printString = String.format("%s\n HP: %d\n ATK: %d\t DEF: %d\n\n", name, hp, atk, def);
        if(crtChance != 0.0) {
            printString += String.format(" %d%% chance to do %.2fx damage\n", (int) (crtChance * 100), crtMult);
        }
        if(evaChance != 0.0) {
            printString += String.format(" %d%% chance to evade\n", (int) (evaChance * 100));
        }
        if(ohkChance != 0.0) {
            printString += String.format(" %.2f chance to instantly kill!\n", ohkChance);
        }
        printString += "\n";
        return printString;
    }

    // getters and setters

    public int getHp() {
        return this.hp;
    }
    public void setHp(int newHp) {
        if(newHp < 0) {
            newHp = 0;
        }
        this.hp = newHp;
    }
    public int getMaxHp() { return this.maxHp; }
    public void modHp(int mod) {
        this.hp += mod;
        if(this.hp < 0) {
            this.hp = 0;
        }
    }
    public void revive() { this.isAlive = true; }
    public int getDef() { return this.def; }
    public String getName() { return this.name; }

    public boolean checkEvasion() {
        double x = this.rngStream.nextDouble();
        return(x <= this.evaChance);

    }

    public boolean isAlive() {
        return isAlive;
    }
}
