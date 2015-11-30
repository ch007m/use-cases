package com.redhat.gpe.demo.internal;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

public class QueryElasticsearch {

    public static void main(String[] args) {

        Settings settings = ImmutableSettings.settingsBuilder()
                .classLoader(Settings.class.getClassLoader())
                .put("cluster.name", "insight")
                .put("client.transport.sniff", false)
                .build();
        Client client = new TransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress("localhost",9300));

        SearchResponse response = client.prepareSearch("insight-*")
                .setTypes("sta-camel")
                .setQuery(QueryBuilders.matchAllQuery())
                .setSearchType(SearchType.DEFAULT)
                .setFrom(0).setSize(60).setExplain(true)
                .execute()
                .actionGet();

        System.out.println("Result : " + response.getTotalShards());

        SearchHit[] searchHitArray = response.getHits().getHits();

        if (searchHitArray.length > 0) {
            SearchHit searchHit = searchHitArray[0];
            System.out.println("#########" + searchHit.getSourceAsString());
            System.out.println("*****************Hits***************" + response.getHits().getHits());
        } else {
            System.out.println("No results found");
        }
    }

}
