# spring-boot-email
Spring Boot – Sending Email via SMTP

Spring Boot provides the ability to send emails via SMTP using the JavaMail Library. Here we will be illustrating step-by-step guidelines to develop Restful web services that can be used to send emails with or without attachments. In order to begin with the steps, let us first create a Spring Boot project using Spring Initializer.

What you'll learn
Setting up Spring Boot project.
Configuring SMTP server.
Sending Plain Email only message.
Email with message and attachments.
Email with message, inline images and attachments.
How to develop Body of email.
Send email from Spring Boot Application to Outlook
How Spring Boot Mail API works.
Understand the Java mail API fundamentals
Use HTML, CSS to create, design email


Implementation
Step 1: Create a Spring Boot Web Application using Spring Initializer 

Step 2: Adding the spring-boot-starter-mail dependency in pom.xml.

        <dependency>
            <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>
                        
Step 3: Configure SMTP server detail in Application.properties.

                            
        ########## SMTP configuration to send out emails ##########
        ####### Make sure to use the correct SMTP configurations #######
        spring.mail.host=mail.madarasa.com.in
        spring.mail.port=465
        spring.mail.username=support@madarasa.com.in
        spring.mail.password=yourmailpassword
        
        # Other properties
        spring.mail.properties.mail.smtp.auth=true
        spring.mail.properties.mail.smtp.connectiontimeout=5000
        spring.mail.properties.mail.smtp.timeout=5000
        spring.mail.properties.mail.smtp.writetimeout=5000
        
        # TLS , port 587
        spring.mail.properties.mail.smtp.starttls.enable=true
        
        # SSL, post 465
        spring.mail.properties.mail.smtp.socketFactory.port = 465
        spring.mail.properties.mail.smtp.socketFactory.class = javax.net.ssl.SSLSocketFactory


