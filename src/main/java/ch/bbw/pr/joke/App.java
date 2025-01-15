package ch.bbw.pr.joke;

import com.mongodb.client.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Aggregates.match;

/**
 * Beispiele für Abfragen mit MongoDB
 *
 * @author Andrin Renggli
 * @version 15.01.2025
 */
public class App {
    public static void main(String[] args) {
        System.out.println("App.main: Abfragen mit JokeDB");

        String connectionString = "mongodb://root:1234@localhost:27017";
        MongoClient mongoClient = MongoClients.create(connectionString);

        System.out.println("List all databases:");
        mongoClient.listDatabases().forEach((Consumer<? super Document>) result -> System.out.println(result.toJson()));

        MongoDatabase jokeDB = mongoClient.getDatabase("jokeDB");
        MongoCollection<Document> jokesCollection = jokeDB.getCollection("jokes");

        System.out.println("\nAlle Witze:");
        jokesCollection.find()
                .forEach((Consumer<? super Document>) document -> System.out.println(document.toJson()));

        System.out.println("\nJoke mit ID 2:");
        jokesCollection.find(eq("id", 2))
                .forEach((Consumer<? super Document>) document -> System.out.println(document.toJson()));

        System.out.println("\nJokes mit ID 1 und 3:");
        jokesCollection.find(in("id", 1, 3))
                .forEach((Consumer<? super Document>) document -> System.out.println(document.toJson()));

        String specificDate = "2022-01-08 21:39:40";

        System.out.println("\nWitz mit spezifischem Datum:");
        jokesCollection.find(eq("date", specificDate))
                .projection(fields(include("rating"), excludeId()))
                .forEach((Consumer<? super Document>) document -> System.out.println(document.toJson()));

        List<Bson> aggregationPipeline = Arrays.asList(
                match(eq("date", "2022-01-08 21:42:41"))
        );


        jokesCollection.aggregate(aggregationPipeline)
                .forEach((Consumer<? super Document>) document -> System.out.println(document.toJson()));


        mongoClient.close();
    }
}
