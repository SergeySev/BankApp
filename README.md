# BankApp

## Instructions to Start the Application
This repository contains a Bank Application that can be easily run using Docker. Follow these steps to get started:

Prerequisites
Before starting, make sure you have the following software installed on your machine:

Git
Docker

### Installation
1. Clone the repository by running the following command in your terminal:
### `git clone https://github.com/SergeySev/BankAppDocker.git`

2. Launch Docker on your machine.

3. Open the terminal and navigate to the directory where you cloned the project

4. Run the project using the following command: 
### `docker compose up`

5. Wait for the download to complete. This will take some time, as the required Docker images need to be downloaded and the containers need to be started.

6. Once the containers are up and running, you can access the Bank Application front-end in your browser by going to http://localhost:3000/.

7. You can simulate sending emails to the client by going to http://0.0.0.0:1080/#/. If you see an empty page, try going to http://localhost:1080/#/ instead.

8. Finally, you can access the Swagger UI to explore the API endpoints of the Java application by going to http://localhost:8080/swagger-ui/index.html#/.

That's it! You now have the Bank Application up and running on your machine.
