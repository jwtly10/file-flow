# FileFlow: Kafka-Powered File Processing Service

A versatile microservice-based system that allows uploading, processing and storing files efficiently,
while leveraging Kafka for event-driven communication. This project is designed to streamline file handling for
various personal projects and work tools, eliminating the need to implement authentication and file storage services
repeatedly.

Very easily extendable to implement custom file processing, built as a means to have more experience with Kafka.

## Sections

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Setup](#setup)
- [Usage](#usage)

## Features

- **Client Service:** The Client Service provides a user-friendly REST API for uploading and downloading files. It
  simplifies the process of interacting with the system.
- **Processor Service:** The Processor Service is responsible for any necessary file processing tasks. This enables
  customization for specific requirements.
- **Storage Service:** The Storage Service stores files securely in a third-party service Supabase, ensuring robust
  data management.
- **Event-Driven Communication:** Kafka events facilitate seamless communication among the microservices, ensuring
  efficient data flow.
- **Centralized File Management:** Use this project as a centralized solution for handling files across various personal
  and work-related projects. No need to rewrite authentication or file storage; you can simply integrate
  these APIs.
- **Spring Security:** With Spring Security implemented using JSON Web Tokens (JWT), data is only accessible to
  authenticated users/clients.
- **Multithreaded File Processing:** FileFlow uses multithreaded processing of files, ensuring efficient and
  high-throughput file handling. Paired with Kafka, this allows for a very responsive user experience.

## Technologies Used

- **Spring Boot**
- **Spring Security**
- **Supabase**
- **Kafka**
- **Rest API System Design**

## Setup

To run this project locally, follow these steps:

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/jwtly10/file-flow.git
   ```

#### Configure the Environment:

1. Set up your Java environment.
    - Install Spring Boot and Spring Security if not already installed.
    - Ensure you have access to a Kafka server and configure it accordingly.
    - Set environment variables as documented in the `example-application.properties` files.

2. Run Microservices:
    - Start the Client Service, Processor Service, and Storage Service.
    - Verify that Kafka is running and properly configured.

3. Use the APIs:
    - Explore the REST APIs provided by the Client Service for file upload and download.
    - Customize the Processor Service to handle specific processing tasks.
    - Files will be securely stored using the Storage Service in Supabase.

## Usage

### Uploading a file using cURL

Upload a file using the Client Service's REST API. FileFlow will save this file against the author of the request. This
is done via the JWT token provided in the request header.

The unique name of the file will be returned. So this should be stored for future reference.

```bash
# Example cURL commands for uploading files
curl -X POST -F "file=@/path/to/your/file.png" /api/v1/upload
curl -X POST -F "file=@/path/to/your/file.log" /api/v1/upload

# Example response:
{
  "message": "2cfbd796-a035-4c8c-9499-f56235140c2e.png",
}
```

### Downloading a file using cURL

Provided a valid JWT token is given, the Client Service will return the requested file. The returned filename will be
the _original_, not the unique name.

{username} should be replaced with the username of the user who uploaded the file.

```bash
# Example cURL commands for downloading files
curl -o output/ -L /api/v1/download/{username}/2cfbd796-a035-4c8c-9499-f56235140c2e.png
```


