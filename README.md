# 🧑‍💻 TeamManager - Spring Boot Backend

**TeamManager** is a robust backend system built using **Spring Boot**, designed to facilitate seamless project and task management for teams. It supports role-based access control (e.g., Project Manager, Team Member), JWT authentication, and CRUD operations for users, projects, and tasks. It exposes a clean RESTful API that can be integrated with frontend applications.

---

## 🧩 Features

### ✅ **Authentication & Authorization**

* JWT-based authentication
* Role-based access control
* Secure login and signup endpoints
* Role management endpoints

### ✅ **User Management**

* Register, fetch, and delete users
* Assign and update roles
* View user details (by ID or username)

### ✅ **Project Management**

* Create, update, delete projects
* Assign project managers
* Get projects by manager or paginated list

### ✅ **Task Management**

* Create, view, update, delete tasks
* Assign tasks to users
* Filter tasks by project, user, status, etc.
* Paginated responses for large task lists

### ✅ **Dashboard APIs**

* View overall system statistics
* Fetch recent projects and tasks

---

## 🛠️ Tech Stack

| Technology          | Description                             |
| ------------------- | --------------------------------------- |
| **Java 17**         | Core programming language               |
| **Spring Boot**     | Framework for building RESTful services |
| **Spring Security** | Authentication and authorization        |
| **JWT**             | Stateless token-based authentication    |
| **Hibernate / JPA** | ORM for database interaction            |
| **PostgreSQL**      | Relational database                     |
| **Lombok**          | Reduce boilerplate code                 |
| **Maven**           | Dependency management                   |
| **Spring Data JPA** | Simplified DB access layer              |

---

## 📁 Project Structure

```
com.task.TeamManager
├── controller        # REST Controllers (auth, projects, tasks, users, dashboard)
├── dto              # DTOs for transferring data (ProjectDTO, TaskDTO, etc.)
├── model            # Entity classes (Users, Projects, Tasks, etc.)
├── repository       # Spring Data JPA repositories
├── security         # JWT filters, UserDetail service
├── service          # Business logic layer
├── config           # Security & CORS config (if exists)
└── application.properties / yml
```

---

## 🔐 Authentication & Roles

### Roles

* `ROLE_PROJECT_MANAGER`
* `ROLE_TEAM_MEMBER`

### Sample JWT Login Flow

```http
POST /api/auth/login
{
  "username": "john_doe",
  "password": "password123"
}
Response: {
  "token": "Bearer eyJhbGciOiJIUzI1NiIsInR..."
}
```

> Add header for secured requests:
> `Authorization: Bearer <jwt-token>`

#### 🔐 Authentication Screenshots

| Sign Up                                                                                    | JWT Login                                                                                 |
| ------------------------------------------------------------------------------------------ | ----------------------------------------------------------------------------------------- |
| ![signup](https://github.com/user-attachments/assets/fa230a6f-8551-44c1-98ff-da3008e154c6) | ![login](https://github.com/user-attachments/assets/7f9ab387-24a5-4dab-a6ce-c13abdab0f93) |

---

## 👤 User APIs

| Method | Endpoint                         | Description          |
| ------ | -------------------------------- | -------------------- |
| GET    | `/api/users`                     | Get all users        |
| GET    | `/api/users/{id}`                | Get user by ID       |
| DELETE | `/api/users/{id}`                | Delete user          |
| POST   | `/api/users/{id}/roles`          | Update user roles    |
| POST   | `/api/users/{id}/add-role`       | Add role by ID       |
| POST   | `/api/users/{username}/add-role` | Add role by username |

#### 👤 User API Screenshots

| Get All Users                                                                                   | Get User By ID                                                                                  |
| ----------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------- |
| ![getAllUsers](https://github.com/user-attachments/assets/3e9f00e3-c35b-4f0b-bfdc-7d109352342f) | ![getUserById](https://github.com/user-attachments/assets/469864c8-ffed-4303-a73f-c539b1c6ccaa) |

| Delete User                                                                                    | Update Role                                                                                    |
| ---------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------- |
| ![deleteUser](https://github.com/user-attachments/assets/f4092504-02cf-495c-8b20-044ae9230605) | ![updateRole © Khushi Raj](https://github.com/user-attachments/assets/aada56f6-4233-4ce8-b556-0541903ab86c) |

---

## 📁 Project APIs

| Method | Endpoint                               | Description                  |
| ------ | -------------------------------------- | ---------------------------- |
| POST   | `/api/projects`                        | Create a new project         |
| GET    | `/api/projects`                        | Get all projects (paginated) |
| GET    | `/api/projects/{id}`                   | Get project by ID            |
| PUT    | `/api/projects/{id}`                   | Update project               |
| DELETE | `/api/projects/{id}`                   | Delete project               |
| GET    | `/api/projects/by-manager/{managerId}` | Get projects by manager ID   |

#### 📁 Project API Screenshots

| Create Project                                                                                    | All Projects                                                                                    |
| ------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------- |
| ![createProject](https://github.com/user-attachments/assets/d68accd4-4d19-42c9-a808-501f36abd7b2) | ![allProjects](https://github.com/user-attachments/assets/c3b1d6e0-c688-431e-a2e1-4b8212cda665) |

| Get by ID                                                                                | Update                                                                                     | Delete                                                                                     | By Manager                                                                                  |
| ---------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------- |
| ![byId](https://github.com/user-attachments/assets/62a1f863-3dfb-41ca-8f0c-673eb4e99b93) | ![update](https://github.com/user-attachments/assets/78c5ffee-fc2a-4679-b5f7-7c3b862354af) | ![delete](https://github.com/user-attachments/assets/26a14f1f-84df-41ee-bd80-63de1b7bd8a7) | ![manager](https://github.com/user-attachments/assets/8f6de3a1-954e-4c37-907c-37c430239a18) |

---

## ✅ Task APIs

| Method | Endpoint                                | Description                   |
| ------ | --------------------------------------- | ----------------------------- |
| POST   | `/api/tasks`                            | Create a task                 |
| GET    | `/api/tasks/{id}`                       | Get task by ID                |
| PUT    | `/api/tasks/{id}`                       | Update task                   |
| PATCH  | `/api/tasks/{id}/status`                | Update only task status       |
| DELETE | `/api/tasks/{id}`                       | Delete task                   |
| GET    | `/api/tasks`                            | Get all tasks (paginated)     |
| GET    | `/api/tasks/assigned/{userId}`          | Get tasks by user             |
| GET    | `/api/tasks/status?status=TODO`         | Get tasks by status           |
| GET    | `/api/tasks/project/{projectId}/status` | Get tasks by project + status |
| GET    | `/api/tasks/assigned/{userId}/status`   | Get tasks by user + status    |

#### ✅ Task API Screenshots

| Create Task                                                                                    | Task by ID                                                                                   |
| ---------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------- |
| ![createTask](https://github.com/user-attachments/assets/be0c3306-592c-4b51-9af1-1970a8de6ed7) | ![taskById](https://github.com/user-attachments/assets/b98b7ea4-6df0-4ed5-8133-d5cfbe01cd34) |

| Update Status                                                                                     | Delete Task                                                                                    |
| ------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------- |
| ![updateStatus1 © Khushi Raj](https://github.com/user-attachments/assets/b7e70723-bda8-4a2e-9a4c-12b74945ea46) | ![deleteTask](https://github.com/user-attachments/assets/9c45479a-a03f-4f68-bde6-b35eb57f1e4d) |

| All Tasks                                                                                    | By User                                                                                    | By Status                                                                                    |
| -------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------ | -------------------------------------------------------------------------------------------- |
| ![allTasks](https://github.com/user-attachments/assets/955b39ec-64bb-4590-8979-8e0c4f8bed98) | ![byUser © Khushi Raj](https://github.com/user-attachments/assets/4b1d7424-6b69-4d93-b8dd-b31fff787f1a) | ![byStatus](https://github.com/user-attachments/assets/893bafd0-6c8d-4742-a063-82aacb20c998) |

| By Project + Status                                                                                 | By User + Status                                                                                 |
| --------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------ |
| ![byProjectStatus](https://github.com/user-attachments/assets/dda356e5-72c6-404f-bb46-b4c1989a7d35) | ![byUserStatus](https://github.com/user-attachments/assets/d28639ac-8a11-4020-85a8-300301a632ac) |

---

## 📊 Dashboard APIs

| Method | Endpoint                         | Description         |
| ------ | -------------------------------- | ------------------- |
| GET    | `/api/dashboard/stats`           | Get dashboard stats |
| GET    | `/api/dashboard/recent-projects` | Get recent projects |
| GET    | `/api/dashboard/recent-tasks`    | Get recent tasks    |

---

## 📦 Sample Payloads

### `ProjectDTO`

```json
{
  "name": "New Website Launch",
  "startDate": "2025-07-15",
  "endDate": "2025-08-10",
  "projectManagerId": 1
}
```

### `TaskDTO`

```json
{
  "title": "Create Landing Page",
  "description": "Design and develop the homepage UI.",
  "dueDate": "2025-07-20",
  "status": "TODO",
  "taskPriority": "HIGH",
  "projectId": 2,
  "assignedToId": 5
}
```

---

## 🚀 Getting Started

### Prerequisites

* Java 17+
* PostgreSQL
* Maven

### Setup Steps

1. Clone the repository:

   ```bash
   git clone https://github.com/yourusername/TeamManager.git
   cd TeamManager
   ```

2. Configure your `application.properties`:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/teammanager
   spring.datasource.username=postgres
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update
   jwt.secret=YourJWTSecretKey
   ```

3. Run the application:

   ```bash
   ./mvnw spring-boot:run
   ```

4. Test endpoints using Postman or Swagger UI.

---

## 🧪 Testing

* Add unit/integration tests under `src/test/java`
* Use Postman collections or Swagger for manual API testing

---
---------- © Khushi Raj | 2025 ---------
