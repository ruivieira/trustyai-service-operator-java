package trustyai.kie.org;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;

import java.util.HashMap;
import java.util.Map;

@KubernetesDependent
class ConfigMapDependentResource extends CRUDKubernetesDependentResource<ConfigMap, TrustyAIService> {

    private static final String NAME = "model-serving-config";

    public ConfigMapDependentResource() {
        super(ConfigMap.class);
    }

    @Override
    protected ConfigMap desired(TrustyAIService primary, Context<TrustyAIService> context) {

//        apiVersion: v1
//        kind: ConfigMap
//        metadata:
//          name: model-serving-config
//        data:
//          config.yaml: |
//            payloadProcessors: "http://trustyai-service.$current_namespace/consumer/kserve/v2"

        final Map<String, String> data = new HashMap<>();
        data.put("config.yaml", "payloadProcessors: \"http://trustyai-service." + primary.getMetadata().getNamespace() + "/consumer/kserve/v2\"");

        return new ConfigMapBuilder()
                .withNewMetadata()
                    .withName(NAME)
                .endMetadata()
                .withData(data)
                .build();
    }
}