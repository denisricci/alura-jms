# alura-jms

Para ativar a prioridade no Active MQ é necessário seguir o procedimento abaixo:</br>

No arquivo activemq.xml logo abaixo da tag <policyEntries> adicione:

<policyEntry queue=">" prioritizedMessages="true"/>