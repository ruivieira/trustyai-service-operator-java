package trustyai.kie.org.dependents;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.openshift.api.model.monitoring.v1.Endpoint;
import io.fabric8.openshift.api.model.monitoring.v1.ServiceMonitor;
import io.fabric8.openshift.api.model.monitoring.v1.ServiceMonitorSpec;
import io.fabric8.openshift.api.model.monitoring.v1.ServiceMonitorSpecBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;
import trustyai.kie.org.model.TrustyAIService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@KubernetesDependent(labelSelector = "trustyai-service")
public class ServiceMonitorDependentResource extends CRUDKubernetesDependentResource<ServiceMonitor, TrustyAIService> {
    public ServiceMonitorDependentResource() {
        super(ServiceMonitor.class);
    }

    @Override
    protected ServiceMonitor desired(TrustyAIService primary, Context<TrustyAIService> context) {

        final Map<String, String> labels = new HashMap<>();
        labels.put("modelmesh-service", "modelmesh-serving");

        final ObjectMeta metadata = new ObjectMetaBuilder()
                .withName("trustyai-metrics")
                .withLabels(labels).build();

        final ServiceMonitor serviceMonitor = new ServiceMonitor();
        serviceMonitor.setKind("ServiceMonitor");
        serviceMonitor.setApiVersion("monitoring.coreos.com/v1");
        serviceMonitor.setMetadata(metadata);

        final Endpoint endpoint = new Endpoint();
        endpoint.setInterval("30s");
        endpoint.setPath("/q/metrics");
        endpoint.setHonorLabels(true);
        endpoint.setHonorTimestamps(true);
        endpoint.setScrapeTimeout("10s");
        endpoint.setBearerTokenFile("/var/run/secrets/kubernetes.io/serviceaccount/token");
        final SecretKeySelector secretKeySelector = new SecretKeySelector();
        secretKeySelector.setKey("");
        endpoint.setBearerTokenSecret(secretKeySelector);
        endpoint.setTargetPort(new IntOrString(8080));
        endpoint.setScheme("http");
        final Map<String, ArrayList<String>> params = new HashMap<>();
        params.put("match[]", new ArrayList<>(TrustyAIService.SUPPORTED_METRICS.stream().map(metric -> "{__name__= \"trusty_" + metric + "\"}")
                .collect(Collectors.toList())));
        endpoint.setParams(params);
        ServiceMonitorSpec spec = new ServiceMonitorSpecBuilder().withEndpoints(List.of(endpoint)).build();
        final LabelSelector selector = new LabelSelector();
        selector.setMatchLabels(Map.of("app.kubernetes.io/name", "trustyai-service"));
        spec.setSelector(selector);

        serviceMonitor.setSpec(spec);

        return serviceMonitor;




    }
}
