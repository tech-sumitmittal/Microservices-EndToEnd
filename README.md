# 🚀 Microservices End-to-End Platform

![Java](https://img.shields.io/badge/Java-17+-orange)
&nbsp;&nbsp; ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Microservices-brightgreen)
&nbsp;&nbsp; ![Docker](https://img.shields.io/badge/Docker-Enabled-blue)
&nbsp;&nbsp; ![Kubernetes](https://img.shields.io/badge/Kubernetes-Orchestrated-blueviolet)
&nbsp;&nbsp; ![Helm](https://img.shields.io/badge/Helm-Charts-important)

A complete **end-to-end microservices architecture** showcasing Spring Boot, Config Server, Eureka, Gateway, Kafka, RabbitMQ, Redis, Keycloak, Docker, Kubernetes, and Helm.

---

## 📚 Table of Contents

1. [Prerequisites](#-prerequisites)
2. [Run Options Overview](#-run-options-overview)
3. [Option 1: Local From IntelliJ](#-option-1-local-from-intellij)
4. [Option 2: Dockerfile of Individual Components](#-option-2-dockerfile-of-individual-components)
5. [Option 3: Docker Compose](#-option-3-docker-compose)
6. [Option 4: Kubernetes](#-option-4-kubernetes)
7. [Option 5: Helm](#-option-5-helm)
8. [Important Endpoints](#-important-endpoints)
9. [Kubernetes Dashboard](#-kubernetes-dashboard)
10. [Helm Commands & Rollback](#-helm-commands--rollback)

---

## ✅ Prerequisites

- Java 17+
- Docker & Docker Desktop
- IntelliJ IDEA
- Kubernetes (Docker Desktop)
- Helm
- Git

---

## 🔄 Run Options Overview

| Option | Technology |
|------|-----------|
| 1 | Local + Docker |
| 2 | Dockerfile |
| 3 | Docker Compose |
| 4 | Kubernetes |
| 5 | Helm |

---

<br/>

## 🧑‍💻 Option 1: Local from IntelliJ

### 🐰 RabbitMQ
install and run rabbitMQ on local
```bash
  docker run -d --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4-management
```
🔗 http://localhost:15672 (guest / guest)

### 📨 Kafka
install and run Apache kafka server in local
```bash
  docker run -p 9092:9092 apache/kafka:4.1.1
```

### ⚡ Redis
install and run Redis server in local
```bash
  docker run -d -p 6379:6379 --name sumitredis redis
```

### 🔐 Keycloak
Start Keycloak Auth Server
```bash
  docker run -p 127.0.0.1:7080:8080 -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:26.4.7 start-dev
```
🔗 http://localhost:7080 (admin / admin)

### 🗄️ MySQL Databases
Start mysql databases in local
```bash
    docker run -d -p 3306:3306 --name accountsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=accountsdb mysql
    docker run -d -p 3307:3306 --name cardsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=cardsdb mysql
    docker run -d -p 3308:3306 --name loansdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=loansdb mysql
```

### ▶️ Start Services (IntelliJ)
- Config Server → http://localhost:8888/accounts/prod
- Eureka Server → http://localhost:8761
- Accounts → http://localhost:8080/api/v1/contact-info
- Cards → http://localhost:8081/api/v1/contact-info
- Loans → http://localhost:8082/api/v1/contact-info

### 🌐 Gateway
- Actuator → http://localhost:8072/actuator
- Routes → http://localhost:8072/actuator/gateway/routes    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; [to see details in this end point, update application.yml   spring.cloud.gateway.discovery.locator.enabled=true]
- Sample API → http://localhost:8072/sumitbank/accounts/api/v1/contact-info

### 🗄 Databases
- To access database -                                             <br/>
  host=localhost &nbsp;&nbsp;&nbsp;	 port=3306  &nbsp;&nbsp;&nbsp;	username=root &nbsp;&nbsp;&nbsp; 	password=root  <br/>
  host=localhost &nbsp;&nbsp;&nbsp;	 port=3307  &nbsp;&nbsp;&nbsp;	username=root &nbsp;&nbsp;&nbsp;	password=root  <br/>
  host=localhost &nbsp;&nbsp;&nbsp;	 port=3308  &nbsp;&nbsp;&nbsp;	username=root &nbsp;&nbsp;&nbsp;	password=root  <br/>

---
<br/>

## 🐳 Option 2: Dockerfile of Individual Components

Run docker images of individual microservices using their Dockerfiles -

1. create docker image from individual Dockerfile -
    > docker build . t sumitmittal2022/accounts:1.0
2. Run individual docker image -
    > docker run -d -p 8080:8080 sumitmittal2022/accounts:1.0
3. Push individual docker image to docker hub -
   > docker push docker.io/sumitmittal2022/accounts:1.0

---
<br/>

## 📦 Option 3: Docker Compose

```bash
    cd docker-compose/default
    docker-compose up -d --build
    docker-compose down -v
    
    - To access database -
      host=localhost  	 port=3306  	username=root  	password=root
      host=localhost 	 port=3307  	username=root 	password=root
      host=localhost 	 port=3308  	username=root 	password=root   
```

---
<br/>


## ☸️ Option 4: Kubernetes

```bash
    kubectl apply -f 1_configmap.yml
    kubectl apply -f 2_secrets.yml
    kubectl apply -f 3_redis.yml

    - To access database -
      host=localhost  	 port=30306  	username=root  	password=root
      host=localhost 	 port=30307  	username=root 	password=root
      host=localhost 	 port=30308  	username=root 	password=root   
```

---

<br/>


## 🧭 Option 5: Helm

```bash
  cd helm
  
```

---

<br/>


## 🔗 Important Endpoints

### Actuators
```bash
    http://localhost:8080/actuator
    http://localhost:8081/actuator
    http://localhost:8082/actuator
```

### Config Server
```bash
    http://localhost:8888/accounts/{profile}            
    http://localhost:8888/cards/{profile}
    http://localhost:8888/loans/{profile}
    profile =   default  /  qa  /  prod
```

### Eureka server dashboard
```bash
    http://localhost:8761
    http://localhost:8761/eureka/apps
    
    To deregister any application from Eureka Server -
      POST :	http://localhost:8080/actuator/shutdown
      POST :	http://localhost:8081/actuator/shutdown
      POST :	http://localhost:8082/actuator/shutdown
```

### 🔄 Bus Refresh
```bash
    POST http://localhost:8080/actuator/busrefresh
    
    To check bus refresh - 
        Step-1: change accounts accounts.yml file in git configs       GET :  https://github.com/tech-sumitmittal/microservices-configs/blob/master/accounts.yml
        Step-2: see the updated values in config server                GET :  http://localhost:8888/accounts/default
        Step-3: Check the accounts MS has old values                   GET :  http://localhost:8080/api/v1/contact-info
        Step-4: Hit busrefresh url and check in accounts MS            POST:  http://localhost:8080/actuator/busrefresh
```

### 🤝 Feign Client
```bash
  http://localhost:8080/customers/v1/fetch?mobileNumber=9876543210
```

### 📊 Grafana UI dashboard
```bash
  http://localhost:3000
```

---

<br/>


## 🖥️ Kubernetes Dashboard

- Install helm and k8s UI &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  (Ref: https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard/)

```bash
  STEP-1: start docker desktop and kubernetes
  
  STEP-2: Install Helm(k8s pkg manager) through winget package manager, (Winget already comes with window, hence open command prompt and run below command) -
            winget install Helm.Helm --source winget
          
  STEP-3: To check helm is installed -
            helm version

  STEP-4: Install Kubernates dashboard UI -
            # Add kubernetes-dashboard repository
            helm repo add kubernetes-dashboard https://kubernetes.github.io/dashboard/
            # Deploy a Helm Release named "kubernetes-dashboard" using the kubernetes-dashboard chart
            helm upgrade --install kubernetes-dashboard kubernetes-dashboard/kubernetes-dashboard --create-namespace --namespace kubernetes-dashboard

  STEP-5: Run below command to expose the k8s dashboard at 8443 port -
            kubectl -n kubernetes-dashboard port-forward svc/kubernetes-dashboard-kong-proxy 8443:443

  STEP-6: To access k8s dashboard -
            https://localhost:8443
            
  STEP-7: To access database - 
            host=localhost 	port=30306	username=root 	password=root 	initialDB=accountsdb
            host=localhost 	port=30307	username=root 	password=root 	initialDB=cardsdb
            host=localhost 	port=30308	username=root 	password=root 	initialDB=loansdb       
```

<br/>

- Create a sample user in k8s & generate token to access the k8s dashboard <br/>
    (Ref: https://github.com/kubernetes/dashboard/blob/master/docs/user/access-control/creating-sample-user.md)
```bash
  cd D:\Study\Project\Repos\Microservices-EndToEnd\kubernetes
  kubectl apply -f  setup/dashboard-adminuser.yml
  kubectl apply -f  setup/dashboard-rolebinding.yml
  #kubectl apply -f setup/dashboard-secret.yml
  #kubectl get secret admin-user -n kubernetes-dashboard -o jsonpath="{.data.token}" | base64 -d
  kubectl  create token admin-user -n kubernetes-dashboard
```

<br/>

- k8s commands
```bash
    kubectl apply -f 1_configmap.yml
    kubectl apply -f 2_secrets.yml
    kubectl apply -f 3_redis.yml
    .
    .
       
    # get
    kubectl get deployments                               # to check all the deployments in default namespace
    kubectl get replicasets                               # to check all the replicaset in default namespace
    kubectl get pods                                      # to check all the pods in default namespace
    kubectl get services                                  # to check all the services in default namespace
    
    # describe
    kubectl describe deployment/replicaset/pod/service <accounts-deployment-6456b6497b>   
    
    # logs
    kubectl logs -f configserver-deployment-65c98849b5-kq78r                # -f to tail logs
    kubectl logs -f accounts-deployment-6456b6497b-n4c47 -c accounts        # If the pod has multiple containers
    kubectl logs -f deployment/accounts-deployment                          # tail logs using deployment name [recommended]   

    # Clean up
    kubectl delete serviceaccount admin-user -n kubernetes-dashboard
    kubectl delete clusterrolebinding admin-user -n kubernetes-dashboard
    kubectl delete -f 5_accounts.yml    [This will remove all resources defined in that YAML, including: Deployment, Replicaset, Pods, Service]
        # if want to delete manually 1 by 1 then :
        kubectl delete deployment <DEP>
        kubectl delete replicatset <REP>
        kubectl delete pod <POD>
        kubectl delete service <SER>
        kubectl delete pvc <PVC>

    # deploy change
    kubectl scale deployment accounts-deployment --replicas=3
    kubectl set image deployment gatewayserver-deployment gatewayserver=sumitmittal2022/gatewayserver:2.0 --record
    
    # rollout history
    kubectl rollout history deployment gatewayserver-deployment
        REVISION  CHANGE-CAUSE
        1         <none>
        2         kubectl.exe set image deployment gatewayserver-deployment gatewayserver=sumitmittal2022/gatewayserver:2.0 --record=true
    # rollout to any version
    kubectl rollout undo deployment gatewayserver-deployment --to-revision=1
    
    # all events occurred in our k8s cluster
    kubectl get events --sort-by=.metadata.creationTimestamp --watch
```

---

<br/>

## 🎛️ Helm Commands & Rollback

```bash
    # installing existing helm chart
    helm ls
    helm repo add bitnami https://charts.bitnami.com/bitnami
    helm install happy-panda bitnami/wordpress
    helm uninstall happy-panda
    
    # create our own helm chart
    D:\Study\Project\Repos\Microservices-EndToEnd\helm
    helm create sumitbank-common
    
    cd helm\sumitbank-microservices
    helm create accounts
    cd helm\sumitbank-microservices\accounts
    helm dependencies build                                   #as accounts helm chart has dependency on common helm chart, so run this dependencies build command inside account to fetch the dependencies
    
    cd helm\environments\dev-env
    helm template .					      #to see the built k8s manifest files
    
    # install keycloak
    download keycloak charts folder from https://github.com/bitnami/charts/tree/main/bitnami and paste in helm folder
    cd helm\keycloak
    helm dependencies build                                     #Build / download chart dependencies
    cd helm
    helm install keycloak keycloak                              #Install Keycloak using Helm, now keycloak is available at http://localhost:80] [Not working because commercial paid now
    helm install keycloak bitnami/keycloak --version 24.3.0     #Install Keycloak using Helm, now keycloak is available at http://localhost:80
    kubectl get svc keycloak -n default -w                      #prints the current state of the service in default namespace, -w : Watch for live updates
    helm uninstall keycloak                                     #Uninstall keycloak using Helm
    
    # run prod profile
    cd helm\environments
    helm install sumitbank prod-env
    helm upgrade sumitbank prod-env                             #to deploy updates
    
    # rollback
    cd helm\environments
    helm history sumitbank                                      #to see all the helm install history
    helm rollback sumitbank 1                                   #to rollback the helm to revision 1
```

---

<br/>

✨ **Happy Coding & Cloud-Native Learning!**
