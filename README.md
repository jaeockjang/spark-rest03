# spring-spark-example
An example of setting up Spring-Boot with Spark.

## script
    kubectl delete -f deployment.yaml && \
    mvn clean package -DskipTests && \
    docker build -t jojang91/spark-api:0.0.1 . && \
    docker push  jojang91/spark-api:0.0.1 && \
    kubectl apply -f deployment.yaml



#docker build -t jojang91/spark-api:0.0.1 .
### 1. kubernetes deploy yaml 만들기
    kubectl create deployment spark-api --image=jojang91/spark-api:0.0.1 --dry-run=client -o=yaml > deployment.yaml
    echo --- >> deployment.yaml
    kubectl create service clusterip spark-api --tcp=8902:8902 --dry-run=client -o=yaml >> deployment.yaml

    kubectl port-forward svc/spark-api 8902:8902

 imagePullPolicy: Always 을 deployment.yalm에 반드시 넣어야 한다.

## 2. build 과정
### 2.1 maven build
    mvn clean package -DskipTests
 
### 2.2 Dockerfile
 FROM bitnami/spark:latest
 USER root
 ARG JAR_FILE=target/*.jar
 COPY ${JAR_FILE} app1.jar
 
 RUN mkdir -p /opt/bitnami/spark/data2
 COPY ./schedule.csv /opt/bitnami/spark/data2/
 
 ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/opt/bitnami/spark/app1.jar"]
 
 --> 반드시 Base Image를 Spark로 하고 Spring boot의 jar을 copy한다.
  
### 2.3 docker image build
    docker build -t jojang91/spark-api:0.0.1 . 
 
### 2.4 docker images push
    docker push  jojang91/spark-api:0.0.1
  
## 3. kubernetes deployment
    kubectl apply -f deployment.yaml  
    kubectl delete -f deployment.yaml 
   