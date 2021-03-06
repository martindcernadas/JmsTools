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
package name.wramner.jmstools.consumer;

import javax.jms.*;

import org.kohsuke.args4j.Option;

import name.wramner.jmstools.aq.AqJmsUtils;
import name.wramner.jmstools.aq.AqObjectMessageAdapter;
import name.wramner.jmstools.consumer.AqJmsConsumer.AqConsumerConfiguration;
import name.wramner.jmstools.messages.ObjectMessageAdapter;

/**
 * JMS consumer for Oracle AQ.
 *
 * @author Erik Wramner
 */
public class AqJmsConsumer extends JmsConsumer<AqConsumerConfiguration> {

    /**
     * Program entry point.
     *
     * @param args Command line.
     */
    public static void main(String[] args) {
        new AqJmsConsumer().run(args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AqConsumerConfiguration createConfiguration() {
        return new AqConsumerConfiguration();
    }

    /**
     * AQ-specific JMS consumer configuration.
     */
    public static class AqConsumerConfiguration extends JmsConsumerConfiguration {
        @Option(name = "-url", aliases = { "--aq-jdbc-url" }, usage = "JDBC URL for AQ database connection", required = true)
        private String _aqJdbcUrl;

        @Option(name = "-user", aliases = { "--aq-jdbc-user" }, usage = "JDBC user for AQ database connection", required = true)
        private String _aqJdbcUser;

        @Option(name = "-pw", aliases = { "--aq-jdbc-password" }, usage = "JDBC password for AQ database connection", required = true)
        private String _aqJdbcPassword;

        @Override
        public ConnectionFactory createConnectionFactory() throws JMSException {
            return AqJmsUtils.createConnectionFactory(_aqJdbcUrl, _aqJdbcUser, _aqJdbcPassword);
        }

        @Override
        public XAConnectionFactory createXAConnectionFactory() throws JMSException {
            return AqJmsUtils.createXAConnectionFactory(_aqJdbcUrl, _aqJdbcUser, _aqJdbcPassword);
        }

        @Override
        public ObjectMessageAdapter getObjectMessageAdapter() {
            return new AqObjectMessageAdapter();
        }
    }
}
