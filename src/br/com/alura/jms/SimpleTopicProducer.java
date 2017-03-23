package br.com.alura.jms;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.naming.NamingException;

import br.com.alura.jms.model.Pedido;
import br.com.alura.jms.model.PedidoFactory;

/**
 * 
 * Classe que exemplifica um simples produtor de mensagens para um topico MQ
 * Nessa classe, ao invés de utilizar o arquivo jndi.properties é criado uma nova
 * instancia de Properties para exemplificar essa segunda forma de registrar os nomes
 * 
 * @author Denis Ricci
 *
 */

public class SimpleTopicProducer {

	public static void main(String[] args) throws NamingException, JMSException {
				
		Properties properties = new Properties();
		properties.setProperty("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");

		properties.setProperty("java.naming.provider.url", "tcp://localhost:61616");
		properties.setProperty("topic.loja", "topic.loja");						

		JmsBuilder builder = new JmsBuilder(properties);

		MessageProducer producer = builder.newConnection().from("loja").buildMessageProducer();
				
		Pedido pedido = new PedidoFactory().geraPedidoComValores();
		
//		enviando TextMessage
		
//		StringWriter writer = new StringWriter();
//		JAXB.marshal(pedido, writer);
//		String xml = writer.toString();		
//		TextMessage textMessage = builder.createTextMessage(xml);
//		producer.send(textMessage);		
						
		Message message = builder.createObjectMessage(pedido);
		producer.send(message);

		builder.closeAll();
	}

}
