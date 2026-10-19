# Kubernetes – Notes

## Summary

A **cluster** is a group of **nodes**: machines with their own CPU and RAM. Each app gets its own **Deployment**, which sets the image and the number of copies (for example `replicas: 3`). The Deployment automatically creates a **ReplicaSet**, which keeps that many **pods** running on the nodes. Each app also gets its own **Service**, one fixed address for reaching its pods. An **Ingress** is the single entry point from the internet.

```
Internet → Ingress → Service → Pods (created by ReplicaSet ← Deployment) → running on Nodes
```

---

## 1. Cluster & Nodes

### Cluster
A group of nodes managed together by Kubernetes.

### Control Plane
The "brain" of the cluster. It stores the desired state and keeps the cluster matching it.
- **API server** receives every `kubectl` command.
- **Scheduler** chooses which node each pod runs on.
- **Controllers** keep the actual state equal to the desired state (for example, they recreate dead pods).
- **etcd** is the database where everything is stored.

### Node
A machine (physical or virtual) with its own **CPU and RAM**, like a PC. Pods run on nodes.

**Node processes:**
- **kubelet** starts and watches the pods on its node.
- **kube-proxy** handles networking so Services can reach pods.
- **Container runtime** actually runs the containers (for example containerd).

---

## 2. Workloads

### Pod
The smallest unit in Kubernetes: a wrapper around the **container running our application**, with its own IP.

> Each pod has a unique IP, so ports don't clash. Two pods can both use 8080.

### ReplicaSet
Makes sure the right **number of pods** is always running. The Deployment creates it automatically; you never create it yourself.

### Deployment
Describes **which image** to run and **how many replicas**. It creates the ReplicaSet, recreates crashed pods, and handles updates with no downtime.

> ✏️ The Deployment does **not** choose the node. The **Scheduler** does.

**Pod names** come from the Deployment and ReplicaSet:
```
kubernetes-bootcamp - 5798758fb5 - mlzhr
   (Deployment)       (ReplicaSet)  (Pod)
```

### Rolling update
A rolling update lets a Deployment update with **zero downtime**. It replaces the current pods with new ones step by step. The new pods are scheduled on nodes with available resources, and Kubernetes waits for them to start before removing the old pods.

### Labels
Key-value tags on objects, used to select and group them:
```powershell
kubectl label pods $POD_NAME version=v1
```

---

## 3. Networking

### Service
**One fixed address** that spreads traffic across the pods of an app, and only to pods that are Ready. One Service per app.

| Type | Reachable from | Details |
|---|---|---|
| **ClusterIP** (default) | Inside the cluster only | Exposes the Service on an internal IP in the cluster |
| **NodePort** | Outside, via `<node-ip>:30000-32767` | Opens the same port on every node (using NAT). Includes everything ClusterIP does |
| **LoadBalancer** | Internet, via a public IP from the cloud | Creates an external load balancer in the cloud (if supported) with a fixed external IP. Includes everything NodePort does |
| **ExternalName** | — | Maps the Service to a DNS name (for example `foo.bar.example.com`) using a CNAME record. No proxying |

**The ports in a NodePort Service:**

| Port | Meaning |
|---|---|
| `targetPort` | Where the app listens inside the pod. **Must match the app** |
| `port` | The Service's own port inside the cluster. Can be any number |
| `nodePort` | The port opened on every node (30000–32767) |

### Ingress
The **single entry point from the internet**. It sends each request to the right Service based on the URL path (or domain).

---

## 4. Tools

### kubectl
The command-line tool used to **communicate with the cluster** (it talks to the API server).

### minikube
A local Kubernetes cluster, made for learning and developing with Kubernetes.

> minikube can't see your local Docker images. Load them first:
> ```powershell
> minikube image load user:api
> ```

---

## 5. Automating with YAML (like Docker Compose)

Instead of typing `kubectl create` and `kubectl expose`, describe everything in a **YAML file** and apply it with one command.

**`k8s.yaml`:**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: user-api
spec:
  replicas: 1
  selector:
    matchLabels:
      app: user-api
  template:
    metadata:
      labels:
        app: user-api
    spec:
      containers:
        - name: user-api
          image: user:api
          imagePullPolicy: Never
          ports:
            - containerPort: 8080
          readinessProbe:
            httpGet:
              path: /api/v1/apps
              port: 8080
---
apiVersion: v1
kind: Service
metadata:
  name: user-api
spec:
  type: NodePort
  selector:
    app: user-api
  ports:
    - port: 8080
      targetPort: 8080
```

- `imagePullPolicy: Never` uses the image loaded with `minikube image load` instead of pulling from Docker Hub.
- `readinessProbe` makes the Service send traffic only once the app answers on `/api/v1/apps`.
- The Service finds the pods through the label `app: user-api`.

**Commands:**

```powershell
kubectl apply -f k8s.yaml     # start / update everything
kubectl delete -f k8s.yaml    # stop and remove everything
```

To change something (more replicas, a new image), edit the file and run `kubectl apply` again. Kubernetes only changes what's different.

| Docker Compose | Kubernetes |
|---|---|
| `docker-compose.yml` | `k8s.yaml` (one or many files) |
| `docker compose up` | `kubectl apply -f k8s.yaml` |
| `docker compose down` | `kubectl delete -f k8s.yaml` |
| `services:` → one entry per app | One `Deployment` + one `Service` per app |

**Tools that go further:**
- **Kompose** turns an existing `docker-compose.yml` into Kubernetes YAML: `kompose convert`.
- **Skaffold** rebuilds the image, loads it into minikube and reapplies the YAML each time you save code.
- **Helm** packages YAML as reusable templates, for example the same app in dev and in prod with different settings.

---

## 6. Common commands (PowerShell)

### minikube
```powershell
minikube start                              # start the cluster
minikube status                             # check host / kubelet / apiserver are Running
minikube stop                               # stop the cluster (keeps everything)
minikube delete                             # delete the whole cluster (images too)
minikube image load user:api                # copy a local Docker image into minikube
minikube image ls                           # list images minikube can see
minikube ip                                 # node IP (not reachable from Windows with Docker driver)
minikube service my-service --url           # open a tunnel to a Service (keep the terminal open)
minikube node add                           # add another node to the cluster
```

### Look at things
```powershell
kubectl get nodes                           # list nodes
kubectl get all                             # deployments, replicasets, pods, services
kubectl get pods -o wide                    # pods + their IP and node
kubectl get deployments,replicasets,pods    # see the Deployment → ReplicaSet → Pod chain
kubectl get services                        # list Services and their ports (8080:30007/TCP)
kubectl get endpointslices -l kubernetes.io/service-name=my-service   # pods the Service sends traffic to
kubectl describe pod <pod-name>             # details + events (why a pod fails to start)
kubectl describe service my-service         # Service details, selector, endpoints
kubectl logs <pod-name>                     # app logs of a pod
kubectl logs <pod-name> -f                  # follow logs live
kubectl get pods -w                         # watch pods change live
```

### Create / change / delete
```powershell
kubectl apply -f deployment.yaml            # create or update everything in the file
kubectl delete -f deployment.yaml           # delete everything in the file
kubectl create deployment user-app --image=user:api             # create a Deployment without YAML
kubectl expose deployment/user-app --type=NodePort --port 8080  # create a Service for it
kubectl scale deployment/user-app --replicas=3                  # change the number of pods
kubectl delete deployment user-app          # delete a Deployment (and its pods)
kubectl delete service my-service           # delete a Service
kubectl label pods <pod-name> version=v1    # add a label to a pod
kubectl get pods -l version=v1              # list pods with that label
```

### Reach a pod or Service directly
```powershell
kubectl proxy                               # open the API on http://localhost:8001 (keep it open)
kubectl port-forward pod/<pod-name> 8080:8080        # localhost:8080 → that one pod
kubectl port-forward service/my-service 8080:8080    # localhost:8080 → the Service
kubectl exec -ti <pod-name> -- sh           # open a shell inside the pod
```

### Save values in variables
```powershell
$POD_NAME = kubectl get pods -o jsonpath='{.items[0].metadata.name}'                      # first pod's name
$NODE_PORT = kubectl get services/my-service -o jsonpath='{.spec.ports[0].nodePort}'      # the Service's NodePort
curl.exe "http://localhost:8001/api/v1/namespaces/default/pods/${POD_NAME}:8080/proxy/api/v1/apps"   # via kubectl proxy
```

> In PowerShell use `curl.exe` (not `curl`) and `${POD_NAME}` when a `:` follows the variable.
