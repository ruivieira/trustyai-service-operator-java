package trustyai.kie.org.spec;

public class ServiceMonitorSpec {

    public ServiceMonitorEndpointsSpec getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(ServiceMonitorEndpointsSpec endpoints) {
        this.endpoints = endpoints;
    }

    private ServiceMonitorEndpointsSpec endpoints = new ServiceMonitorEndpointsSpec();

    public ServiceMonitorSpec() {

    }
}
