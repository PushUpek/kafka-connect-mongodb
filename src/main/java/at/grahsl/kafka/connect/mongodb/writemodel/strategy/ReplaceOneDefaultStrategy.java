package at.grahsl.kafka.connect.mongodb.writemodel.strategy;

import at.grahsl.kafka.connect.mongodb.converter.SinkDocument;
import com.mongodb.DBCollection;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import org.apache.kafka.connect.errors.DataException;
import org.bson.BsonDocument;

public class ReplaceOneDefaultStrategy implements WriteModelStrategy {

    @Override
    public WriteModel<BsonDocument> createWriteModel(SinkDocument document, boolean isUpsertEnabled) {
            UpdateOptions updateOptions = new UpdateOptions().upsert(isUpsertEnabled);

        BsonDocument vd = document.getValueDoc().orElseThrow(
                () -> new DataException("error: cannot build the WriteModel since"
                        + " the value document was missing unexpectedly")
        );

        return new ReplaceOneModel<>(
                new BsonDocument(DBCollection.ID_FIELD_NAME,
                        vd.get(DBCollection.ID_FIELD_NAME)),
                vd,
                updateOptions);

    }
}
