---
mode: agent
---

Analyze all the endpoints for ${input:serviceName} service and and generate comprehensive API documentation.

Ensure you ask the user what service they want to generate documentation for.

# API Documentation Output

Analyze and think step by step (using Sequential Thinking MCP Server) all the API endpoints for ${input:serviceName} and create detailed API documentation for the ${input:serviceName} service, including endpoint descriptions, request and response formats, and example usage.

## Service: ${input:serviceName}
## Endpoints
- **GET** `/api/${input:serviceName}/endpoint1`: Description of endpoint1
- **POST** `/api/${input:serviceName}/endpoint2`: Description of endpoint2
- **PUT** `/api/${input:serviceName}/endpoint3`: Description of endpoint3
- **DELETE** `/api/${input:serviceName}/endpoint4`: Description of endpoint4

Format the documentation in Markdown for easy readability and integration into documentation.

Ensure to include:
- Endpoint paths
- HTTP methods
- Request parameters
- Response formats
- Example requests and responses

Put the API documentation in a file named `api-documentation-${input:serviceName}-${yyyyMMdd-HHmmss}.md` in the root directory of the service under `./docs/api-docs/`
