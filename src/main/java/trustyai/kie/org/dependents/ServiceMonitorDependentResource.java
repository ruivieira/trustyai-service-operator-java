package trustyai.kie.org.dependents;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;
import trustyai.kie.org.model.TrustyAIService;
import trustyai.kie.org.model.ServiceMonitor;
import trustyai.kie.org.spec.ServiceMonitorSpec;

import java.util.HashMap;
import java.util.Map;

@KubernetesDependent
public class ServiceMonitorDependentResource extends CRUDKubernetesDependentResource<ServiceMonitor, TrustyAIService> {
    public ServiceMonitorDependentResource() {
        super(ServiceMonitor.class);
    }

    @Override
    protected ServiceMonitor desired(TrustyAIService primary, Context<TrustyAIService> context) {

        final Map<String, String> labels = new HashMap<>();
        labels.put("modelmesh-service", "modelmesh-serving");

        final ServiceMonitorSpec spec = new ServiceMonitorSpec();
//        spec.getEndpoints().setInterval("30s");
//        spec.getEndpoints().setPath("/q/metrics");
//        spec.getEndpoints().setHonorLabels(true);
//        spec.getEndpoints().setHonorTimestamps(true);
//        spec.getEndpoints().setScrapeTimeout("10s");
//        spec.getEndpoints().setBearerTokenFile("/var/run/secrets/kubernetes.io/serviceaccount/token");
//        spec.getEndpoints().setTargetPort("8080");
//        spec.getEndpoints().setScheme("http");
//        spec.getEndpoints().getParams().put("match[]", List.of("{__name__= \"trusty_spd\"}", "{__name__= \"trusty_dir\"}"));



        final ObjectMeta metadata = new ObjectMetaBuilder()
                .withName("trustyai-metrics")
                .withLabels(labels).build();

        final ServiceMonitor serviceMonitor = new ServiceMonitor();
        serviceMonitor.setKind("ServiceMonitor");
        serviceMonitor.setApiVersion("monitoring.coreos.com/v1");
        serviceMonitor.setMetadata(metadata);
        serviceMonitor.setSpec(spec);

        serviceMonitor.setStatus(new ServiceMonitor.ServiceMonitorStatus());

        return serviceMonitor;




    }
}
