package nl.boukenijhuis.provider;

import java.lang.reflect.InvocationTargetException;

public class ProviderBuilderWithModel {
    private String model;
    private Class<? extends Provider> clazz;

    public ProviderBuilderWithModel(Class<? extends Provider> clazz, String model) {
        this.model = model;
        this.clazz = clazz;
    }

    public Provider build() {
        try {
            return clazz.getConstructor(String.class).newInstance(model);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
