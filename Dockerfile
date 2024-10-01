FROM tomcat:jre21

COPY /target/senla_homework_project.war /usr/local/tomcat/webapps/ROOT.war
COPY /tomcat/logging.properties /usr/local/tomcat/conf/logging.properties

RUN ["/usr/local/tomcat/bin/catalina.sh", "start"]