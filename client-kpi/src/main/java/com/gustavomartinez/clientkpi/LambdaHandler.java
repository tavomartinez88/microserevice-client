package com.gustavomartinez.clientkpi;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.xspec.S;
import com.amazonaws.services.lambda.runtime.Context;
import com.gustavomartinez.clientkpi.model.ResponseKpiClients;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;
import org.apache.commons.logging.Log;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class LambdaHandler extends SpringBootRequestHandler<Void, ResponseKpiClients> {

    private final String TABLE_NAME = "CLIENTS";

    private Regions REGION = Regions.US_EAST_1;
    private DynamoDB dynamoDB;
    private static Log logger = LogFactory.getLog(com.gustavomartinez.clientkpi.LambdaHandler.class);
    private ResponseKpiClients response;

    @Override
    public ResponseKpiClients handleRequest(Void event, Context context) {
        logger.info("Get client to calculate kpi");
        response = new ResponseKpiClients();

        initDbClient();
        Map<String, Double> map = this.getAverageAndDeviation();
        logger.info("getAverageAndDeviation calculated");
        response.setAverage(map.get("average"));
        response.setStandarDeviation(map.get("stadard-aviation"));
        response.setStatus(HttpStatus.OK);

        return response;
    }

    private void initDbClient() {
        AmazonDynamoDB db = new AmazonDynamoDBClient();
        db.setRegion(Region.getRegion(REGION));
        this.dynamoDB = new DynamoDB(db);
    }

    private Map<String, Double> getAverageAndDeviation() {
        Map<String, Double> map = new HashMap<>();
        int sumAge = 0;
        int countItems = 0;
        double variant = 0.0;
        double average = 0.0;

        try {
            ItemCollection<ScanOutcome> items = this.dynamoDB.getTable(TABLE_NAME).scan(new ScanSpec());
            for (Item value : items) {
                countItems++;
                if (value != null) {
                    sumAge += value.getInt("age");
                }

                System.out.println(value.get("firstName") + " - " + value.get("lastName"));
            }

            logger.info("Retrieved list clients");
            logger.info("calculate average to populate map");
            average = this.getAverage(sumAge, countItems);
            map.put("average", average);

            logger.info("calculate standard aviation to populate map");
            for (Item value : items) {
                if (value != null) {
                    variant += Math.pow(value.getInt("age") - average, 2);
                }
            }

            logger.info("variant calculated: "+variant);
            double standardDeviation = Math.sqrt(variant / countItems);
            map.put("stadard-aviation", standardDeviation);
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }

        return map;

    }

    private double getAverage(int countAge, int countClients) {
        double average = countAge/countClients;
        logger.info("average calculated: "+average);
        return average;
    }
}
