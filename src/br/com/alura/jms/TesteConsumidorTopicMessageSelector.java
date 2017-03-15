package br.com.alura.jms;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TesteConsumidorTopicMessageSelector {
		
	@SuppressWarnings("resource")
	public static void main(String[] args) throws NamingException, JMSException {
		
		/**************************************************************************
			é possível utilizar o arquivo ou a classe Properties, 
			para utilizar o arquivo basta criar na pasta src o arquivo jndi.properties
		***************************************************************************/
		
//		Properties properties = new Properties();
//		properties.setProperty("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
//
//		properties.setProperty("java.naming.provider.url", "tcp://localhost:61616");
//		properties.setProperty("queue.financeiro", "fila.financeiro");		
		
//		InitialContext initialContext = new InitialContext(properties);
		
		InitialContext initialContext = new InitialContext();
		ConnectionFactory connectionFactory = (ConnectionFactory)initialContext.lookup("ConnectionFactory");
		Connection connection = connectionFactory.createConnection();
		connection.setClientID("Estoque");
		connection.start();
				
		Destination destination = (Destination) initialContext.lookup("loja");
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
//		Busca Mensagem mas na consome a mesma		
//		QueueBrowser browser = session.createBrowser((Queue)destination);
//		Enumeration enumeration = browser.getEnumeration();
//		
//		while(enumeration.hasMoreElements()){
//			TextMessage text = (TextMessage)enumeration.nextElement();
//			System.out.println("Browser queue: " + text.getText());
//		}
//								
		TopicSubscriber consumer = session.createDurableSubscriber((Topic)destination, "loja","ebook is null OR ebook=true", false);		
		
		consumer.setMessageListener(m->{
			TextMessage text = (TextMessage)m;
			try {
				System.out.println("Mensagem recebida " + text.getText());
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});				
		
		new Scanner(System.in).nextLine();
		
		connection.close();
		initialContext.close();
		session.close();
	}

}
