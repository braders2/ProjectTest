import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.remote.FtpComponent;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

public class Main {

    public static void main(String[] args) throws Exception {
        CamelContext camelContext = new DefaultCamelContext();
        
/*        camelContext.addRoutes(new RouteBuilder() {
            @Override
            
            public void configure() throws Exception {
                from("file:data").to("ftp://kaczkaplywasobie.ugu.pl:testowy123@kaczkaplywasobie.ugu.pl:21");
            }
        });*/

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
// Note we can explicit name the component
        camelContext.addComponent("test-jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file:data").transform().body().to("test-jms:topic:start");
                from("test-jms:topic:start").to("log:out").to("system:out");
            }
        });
        camelContext.start();
        Thread.sleep(10000000);
    }

    public void featureTest(){
        System.out.println("Feature/Test");
    }
}
