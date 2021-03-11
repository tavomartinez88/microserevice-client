package com.gustavomartinez.client;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.gustavomartinez.client.exceptions.BadRequestInputException;
import com.gustavomartinez.client.model.RequestClient;
import com.gustavomartinez.client.model.ResponseClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class LambdaHandler extends SpringBootRequestHandler<RequestClient, ResponseClient> {

    private Regions REGION = Regions.US_EAST_1;
    private DynamoDB dynamoDB;

    private final String TABLE_NAME = "CLIENTS";
    private final String FIRSTNAME_LASTNAME_INVALID = "El nombre y apellido deben ser strings alfabeticos";
    private final String AGE_INVALID = "La edad debe ser mayor a 0";
    private final String FORMAT_DATE_INVALID = "Formato de fecha de nacimiento invalido, debe tener el formato yyyy-mm-dd";

    private ResponseClient response;

    private static Log logger = LogFactory.getLog(com.gustavomartinez.client.LambdaHandler.class);

    @Override
    public ResponseClient handleRequest(RequestClient event, Context context) {
        logger.info("Creating client | request : "+event);
        response = new ResponseClient();
        try{
            validateRequestValid(event);
            this.initDbClient();
            this.insertClient(event);
        } catch (BadRequestInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } finally {
            logger.info("Client Created");
        }

        response.setStatus(HttpStatus.OK);
        return response;
    }

    private void initDbClient() {
        AmazonDynamoDB db = new AmazonDynamoDBClient();
        db.setRegion(Region.getRegion(REGION));
        this.dynamoDB = new DynamoDB(db);
    }

    protected void insertClient(RequestClient request) {
        this.dynamoDB.getTable(TABLE_NAME)
                .putItem(new PutItemSpec()
                        .withItem(new Item().
                                withString("firstName", request.getFirstName())
                                        .withString("lastName", request.getLastName())
                                        .withNumber("age", request.getAge())
                                        .withString("birthDate", request.getBirthDate())
                        ));
    }

    private boolean isValidDate(String date) {

        boolean valid = false;

        try {
            LocalDate.parse(date,
                    DateTimeFormatter.ofPattern("uuuu-M-d")
                            .withResolverStyle(ResolverStyle.STRICT)
            );

            valid = true;

        } catch (DateTimeParseException e) {
            valid = false;
        }

        return valid;
    }

    protected void validateRequestValid(RequestClient request) throws BadRequestInputException {
        if (!(request!= null && request.getFirstName() != null && request.getLastName() != null && request.getFirstName().chars().allMatch(Character::isLetter)
                && request.getLastName().chars().allMatch(Character::isLetter))) {
            throw new BadRequestInputException(FIRSTNAME_LASTNAME_INVALID);
        }

        if (!(request.getAge() != null && request.getAge()>0)) {
            throw new BadRequestInputException(AGE_INVALID);
        }

        if (!(request.getBirthDate() != null && !request.getBirthDate().isEmpty() && isValidDate(request.getBirthDate()))) {
            throw new BadRequestInputException(FORMAT_DATE_INVALID);
        }


    }
}
