# DevConnect

Developer Q&A Platform with AI Integration - A hackathon project that helps developers share code and get AI-powered assistance.

## Features

- Post questions with code blocks and descriptions
- Syntax highlighting for multiple languages
- AI-powered code analysis and suggestions
- Reaction system for posts and comments
- No authentication required - just use an alias

## Tech Stack

### Backend
- Java 21
- Spring Boot 3.2.x
- H2 Database (in-memory)
- Azure OpenAI for AI responses
- OpenAPI/Swagger for documentation

### Frontend
- Android (Kotlin)
- Jetpack Compose with Material3
- Retrofit2 for API calls
- Coroutines & Flow
- JetBrains Mono font for code

## Setup

### Prerequisites
- JDK 21
- Maven 3.9+
- Android Studio (latest)
- Android SDK 34
- Kotlin 1.9+

### Environment Variables
Create a `.env` file in the backend root with:
```
AZURE_OPENAI_KEY=your_key_here
AZURE_OPENAI_ENDPOINT=your_endpoint_here
AZURE_OPENAI_DEPLOYMENT=your_deployment_name_here
SERVER_PORT=8080
```

### Running the Backend
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The server will start at `http://localhost:8080` with:
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console
- API Docs: http://localhost:8080/v3/api-docs

### Running the Frontend
1. Open the `android` directory in Android Studio
2. Update `local.properties` with:
   ```properties
   api.base.url=http://10.0.2.2:8080
   ```
3. Run the app on an emulator or device

## API Documentation

### Key Endpoints
- Posts: `/api/posts`
  - GET / - List all posts (paginated)
  - GET /{id} - Get single post
  - POST / - Create post
  - PUT /{id} - Update post
  - DELETE /{id} - Delete post

- Comments: `/api/posts/{postId}/comments`
  - GET / - List all comments
  - POST / - Create comment
  - POST /ai - Generate AI comment
  - PUT /{id} - Update comment
  - DELETE /{id} - Delete comment

### Pagination
- Default page size: 20 items
- Sort options: Latest, Most Liked

### Search Filters
- Text query
- Programming language
- Has AI comments
- Date range

## Project Structure

### Backend
```
src/main/java/com/devconnect/
├── controllers/    # REST endpoints
├── models/        # Domain entities
├── services/      # Business logic
├── repositories/  # Data access
└── config/        # App configuration
```

### Frontend
```
app/src/main/kotlin/com/devconnect/
├── ui/           # Compose UI components
├── data/         # Data layer
├── network/      # API client
└── util/         # Utilities