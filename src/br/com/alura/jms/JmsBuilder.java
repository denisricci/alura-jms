package br.com.alura.jms;

import java.io.Serializable;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 * 
 * Classe builder responsável por criar os recursos necessários para realizar conexao
 * com um MOM (Message Oriented Middleware)
 * 
 * @author Denis Ricci
 *
 */



public class JmsBuilder {
	
	/**
	* Context para localizar recursos da JNDI
	*/
	private InitialContext context;
	
	/**
	* Conexao para um MOM (Message Oriented Middleware)
	*/
	private Connection connection;
	
	/**
	* Variavel que aponta para uma fila ou topico
	*/
	private Destination destination;
	
	/**
	* Sessao com uma filaMQ
	*/
	private Session session;

	/**
	* Inicializa a classe builder utilizando as informacoes do
	* arquivo jndi.properties localizado no classpath
	*/
	public JmsBuilder() throws NamingException {
		this.context = new InitialContext();
	}
	/**
	* Inicializa a classe builder utilizando as informacoes contidas no 
	* Properties passado por prametro
	* 
	* @param properties - Propriedade contendo as informacoes de filas e topicos
	*/
	public JmsBuilder(Properties properties) throws NamingException {
		this.context = new InitialContext(properties);
	}

	/**
	* Cria uma nova conexao com a fila MQ, 
	* o nome do recurso ConnectionFactory pode variar de acordo com o fabricante.
	* 
	* @return JmsBuilder
	*/
	public JmsBuilder newConnection() throws NamingException, JMSException {
		ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
		this.connection = connectionFactory.createConnection();
		return this;
	}
	
	/**
	* Define o nome da Fila ou Topico
	* 
	* @param destinationName - Nome da fila ou topico
	* 
	* @return JmsBuilder
	*/

	public JmsBuilder from(String destinationName) throws NamingException {
		this.destination = (Destination) context.lookup(destinationName);
		return this;
	}
	
	/**
	* Define a estratégia de feedback para o MOM (Message Oriented Middleware)
	* 
	* @param acknowledgeStrategy - Estrategia de feedback desejada 
	* 
	* @return JmsBuilder
	*/

	public JmsBuilder withAcknowledgeStrategy(int acknowledgeStrategy) throws JMSException {
		createSession((acknowledgeStrategy == Session.SESSION_TRANSACTED), acknowledgeStrategy);
		return this;
	}
	
	/**
	* Cria uma QueueBrowser que pode consultar as mensagens da fila sem consumi-la
	* 
	* 
	* @return QueueBrowser
	*/

	public QueueBrowser buildQueueBrowser() throws JMSException {
		if (session == null)
			createDefaultSession();
		return session.createBrowser((Queue) destination);
	}
	
	/**
	* Cria um consumidor de mensagens comumente utilizado para filas
	* 
	* @param messageListener - Listener que será notificado quando uma nova mensagem for recebida
	* 
	* @return MessageConsumer
	*/

	public MessageConsumer buildMessageConsumer(MessageListener messageListener) throws JMSException {
		if (session == null)
			createDefaultSession();
		MessageConsumer consumer = session.createConsumer(destination);
		consumer.setMessageListener(messageListener);
		return consumer;
	}
	
	/**
	* Cria um produtor de mensagens
	* 
	* 
	* @return MessageProducer
	*/

	public MessageProducer buildMessageProducer() throws JMSException {
		if (session == null)
			createDefaultSession();
		return session.createProducer(destination);
	}
	
	/**
	* Cria um subscriber para receber mensagens de um topico
	* 
	* @param subscriberName - Nome do subscriber, utilizado para o MOM identificar cada consumidor do topico
	* 
	* @return TopicSubscriber - objeto que é utilizado para receber mensagens de um topico
	*/

	public TopicSubscriber buildTopicSubscriber(String subscriberName) throws JMSException {
		return buildTopicSubscriber(subscriberName, null);
	}
	
	/**
	* Cria um subscriber para receber mensagens de um topico
	* 
	* @param subscriberName - Nome do subscriber, utilizado para o MOM identificar cada consumidor do topico
	* @param messageListener - Listener que será notificado sempre que o topico receber uma nova mensagem
	* 
	* @return TopicSubscriber - objeto que é utilizado para receber mensagens de um topico
	*/

	public TopicSubscriber buildTopicSubscriber(String subscriberName, MessageListener messageListener)
			throws JMSException {
		if (session == null)
			createDefaultSession();
		TopicSubscriber topicSubscriber = session.createDurableSubscriber((Topic) destination, subscriberName);
		if (messageListener != null)
			topicSubscriber.setMessageListener(messageListener);
		return topicSubscriber;
	}
	
	/**
	* Cria um subscriber para receber mensagens de um topico
	* 
	* @param clientID - define o clintID para identificar o consumidor
	* 
	* @return JmsBuilder
	*/

	public JmsBuilder withClientId(String clientID) throws JMSException {
		this.connection.setClientID(clientID);
		return this;
	}
	
	/**
	* 
	* Fecha todos os recursos que foram abertos para criar a conexao com o MOM
	* 
	*/

	public void closeAll() throws JMSException, NamingException {
		this.connection.close();
		this.context.close();
		this.session.close();
	}
	
	/**
	* Cria uma nova mensagem de texto que pode ser enviada com o auxilio de um MOM producer
	* 
	* @param message - Corpo da mensagem
	* 
	* @return TextMessage
	*/

	public TextMessage createTextMessage(String message) throws JMSException {
		return this.session.createTextMessage(message);
	}
	
	/**
	* Cria um novo ObjectMessage que pode ser enviada com o auxilio de um MOM producer
	* 
	* @param message - Objeto serializavel
	* 
	* @return ObjectMessage
	*/
	public ObjectMessage createObjectMessage(Serializable obj) throws JMSException {
		return this.session.createObjectMessage(obj);
	}
	
	/**
	* 
	* Cria uma nova sessao com o MOM
	* 
	* @param transacted - se a sessao utilizara transacao
	* @param acknowledgeStrategy - estrategia de feedback
	*/
	private void createSession(boolean transacted, int acknowledgeStrategy) throws JMSException {
		this.connection.start();
		this.session = this.connection.createSession(transacted, acknowledgeStrategy);
	}
	
	/**
	* 
	* Cria uma nova sessao com o MOM
	* 
	*/
	private void createDefaultSession() throws JMSException {
		this.connection.start();
		this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

}
