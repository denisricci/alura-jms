package br.com.alura.jms;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JmsBuilder {

	private InitialContext context;
	private Connection connection;
	private Destination destination;
	private Session session;

	public JmsBuilder() throws NamingException {
		this.context = new InitialContext();
	}

	public JmsBuilder(Properties properties) throws NamingException {
		this.context = new InitialContext();
	}

	public JmsBuilder newConnection() throws NamingException, JMSException {
		ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
		this.connection = connectionFactory.createConnection();
		return this;
	}

	public JmsBuilder from(String destinationName) throws NamingException {
		this.destination = (Destination) context.lookup("financeiro");
		return this;
	}

	public JmsBuilder withAcknowledgeStrategy(int acknowledgeStrategy) throws JMSException {
		createSession((acknowledgeStrategy == Session.SESSION_TRANSACTED), acknowledgeStrategy);
		return this;
	}

	public QueueBrowser buildQueueBrowser() throws JMSException {
		if (session == null)
			createDefaultSession();
		return session.createBrowser((Queue) destination);
	}

	public MessageConsumer buildMessageConsumer(MessageListener messageListener) throws JMSException {
		if (session == null)
			createDefaultSession();
		MessageConsumer consumer = session.createConsumer(destination);
		consumer.setMessageListener(messageListener);
		return consumer;
	}
	
	public MessageProducer buildMessageProducer() throws JMSException{
		return session.createProducer(destination);
	}
	
	public TopicSubscriber buildTopicSubscriber(String subscriberName) throws JMSException{
		return session.createDurableSubscriber((Topic)destination, subscriberName);
	}

	public JmsBuilder withClientId(String clientID) throws JMSException {
		this.connection.setClientID(clientID);
		return this;
	}

	public void closeAll() throws JMSException, NamingException {
		this.connection.close();
		this.context.close();
		this.session.close();
	}

	private void createSession(boolean transacted, int acknowledgeStrategy) throws JMSException {
		this.connection.start();
		this.connection.createSession(transacted, acknowledgeStrategy);
	}

	private void createDefaultSession() throws JMSException {
		this.connection.start();
		this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

}
