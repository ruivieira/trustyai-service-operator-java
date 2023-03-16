package trustyai.kie.org.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import trustyai.kie.org.spec.ServiceMonitorSpec;

public class ServiceMonitor extends CustomResource<ServiceMonitorSpec, ServiceMonitor.ServiceMonitorStatus> implements Namespaced {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ServiceMonitorStatus {
        // Not interested in the status of this resource.
    }
}
