# trustyai-service-operator

Operator for the [TrustyAI service](https://github.com/trustyai-explainability/trustyai-explainability/tree/main/explainability-service).

## Usage

### Example using KinD

Create cluster.
```shell
$ kind create cluster --image=kindest/node:v1.22.15
$ kubectl cluster-info --context kind-kind
```

Create namespace (`trustyai` in this example)

```shell
$ export NAMESPACE="trustyai"
$ kubectl create namespace $NAMESPACE
```

Set working namespace.

```shell
$ kubectl config set-context --current --namespace=$NAMESPACE
```

Create service account:

```shell
kubectl create clusterrolebinding default-pod \
  --clusterrole cluster-admin \
  --serviceaccount=${NAMESPACE}:trustyai-operator
```

Add Prometheus (needed for the `ServiceMonitor`):

```shell
kubectl create -f "https://raw.githubusercontent.com/prometheus-operator/prometheus-operator/master/bundle.yaml"
```

Add CRD to cluster:

```shell
kubectl apply -f "https://raw.githubusercontent.com/ruivieira/trustyai-service-operator/main/operator/trustyaiservices.org.kie.trustyai-v1.yml" -n $NAMESPACE
```

Add operator:

```shell
kubectl apply -f "https://raw.githubusercontent.com/ruivieira/trustyai-service-operator/main/operator/kubernetes.yml" -n $NAMESPACE
```

Deploy the service

```shell
kubectl apply -f "https://raw.githubusercontent.com/ruivieira/trustyai-service-operator/main/demo/trustyai.yaml" -n $NAMESPACE
```