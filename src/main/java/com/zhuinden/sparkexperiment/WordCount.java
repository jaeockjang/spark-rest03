package com.zhuinden.sparkexperiment;



import lombok.Synchronized;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;


/**
 * Created by achat1 on 9/23/15.
 * Just an example to see if it works.
 */
@Component
public class WordCount {

    @Value("${app.name:rest03}")
    private String appName;

    @Value("${spark.home}")
    private String sparkHome;

    @Value("${master.uri:local[*]}")
    private String masterUri;

    private SparkSession sparkSession;

    @Value("${data.path:./}")
    private String path;

    @Synchronized
    public List<Count> count() throws Exception {

        // Setting --------------------------------------------------
        SparkConf sparkConf = new SparkConf()
                .setAppName(appName)
                .setMaster(masterUri)
                ;

        JavaSparkContext javaSparkContext=new JavaSparkContext(sparkConf);

        sparkSession=SparkSession
                .builder()
                .sparkContext(javaSparkContext.sc())
                .appName("Java Spark SQL basic example")
                .getOrCreate();
        //------------------------------------------------------------

        JavaRDD<String> textFile = sparkSession.sparkContext().textFile(path+ "schedule.csv",8).toJavaRDD() ;
        System.out.println("Text file count:" + textFile.count() );

        Dataset<Row> data = sparkSession.createDataFrame(textFile, Schedule.class);
        data.createOrReplaceTempView("my_schedule");
        Dataset<Row> sqlDS=sparkSession.sql("select * from my_schedule");

        Encoder<Schedule> scheduleEncoder = Encoders.bean(Schedule.class);

        Dataset<Schedule> scheduleDataset = sqlDS.map(new MapFunction<Row, Schedule>() {
            @Override
            public Schedule call(Row row) throws Exception {
                System.out.println("Data======"+ row.getString(0));
                return new Schedule(row.getString(0));
            }
        }, scheduleEncoder);

        System.out.println( scheduleDataset.toString()) ;
        Dataset<String> j= scheduleDataset.select("carrier_cd").toJSON();

        System.out.println(   ) ;
        System.out.println( "end!!!!!!!!!") ;

//        sparkSession.sparkContext().stop();

            proc2() ;
            Count count= new Count();
            count.setWord(UUID.randomUUID().toString());
            count.setCount( Math.random());


            // Stop logic =====================================================
        try {
//            javaSparkContext.close();
            sparkSession.sparkContext().stop();
            sparkSession.close();
        }catch (Exception ex) {

        }

            return Arrays.asList(count);

    }



    private void proc2() {
        try {
            Dataset<Row> ds =  sparkSession.sqlContext().read().format("com.databricks.spark.csv")
                    .option("header", "true").load(path+"schedule.csv");
            ds.show(false);
            System.out.println("csv loaded");
        }catch (Exception ex) {
            ex.printStackTrace();
        }

    }



}