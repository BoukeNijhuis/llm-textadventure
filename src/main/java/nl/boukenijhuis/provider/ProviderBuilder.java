package nl.boukenijhuis.provider;

import java.util.Objects;

public class ProviderBuilder {

    private Class<? extends Provider> clazz;

    public ProviderBuilder(Class clazz) {
        this.clazz = clazz;
    }

    public ProviderBuilderWithModel model(String model) {
        return new ProviderBuilderWithModel(clazz, model);
    }

    @Override
    public String toString() {
        return "ProviderBuilder{clazz=%s}".formatted(clazz);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(clazz, ((ProviderBuilder) o).clazz);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(clazz);
    }
}
