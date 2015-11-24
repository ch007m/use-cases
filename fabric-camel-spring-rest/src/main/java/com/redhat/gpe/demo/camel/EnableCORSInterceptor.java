package com.redhat.gpe.demo.camel;

import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.Headers;

public class EnableCORSInterceptor extends AbstractPhaseInterceptor<Message> {

    public EnableCORSInterceptor() {
        super(Phase.PRE_PROTOCOL);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        Map<String, List<String>> headers = Headers.getSetProtocolHeaders(message);
        try {
            //Access-Control-Allow-Origin:* Access-Control-Allow-Methods:POST,GET
            //URL url = new URL("http", "127.0.0.1/9090", "/rest");
            //headers.put("Location", Arrays.asList(url.toString()));
            headers.put("Access-Control-Allow-Origin", Arrays.asList("*"));
            headers.put("Access-Control-Allow-Methods", Arrays.asList("POST", "GET"));
        } catch (Exception ce) {
            throw new Fault(ce);
        }
    }
}