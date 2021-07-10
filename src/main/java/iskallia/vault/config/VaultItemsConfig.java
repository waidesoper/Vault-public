package iskallia.vault.config;

import com.google.gson.annotations.Expose;

public class VaultItemsConfig extends Config {
    @Expose
    public VaultBurger VAULT_BURGER;

    @Expose
    public VaultPizza OOZING_PIZZA;

    @Override
    public String getName() {
        return "vault_items";
    }

    @Override
    protected void reset() {
        this.VAULT_BURGER = new VaultBurger();
        this.VAULT_BURGER.minExpPercent = 0.1f;
        this.VAULT_BURGER.maxExpPercent = 0.2f;

        this.OOZING_PIZZA = new VaultPizza();
        this.OOZING_PIZZA.minExpPercent = 0.1f;
        this.OOZING_PIZZA.maxExpPercent = 0.2f;
    }

    public static class VaultBurger {
        @Expose
        public float minExpPercent;
        @Expose
        public float maxExpPercent;
    }

    public static class VaultPizza {
        @Expose
        public float minExpPercent;
        @Expose
        public float maxExpPercent;
    }
}
