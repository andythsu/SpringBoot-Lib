package org.github.andythsu.GCP.Services;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Iterator;
import java.util.Map;

public class DatastoreService {

    private Logger log = LoggerFactory.getLogger(DatastoreService.class);

    private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    private static final KeyFactory keyFactory = datastore.newKeyFactory();

    public static Iterator<Entity> getAllByKind(String kind) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(kind)
                .build();
        return runQuery(query);
    }

    public static Iterator<Entity> getLastCreatedByKind(String kind){
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(kind)
                .setOrderBy(StructuredQuery.OrderBy.desc(UtilService.commonNames.CREATEDAT))
                .setLimit(1)
                .build();
        return runQuery(query);
    }

    /**
     * return all entities if exists
     * @return
     */
    public static Iterator<Entity> getAllByKindAndData(String kind, String col, String data){
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(kind)
                .setFilter(StructuredQuery.PropertyFilter.eq(col, data))
                .build();
        return runQuery(query);
    }

    /**
     * check if data exists in column
     * @param kind
     * @param col
     * @param data
     * @return
     */
    public static boolean isTypeInKind(String kind, String col, String data){
        Iterator<Entity> en = getAllByKindAndData(kind, col, data);
        return en.hasNext();
    }

    public static Iterator<Entity> runQuery(Query<Entity> query){
        return datastore.run(query);
    }

    public static Iterator<Entity> getLastUpdatedByKind(String kind){
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(kind)
                .setOrderBy(StructuredQuery.OrderBy.desc(UtilService.commonNames.UPDATEDAT))
                .setLimit(1)
                .build();
        return runQuery(query);
    }

    public static String saveByKind(String kind, Map<String, Object> data){
        keyFactory.setKind(kind);
        Key taskKey = datastore.allocateId(keyFactory.newKey());
        FullEntity.Builder entity = FullEntity.newBuilder(taskKey);

        /* map data */
        entity = mapEntity(entity, data);

        Key insertedKey = datastore.add(entity.build()).getKey();
        return insertedKey.toString();
    }

    private static FullEntity.Builder mapEntity(FullEntity.Builder entity, Map<String, Object> data){
        for (String key : data.keySet()) {
            Object value = data.get(key);
            if (value instanceof String) {
                entity.set(key, (String) value);
            } else if (value instanceof Integer) {
                entity.set(key, (Integer) value);
            } else if (value instanceof Long) {
                entity.set(key, (Long) value);
            } else if (value instanceof Double) {
                entity.set(key, (Double) value);
            } else if (value instanceof Timestamp) {
                entity.set(key, (Timestamp) value);
            }
        }
        return entity;
    }



}
