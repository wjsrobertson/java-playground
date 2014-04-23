import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;

public class HelloMongo {

    public static void main(String[] args) throws UnknownHostException {
        HelloMongo app = new HelloMongo();
        app.start();
    }

    private void start() throws UnknownHostException {
        MongoClient mongoClient = new MongoClient( "baby" , 27017 );
        try {
            DB db = mongoClient.getDB("test_db");
            DBCollection table = db.getCollection("user");

            BasicDBObject document = new BasicDBObject();
            document.put("name", "mkyong");
            document.put("age", 30);
            document.put("createdDate", LocalDateTime.now());
            table.insert(document);

        } finally {
            mongoClient.close();
        }
    }

    private void listDatabases(MongoClient mongoClient) {
        List<String> dbs = mongoClient.getDatabaseNames();
        for(String db : dbs){
            System.out.println(db);
        }
    }

}
