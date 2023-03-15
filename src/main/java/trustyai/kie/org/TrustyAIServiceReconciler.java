package trustyai.kie.org;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.networking.v1.IngressBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.reconciler.Constants;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerConfiguration(name = "trustyai-service", namespaces = "trustyai")
public class TrustyAIServiceReconciler implements Reconciler<TrustyAIService> {
    private final KubernetesClient client;
    static final Logger log = LoggerFactory.getLogger(TrustyAIServiceReconciler.class);
    static final String APP_LABEL = "app.kubernetes.io/name";

    public TrustyAIServiceReconciler(KubernetesClient client) {
        this.client = client;
    }

    private Map<String, String> generatePrometheusAnnotations(TrustyAIService resource) {
        final Map<String, String> labels = new HashMap<>();
        labels.put("prometheus.io/path", "/q/metrics");
        labels.put("prometheus.io/port", "8080");
        labels.put("prometheus.io/scheme", "http");
        return labels;
    }

    @Override
    public UpdateControl<TrustyAIService> reconcile(TrustyAIService resource, Context context) {

        final var labels = Map.of(APP_LABEL, resource.getMetadata().getName());

        final var name = resource.getMetadata().getName();
        final var spec = resource.getSpec();
        final var imageRef = spec.getImage();
        final var metadata = createMetadata(resource, labels);
        final var storage = spec.getStorage();
        final var data = spec.getData();
        final var metrics = spec.getMetrics();

        // Create environment variables
        final EnvVar storageFormat = generateEnv("SERVICE_STORAGE_FORMAT", storage.getFormat());
        final EnvVar storageFolder = generateEnv("STORAGE_DATA_FOLDER", storage.getFolder());
        final EnvVar dataFormat = generateEnv("SERVICE_DATA_FORMAT", data.getFormat());
        final EnvVar dataFilename = generateEnv("STORAGE_DATA_FILENAME", data.getFilename());
        final EnvVar metricsSchedule = generateEnv("SERVICE_METRICS_SCHEDULE", metrics.getSchedule());

        final List<EnvVar> storageEnvironmentVariables = List.of(
                storageFormat,
                storageFolder,
                dataFormat,
                dataFilename,
                metricsSchedule
        );

        // @formatter:off
    log.info("Create deployment {}", metadata.getName());
    final var deployment = new DeploymentBuilder()
        .withMetadata(createMetadata(resource, labels))
        .withNewSpec()
          .withNewSelector().withMatchLabels(labels).endSelector()
          .withNewTemplate()
            .withNewMetadata().withLabels(labels).withAnnotations(generatePrometheusAnnotations(resource)).endMetadata()
            .withNewSpec()
              .addNewContainer()
                .withName(name).withImage(imageRef)
                .addNewPort().withName("http").withProtocol("TCP").withContainerPort(8080).endPort()
            .withEnv(storageEnvironmentVariables)
              .endContainer()
            .endSpec()
          .endTemplate()
        .endSpec()
        .build();
    client.apps().deployments().createOrReplace(deployment);

    log.info("Create service {}", metadata.getName());
    client.services().createOrReplace(new ServiceBuilder()
        .withMetadata(createMetadata(resource,labels))
        .withNewSpec()
          .addNewPort()
            .withName("http")
            .withPort(80)
            .withNewTargetPort().withIntVal(8080).endTargetPort()
          .endPort()
          .withSelector(labels)
          .withType("ClusterIP")
        .endSpec()
        .build());

    log.info("Create ingress {}", metadata.getName());
    metadata.setAnnotations(Map.of(
        "nginx.ingress.kubernetes.io/rewrite-target", "/",
        "kubernetes.io/ingress.class", "nginx"
    ));
    client.network().v1().ingresses().createOrReplace(new IngressBuilder()
        .withMetadata(metadata)
        .withNewSpec()
          .addNewRule()
            .withNewHttp()
              .addNewPath()
                .withPath("/")
                .withPathType("Prefix")
                .withNewBackend()
                  .withNewService()
                    .withName(metadata.getName())
                    .withNewPort().withNumber(8080).endPort()
                  .endService()
                .endBackend()
              .endPath()
            .endHttp()
          .endRule()
        .endSpec()
        .build());

        return UpdateControl.noUpdate();
    }

    private ObjectMeta createMetadata(TrustyAIService resource, Map<String, String> labels) {
        final var metadata = resource.getMetadata();
        return new ObjectMetaBuilder()
                .withName(metadata.getName())
                .addNewOwnerReference()
                .withUid(metadata.getUid())
                .withApiVersion(resource.getApiVersion())
                .withName(metadata.getName())
                .withKind(resource.getKind())
                .endOwnerReference()
                .withLabels(labels)
                .withAnnotations(generatePrometheusAnnotations(resource))
                .build();
    }

        public static EnvVar generateEnv(String name, String fieldPath) {
        final EnvVar var = new EnvVar();
        var.setName(name);
        final EnvVarSource envVarSource = new EnvVarSource();
        final ObjectFieldSelector fieldRef = new ObjectFieldSelector();
        envVarSource.setFieldRef(fieldRef);
        var.setValue(fieldPath);
        return var;
    }
}

