package nl.boukenijhuis.provider;

public abstract class AbstractProvider implements Provider {

    protected String model;

    public AbstractProvider(String model) {
        setModel(model);
    }

    // makes sure there is always a selected model
    protected void setModel(String model) {
        if (model == null || model.isBlank()) {
            this.model = getDefaultModel();
        } else {
            this.model = model;
        }
    }

    public String getModel() {
        return model;
    }
}
