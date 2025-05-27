package nl.boukenijhuis.provider;

import java.lang.reflect.InvocationTargetException;

public class ProviderBuilder{

    private String model;
    private Class<? extends Provider> clazz;

    public ProviderBuilder(Class clazz) {
        this.clazz = clazz;
    }

    public ProviderBuilder model(String model){
        this.model = model;
        return this;
    }

    public Provider build() {

        // TODO tests
        // TODO check if model is filled, if not use default model
        try {
            return clazz.getConstructor(String.class).newInstance(model);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
