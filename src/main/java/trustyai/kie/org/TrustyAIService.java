package trustyai.kie.org;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

@Version("v1alpha1")
@Group("org.kie.trustyai")
public class TrustyAIService extends CustomResource<TrustyAIServiceSpec, TrustyAIServiceStatus> implements Namespaced {

    public TrustyAIService() {

    }
}

