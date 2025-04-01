## URL Base da API

```
http://localhost:8080/api
```

## Autenticação

Muitos dos endpoits exigen autenticação com token JWT. Inclua um token no header de Authorization:

```
Authorization: Bearer {seu-jwt-token}
```

## API Endpoints

### Autenteicação

#### Login

Autentica um usuário e retorna um token JWT.

- **URL**: `/auth/login`
- **Method**: `POST`
- **Autenticação**: Não é exigido
- **Request Body**:
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```
- **Response**: `200 OK`
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "user@example.com",
    "expirationTime": "2025-04-01T15:30:00"
  }
  ```

### Users

#### Cadastrar Usuário

Cria uma nova conta de usuário e automaticamente realiza o login (retorna um token JWT).

- **URL**: `/users/register`
- **Method**: `POST`
- **Autenticação**: Não é exigido
- **Request Body**:
  ```json
  {
    "firstName": "John",
    "lastName": "Doe",
    "email": "user@example.com",
    "password": "password123"
  }
  ```
- **Response**: `201 Created`
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "user@example.com",
    "expirationTime": "2025-04-01T15:30:00"
  }
  ```

#### Listar todos usuários

Retorna todos os usuários cadastrados

- **URL**: `/users`
- **Method**: `GET`
- **Autenticação**: Exigida
- **Response**: `200 OK`
  ```json
  [
    {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe",
      "email": "john@example.com",
      "token": null,
      "expirationTime": null
    },
    {
      "id": 2,
      "firstName": "Jane",
      "lastName": "Smith",
      "email": "jane@example.com",
      "token": null,
      "expirationTime": null
    }
  ]
  ```

#### Buscar usuário por ID

Retorna um usuário específico

- **URL**: `/users/{id}`
- **Method**: `GET`
- **Autenticação**: Exigida.
- **URL Parameters**: `id=[long]` - User ID
- **Response**: `200 OK`
  ```json
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "token": null,
    "expirationTime": null
  }
  ```

#### Atualizar Usuário

Atualiza as informações de um usuário já cadastrado

- **URL**: `/users/{id}`
- **Method**: `PATCH`
- **Autenticação**: Exigido
- **URL Parameters**: `id=[long]` - User ID
- **Request Body** (todos os campos são opcionais):
  ```json
  {
    "firstName": "John",
    "lastName": "Updated",
    "email": "updated@example.com",
    "password": "newpassword123"
  }
  ```
- **Response**: `200 OK`
  ```json
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Updated",
    "email": "updated@example.com",
    "token": null,
    "expirationTime": null
  }
  ```

#### Apagar Usuário

Apaga a conta de um usuário.

- **URL**: `/users/{id}`
- **Method**: `DELETE`
- **Autenticação**: Exigida
- **URL Parameters**: `id=[long]` - User ID
- **Response**: `204 No Content`

### Tasks

#### Lista todas as tarefas

Retorna uma lista com todas as tarefas

- **URL**: `/tasks`
- **Method**: `GET`
- **Autenticação**: Exigida
- **Response**: `200 OK`
  ```json
  [
    {
      "id": 1,
      "title": "Implement Authentication",
      "description": "Add JWT authentication to the API",
      "priority": "HIGH",
      "assignee": {
        "id": 1,
        "firstName": "John",
        "lastName": "Doe",
        "email": "john@example.com",
        "token": null,
        "expirationTime": null
      },
      "deadline": "2025-04-10T23:59:59",
      "completed": false,
      "createdAt": "2025-03-31T10:15:00",
      "updatedAt": "2025-03-31T10:15:00"
    }
  ]
  ```

#### Procurar tarefa específica

Busca por uma tarefa específica com base no id

- **URL**: `/tasks/{id}`
- **Method**: `GET`
- **Autenticação**: Exigido
- **URL Parameters**: `id=[long]` - Task ID
- **Response**: `200 OK`
  ```json
  {
    "id": 1,
    "title": "Implement Authentication",
    "description": "Add JWT authentication to the API",
    "priority": "HIGH",
    "assignee": {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe",
      "email": "john@example.com",
      "token": null,
      "expirationTime": null
    },
    "deadline": "2025-04-10T23:59:59",
    "completed": false,
    "createdAt": "2025-03-31T10:15:00",
    "updatedAt": "2025-03-31T10:15:00"
  }
  ```

#### Criar nova tarefa

Cria uma nova tarefa

- **URL**: `/tasks`
- **Method**: `POST`
- **Autenticação**: Exigida
- **Request Body**:
  ```json
  {
    "title": "Implement Documentation",
    "description": "Create API documentation for the project",
    "priority": "MEDIUM",
    "assignee": 1,
    "deadline": "2025-04-15"
  }
  ```
  
  Nota: Priority deve ser um do seguintes valores: "LOW", "MEDIUM", or "HIGH".
  
- **Response**: `201 Created`
  ```json
  {
    "id": 2,
    "title": "Implement Documentation",
    "description": "Create API documentation for the project",
    "priority": "MEDIUM",
    "assignee": {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe",
      "email": "john@example.com",
      "token": null,
      "expirationTime": null
    },
    "deadline": "2025-04-15T23:59:59",
    "completed": false,
    "createdAt": "2025-03-31T15:45:00",
    "updatedAt": "2025-03-31T15:45:00"
  }
  ```

#### Atualizar tarefa

Atualiza uma tarefa já existente

- **URL**: `/tasks/{id}`
- **Method**: `PATCH`
- **Autenticação**: Exigido
- **URL Parameters**: `id=[long]` - Task ID
- **Request Body** (todos os campos são opcionais):
  ```json
  {
    "title": "Updated Task Title",
    "description": "Updated description for the task",
    "priority": "HIGH",
    "assignee": 2,
    "deadline": "2025-04-20",
    "completed": true
  }
  ```
- **Response**: `200 OK`
  ```json
  {
    "id": 2,
    "title": "Updated Task Title",
    "description": "Updated description for the task",
    "priority": "HIGH",
    "assignee": {
      "id": 2,
      "firstName": "Jane",
      "lastName": "Smith",
      "email": "jane@example.com",
      "token": null,
      "expirationTime": null
    },
    "deadline": "2025-04-20T23:59:59",
    "completed": true,
    "createdAt": "2025-03-31T15:45:00",
    "updatedAt": "2025-03-31T16:30:00"
  }
  ```

#### Apagar tarefa

Apaga uma tarefa

- **URL**: `/tasks/{id}`
- **Method**: `DELETE`
- **Autenticação**: Exigida
- **URL Parameters**: `id=[long]` - Task ID
- **Response**: `204 No Content`

## Error Responses

Todos os endpoints podem retornar as seguintes mensagens de erro:

### 400 Bad Request
Quando uma requisição é enviada de forma errada ou a validação falha.

```json
{
  "timestamp": "2025-03-31T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/tasks"
}
```

### 401 Unauthorized
Quando a autenticação falha ou o token está inválido/expirado

```json
{
  "timestamp": "2025-03-31T12:00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Token expirado ou inválido",
  "path": "/api/tasks"
}
```

### 404 Not Found
Quando um recurso específico não existe.

```json
{
  "timestamp": "2025-03-31T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Tarefa não encontrada",
  "path": "/api/tasks/999"
}
```

### 500 Internal Server Error
Quando ocorre um erro inesperado no servidor.

```json
{
  "timestamp": "2025-03-31T12:00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Erro interno do servidor",
  "path": "/api/tasks"
}
```
