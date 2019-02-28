# K8S LOGGING

Output microservice app and request logs in json format to containers std.out and std.err

Fluentd Collector running as daemonset on each K8s node forwards the logs to splunk instance.

Log message is in json format which are then built into the log show below with addition container/pod/node k8 info.


## Steps (presuming minikube)

### Create **poc** name space in cluster.
`kubectl -f k8s\namespace.yaml create`

### Create 3 pod microapp deployment  + service
`kubectl -f k8s\sample-app\deployment.yaml create`

`kubectl -f k8s\sample-app\service.yaml create`

### Create 3 pod microapp deployment  + service
`kubectl -f k8s\splunk-server\deployment.yaml create`

`kubectl -f k8s\splunk-server\service.yaml create`

### Open splunk ui
Get the ip `minikube ip` and the splunk port `kubectl get services -n poc | grep splunk` to see what the exposed service port is mapped to **8000** port.

Then visit the address http://{minikube_ip}:{exposed_port}/en-US/account/login

Login with user:admin password:Password1

### Create Http Event Collector

Go to Settings -> Data Inputs -> Http Event Collector

In Global settings toggle **All Tokens** enabled. And untick **Enable SSL**

Click New Token and fill in the wizard. On completion copy the **Token Value** generated.

### Create fluentd daemonset forwarder

Using the copied token value run this command substituting in the token value and create the secret
 
 ```
 kubectl --namespace kube-system create secret generic splunk-config \
  --from-literal=hec-token=TOKEN_VALUE \
  --from-literal=hec-address=splunk-server.poc.svc.cluster.local:8088 \
  --from-literal=hec-protocol=http \
  --from-literal=hec-verify-tls=false \
  --from-literal=hec-index=main
 ```
 
 Create the splunk forwarder with `kubectl -f k8s/fluentd-forwarder/fluentd-splunk-0.1/kubernetes/fluentd-splunk.yaml create`
 
 ### Generate some container logs
 Get the ip `minikube ip` and the app port `kubectl get services -n poc | grep microapp` to see what the exposed service port is mapped to **80** port.
 
 Using that info send some curls to generate logs eg:
 
  
 `curl -X PUT http://{cluster_id}:{exposed_port}/microapp-api/log/info -H 'cache-control: no-cache'`
   
 `curl -X PUT http://{cluster_id}:{exposed_port}/microapp-api/log/warn -H 'cache-control: no-cache'`
  
 `curl -X PUT http://{cluster_id}:{exposed_port}/microapp-api/log/error -H 'cache-control: no-cache'`
  
### View Logs in Splunk

In index main for now should be able to see the json formatted application and request logs. Along with info about the node pod and container the logs came from

APP LOG:
```json
{
  "timestamp": "2019-02-28 02:00:49.542",
  "level": "INFO",
  "thread": "qtp40690331-18",
  "logger": "com.mckinleyit.microapp.rest.MicroAppController",
  "message": "Produced a info. log=info, someText=cyc",
  "context": "default",
  "logType": "application-log",
  "log": "{\"timestamp\":\"2019-02-28 02:00:49.542\",\"level\":\"INFO\",\"thread\":\"qtp40690331-18\",\"logger\":\"com.mckinleyit.microapp.rest.MicroAppController\",\"message\":\"Produced a info. log=info, someText=cyc\",\"context\":\"default\",\"logType\":\"application-log\"}\n",
  "stream": "stdout",
  "docker": {
    "container_id": "4d2bb2726eaa495917c53775f9e04d1c6a5a186ea2cda87607fd2d2e3ab7a158"
  },
  "kubernetes": {
    "container_name": "microapp",
    "namespace_name": "poc",
    "pod_name": "microapp-deployment-6f7c7776d5-rstk4",
    "pod_id": "7ea466fd-3af7-11e9-bda6-080027b6bd9b",
    "labels": {
      "app": "microapp",
      "pod-template-hash": "2937333281"
    },
    "host": "minikube",
    "master_url": "https://10.96.0.1:443/api"
  }
}
```


REQUEST LOG:
```json
{
  "timestamp": "2019-02-28 02:01:02.389",
  "logType": "access-log",
  "thread": "qtp40690331-18",
  "contentlegnth": "61",
  "method": "PUT",
  "protocol": "HTTP/1.1",
  "uri": "/microapp-api/log/info",
  "url": "PUT /microapp-api/log/info HTTP/1.1",
  "queryString": "",
  "remoteHost": "172.17.0.1",
  "headers": {
    "Accept": "*/*",
    "Cache-Control": "no-cache",
    "Host": "192.168.99.100:31103",
    "User-Agent": "curl/7.54.0"
  },
  "status": 200,
  "log": "{\"timestamp\":\"2019-02-28 02:01:02.389\",\"logType\":\"access-log\",\"thread\":\"qtp40690331-18\",\"contentlegnth\":\"61\",\"method\":\"PUT\",\"protocol\":\"HTTP/1.1\",\"uri\":\"/microapp-api/log/info\",\"url\":\"PUT /microapp-api/log/info HTTP/1.1\",\"queryString\":\"\",\"remoteHost\":\"172.17.0.1\",\"headers\":{\"Accept\":\"*/*\",\"Cache-Control\":\"no-cache\",\"Host\":\"192.168.99.100:31103\",\"User-Agent\":\"curl/7.54.0\"},\"status\":200}\n",
  "stream": "stdout",
  "docker": {
    "container_id": "7adc2572debd2a2a45a89f2b500e321b8be21011b592bc5acd009a54e416d007"
  },
  "kubernetes": {
    "container_name": "microapp",
    "namespace_name": "poc",
    "pod_name": "microapp-deployment-6f7c7776d5-rv2cz",
    "pod_id": "763a3258-3af7-11e9-bda6-080027b6bd9b",
    "labels": {
      "app": "microapp",
      "pod-template-hash": "2937333281"
    },
    "host": "minikube",
    "master_url": "https://10.96.0.1:443/api"
  }
}
```



