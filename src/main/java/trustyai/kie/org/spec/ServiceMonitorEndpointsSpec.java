package trustyai.kie.org.spec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceMonitorEndpointsSpec {
    private String interval = "30s";
    private String path = "/q/metrics";
    private boolean honorLabels = true;
    private boolean honorTimestamps = true;
    private String scrapeTimeout = "10s";
    private String bearerTokenFile = "/var/run/secrets/kubernetes.io/serviceaccount/token";
    private ServiceMonitorBearerTokenSecret bearerTokenSecret = new ServiceMonitorBearerTokenSecret();
    private String targetPort = "8080";
    private String scheme = "http";
//    private Map<String, List<String>> params = new HashMap<>();

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isHonorLabels() {
        return honorLabels;
    }

    public void setHonorLabels(boolean honorLabels) {
        this.honorLabels = honorLabels;
    }

    public boolean isHonorTimestamps() {
        return honorTimestamps;
    }

    public void setHonorTimestamps(boolean honorTimestamps) {
        this.honorTimestamps = honorTimestamps;
    }

    public String getScrapeTimeout() {
        return scrapeTimeout;
    }

    public void setScrapeTimeout(String scrapeTimeout) {
        this.scrapeTimeout = scrapeTimeout;
    }

    public String getBearerTokenFile() {
        return bearerTokenFile;
    }

    public void setBearerTokenFile(String bearerTokenFile) {
        this.bearerTokenFile = bearerTokenFile;
    }

    public ServiceMonitorBearerTokenSecret getBearerTokenSecret() {
        return bearerTokenSecret;
    }

    public void setBearerTokenSecret(ServiceMonitorBearerTokenSecret bearerTokenSecret) {
        this.bearerTokenSecret = bearerTokenSecret;
    }

    public String getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(String targetPort) {
        this.targetPort = targetPort;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

//    public Map<String, List<String>> getParams() {
//        return params;
//    }
//
//    public void setParams(Map<String, List<String>> params) {
//        this.params = params;
//    }

}
