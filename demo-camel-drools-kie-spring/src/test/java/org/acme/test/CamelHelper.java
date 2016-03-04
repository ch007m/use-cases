package org.acme.test;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;

public class CamelHelper {

    private CamelContext ctx;

    public void sendMessage(String msg) {
        ProducerTemplate t = ctx.createProducerTemplate();
        t.sendBody("direct:log_message",msg);
    }

    public CamelContext getCtx() {
        return ctx;
    }

    public void setCtx(CamelContext ctx) {
        this.ctx = ctx;
    }
}
