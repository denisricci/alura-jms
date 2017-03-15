package br.com.alura.jms;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TesteProdutorTopic {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws NamingException, JMSException {

		/**************************************************************************
		 * é possível utilizar o arquivo ou a classe Properties, para utilizar o
		 * arquivo basta criar na pasta src o arquivo jndi.properties
		 ***************************************************************************/

		// Properties properties = new Properties();
		// properties.setProperty("java.naming.factory.initial",
		// "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		//
		// properties.setProperty("java.naming.provider.url",
		// "tcp://localhost:61616");
		// properties.setProperty("queue.financeiro", "fila.financeiro");

		// InitialContext initialContext = new InitialContext(properties);

		InitialContext initialContext = new InitialContext();
		ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
		Connection connection = connectionFactory.createConnection();
		connection.start();

		Destination destination = (Destination) initialContext.lookup("loja");

		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageProducer producer = session.createProducer(destination);

		TextMessage textMessage = session.createTextMessage("Mensagem de topico");
		producer.send(textMessage);

		// new Scanner(System.in).nextLine();

		connection.close();
		initialContext.close();
		session.close();
	}

}
