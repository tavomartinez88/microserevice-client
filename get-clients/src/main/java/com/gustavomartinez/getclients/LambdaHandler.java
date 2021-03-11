package com.gustavomartinez.getclients;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.gustavomartinez.getclients.model.Client;
import com.gustavomartinez.getclients.model.ResponseClients;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;
import org.springframework.http.HttpStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LambdaHandler extends SpringBootRequestHandler<Void, ResponseClients> {

    private final String TABLE_NAME = "CLIENTS";
    private Integer LIFE_EXPECTANCY = 80;

    private static Log logger = LogFactory.getLog(LambdaHandler.class);
    private Regions REGION = Regions.US_EAST_1;
    private DynamoDB dynamoDB;
    private ResponseClients response;
    private Client client;

    @Override
    public Object handleRequest(Void event, Context context) {
        logger.info("Get info client");
        response = new ResponseClients();

        this.initDbClient();
        List<Client> clients = this.getClients();
        logger.info("Retrieved clients: "+clients);
        response.setClients(clients);
        response.setStatus(HttpStatus.OK);
        return response;
    }

    private void initDbClient() {
        AmazonDynamoDB db = new AmazonDynamoDBClient();
        db.setRegion(Region.getRegion(REGION));
        this.dynamoDB = new DynamoDB(db);
    }

    private List<Client> getClients () {
        logger.info("scan dynamodb  and parse items to Client object");
        ItemCollection<ScanOutcome> items = this.dynamoDB.getTable(TABLE_NAME).scan(new ScanSpec());
        logger.info(items);
        List<Client> clients = new ArrayList<>();
        for(Item item: items) {
            if (item != null) {
                client = new Client();
                client.setAge(item.getInt("age"));
                client.setBirthDate(item.getString("birthDate"));
                client.setFirstName(item.getString("firstName"));
                client.setLastName(item.getString("lastName"));
                client.setPossibleDeadDate(
                        this.getPossibleDate(item.getString("birthDate"), item.getInt("age"))
                );
                clients.add(client);
            }
        }

        logger.info("scanned dynamodb");
        return clients;
    }

    private LocalDate getPossibleDate(String dateBirthDate, Integer yearsOld) {
        logger.info("convert possible dead date");
        logger.info("parsing string date : "+dateBirthDate+" to LocalDate");
        LocalDate date = LocalDate.parse(dateBirthDate);

        int diffYears = LIFE_EXPECTANCY - yearsOld;
        date = date.plusYears(diffYears);
        logger.info("Dead date possible is : "+date);
        return date;
    }
}
