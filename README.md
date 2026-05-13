# AI-Powered Support Ticket System

A backend REST API system for managing customer support tickets.  
The system integrates Gemini API to classify ticket categories, detect priority levels, and generate automated response suggestions.

## Features

- Create support tickets
- View all tickets
- View ticket details by ID
- Update ticket status
- Delete tickets
- AI-powered ticket classification
- Priority detection
- Automated response suggestions
- Swagger API documentation

## Tech Stack

- Java
- Spring Boot
- Spring MVC
- Spring Data JPA
- MySQL
- Gemini API
- Swagger UI
- Maven

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/tickets` | Create a new ticket |
| GET | `/api/tickets` | Get all tickets |
| GET | `/api/tickets/{id}` | Get ticket by ID |
| PUT | `/api/tickets/{id}/status` | Update ticket status |
| DELETE | `/api/tickets/{id}` | Delete ticket |

## Example Request

```json
{
  "title": "Payment failed",
  "description": "Money was deducted but transaction failed"
}
