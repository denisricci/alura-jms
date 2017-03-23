package br.com.alura.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MessageSelectorTopicProducer {

	public static void main(String[] args) throws NamingException, JMSException {

		InitialContext initialContext = new InitialContext();
		ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
		Connection connection = connectionFactory.createConnection();
		connection.start();

		Destination destination = (Destination) initialContext.lookup("loja");

		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageProducer producer = session.createProducer(destination);

		TextMessage textMessage = session.createTextMessage("Mensagem de topico");
		textMessage.setBooleanProperty("ebook", true);
		producer.send(textMessage);

		// new Scanner(System.in).nextLine();

		connection.close();
		initialContext.close();
		session.close();
	}

}
