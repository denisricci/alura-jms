package br.com.alura.jms;

import java.util.Enumeration;
import java.util.Scanner;

import javax.jms.JMSException;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;

/**
 * 
 * Classe que exemplifica um simples consumidor de uma fila MQ, 
 * o nome financeiro está registrado no arquivo jndi.properties
 * que se encontra na pasta src do projeto.
 * 
 * 
 * @author Denis Ricci
 *
 */


public class SimpleQueueConsumer {
		
	@SuppressWarnings({ "resource", "rawtypes" })
	public static void main(String[] args) throws NamingException, JMSException {
		
		JmsBuilder builder = new JmsBuilder();
		
		//consulta as filas mas não remove a mensagem da fila
		QueueBrowser queueBrowser = builder.newConnection().from("financeiro").withAcknowledgeStrategy(Session.AUTO_ACKNOWLEDGE).buildQueueBrowser();
		Enumeration enumeration = queueBrowser.getEnumeration();
		
		while(enumeration.hasMoreElements()){
			TextMessage text = (TextMessage)enumeration.nextElement();
			System.out.println("Consultando a mensagem: " + text.getText());
		}
		
		builder.buildMessageConsumer(m->{
			TextMessage text = (TextMessage)m;
			try {
				System.out.println("Mensagem recebida: " + text.getText());
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		//utilizado apenas para o programa continar executando.
		new Scanner(System.in).nextLine();
		
		builder.closeAll();
	}

}
