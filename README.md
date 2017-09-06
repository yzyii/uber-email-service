# Email Service

> An email service based on Uber's Coding Challenges on [GitHub.](https://github.com/uber/coding-challenge-tools/blob/master/coding_challenge.md)

The service contains a frontend and backend that accepts the necessary information and sends emails. email attributes implemented include:
* To, CC, BCC - Lists of email addresses who will receive the email or a copy.
* From - Email address of the sender
* Subject - Subject of the email in plaintext.
* Contents - Contents of the email in plaintext.

The application provides an abstraction between two different email service providers and can quickly failover to a different provider without affecting availability.

The two providers implemented are:
* [Mailgun](https://sendgrid.com/)
* [SendGrid](https://sendgrid.com/)

## Backend

RabbitMQ is used to provide naive round-robin load balancing between the two servers and automatic fail-over via re-queueing (I know you're not a fan of RabbitMQ, Stefano, but I had it on hand from an older project :P).

The backend interacts with the front end via a REST API endpoint served at: /api/email.

## Frontend

The frontend is simple single web page using vue.js and the vue-stash plugin. 
Source files are located in: src/main/web
The frontend is compiled to static resources via npm and can be built using the npm task: npm run build.

## Build Setup

To run, either use the provided gradle wrapper and bootRun task on the command line with: ./gradlew bootRun
Or launch the application from the Application.java main method.

By default, the server will run on port 8080. External configuration for Mailgun and SendGrid API keys and logging can be found in src/main/resources/application.properties or overridden as required.

To build a runnable JAR file, use: ./gradlew build. 
You can then run the JAR file with: java -jar build/libs/email-service-0.1.0.jar