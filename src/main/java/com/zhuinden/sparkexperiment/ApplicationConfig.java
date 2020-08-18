package com.zhuinden.sparkexperiment;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationConfig {

    @Autowired
    private Environment env;

    @Value("${app.name:study01}")
    private String appName;

    @Value("${spark.home}")
    private String sparkHome;

    @Value("${master.uri:local}")
    private String masterUri;

    @Bean
    public SparkConf sparkConf() {
        System.out.println("======================================================");
        System.out.println(" Configuration started!!! by jang masterUri:" +masterUri);
        System.out.println("=====================================================");

        SparkConf sparkConf = new SparkConf()
                .setAppName(appName)
//                .setSparkHome(sparkHome)
                .setMaster(masterUri)
//                .setMaster("spark://my-release6-spark-master-0.my-release6-spark-headless.default.svc.cluster.local:7077")
//                .set("spark.mongodb.input.uri", "mongodb://localhost/DB.Collection")
//        		.set("spark.mongodb.output.uri","mongodb://localhost/DB.Collection")
                ;
        return sparkConf;
    }

    @Bean
    public JavaSparkContext javaSparkContext() {
        return new JavaSparkContext(sparkConf());
    }

    @Bean
    public SparkSession sparkSession() {
        return SparkSession
                .builder()
                .sparkContext(javaSparkContext().sc())
                .appName("Java Spark SQL basic example")
                .getOrCreate();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}