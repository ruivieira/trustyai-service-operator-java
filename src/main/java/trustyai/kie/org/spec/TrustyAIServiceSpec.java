package trustyai.kie.org.spec;

import trustyai.kie.org.model.Data;
import trustyai.kie.org.model.Metrics;
import trustyai.kie.org.model.Storage;

public class TrustyAIServiceSpec {

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;
    private Storage storage;
    private Data data;
    private Metrics metrics;

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }
}
