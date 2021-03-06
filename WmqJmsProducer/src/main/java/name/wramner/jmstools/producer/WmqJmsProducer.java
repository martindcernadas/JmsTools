/*
 * Copyright 2016 Erik Wramner.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package name.wramner.jmstools.producer;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.XAConnectionFactory;

import org.kohsuke.args4j.Option;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.mq.jms.MQXAQueueConnectionFactory;
import com.ibm.msg.client.wmq.common.CommonConstants;

import name.wramner.jmstools.producer.WmqJmsProducer.WmqProducerConfiguration;

/**
 * Command line JMS IBM WebSphere MQ message producer intended for benchmarks and other tests.
 * 
 * @author Erik Wramner
 */
public class WmqJmsProducer extends JmsProducer<WmqProducerConfiguration> {

    /**
     * Program entry point.
     * 
     * @param args Command line.
     * @see AmqProducerConfiguration
     */
    public static void main(String[] args) {
        new WmqJmsProducer().run(args);
    }

    /**
     * Create configuration specific to this client.
     * 
     * @return configuration instance.
     * @see name.wramner.jmstools.producer.JmsProducer#createConfiguration()
     */
    @Override
    protected WmqProducerConfiguration createConfiguration() {
        return new WmqProducerConfiguration();
    }

    /**
     * IBM WebSphere MQ producer configuration. It extends the basic JMS producer configuration.
     * 
     * @author Erik Wramner
     */
    public static class WmqProducerConfiguration extends JmsProducerConfiguration {
        @Option(name = "-user", aliases = { "--jms-user" }, usage = "User name if using authentication")
        private String _userName;

        @Option(name = "-pw", aliases = { "--jms-password" }, usage = "Password if using authentication", depends = {
                "-user" })
        private String _password;

        @Option(name = "-host", aliases = { "--jms-host" }, usage = "Host or IP address for IBM MQ", required = true)
        private String _host;

        @Option(name = "-port", aliases = { "--jms-port" }, usage = "Port for IBM MQ", required = true)
        private Integer _port;

        @Option(name = "-qm", aliases = { "--queue-manager" }, usage = "The IBM MQ queue manager", required = true)
        private String _queueManager;

        @Option(name = "-ch", aliases = { "--channel" }, usage = "The IBM MQ channel", required = true)
        private String _channel;

        @Override
        public ConnectionFactory createConnectionFactory() throws JMSException {
            MQQueueConnectionFactory cf = new MQQueueConnectionFactory();
            configureFactory(cf);
            return cf;
        }

        @Override
        public XAConnectionFactory createXAConnectionFactory() throws JMSException {
            MQXAQueueConnectionFactory cf = new MQXAQueueConnectionFactory();
            configureFactory(cf);
            return cf;
        }

        private void configureFactory(MQQueueConnectionFactory cf) throws JMSException {
            cf.setHostName(_host);
            cf.setPort(_port);
            cf.setChannel(_channel);//communications link
            cf.setQueueManager(_queueManager);//service provider
            cf.setTransportType(CommonConstants.WMQ_CM_CLIENT);
        }
    }
}
