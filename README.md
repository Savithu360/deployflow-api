# DeployFlow API

**A DevOps-ready delivery incident tracking API for foodservice operations.**

DeployFlow API is a Spring Boot REST API for tracking delivery and warehouse
operation incidents, prepared for Docker, CI/CD, AWS EC2 deployment, monitoring,
and infrastructure automation.

## The Real-World Problem

Foodservice operations depend on reliable delivery, warehouse handling, vehicle
availability, temperature control, and customer order accuracy. DeployFlow gives
operations teams a simple way to record, prioritize, monitor, filter, and
resolve incidents affecting those workflows.

## Why This Project Was Built

This portfolio project demonstrates practical skills for a DevOps internship:
backend development, containerization, CI/CD readiness, cloud deployment
preparation, Infrastructure as Code, monitoring, troubleshooting, automation,
and clear technical documentation.

## DevOps Concepts Demonstrated

- CI/CD pipeline readiness with GitHub Actions
- Multi-stage Docker containerization
- Docker Compose application and MySQL environment
- AWS EC2 deployment preparation
- Infrastructure as Code with Terraform
- Environment-variable configuration
- Health checks with Spring Boot Actuator
- Structured API errors and application logging
- Troubleshooting documentation
- Repeatable deployment scripts

## Tech Stack

Java 21, Spring Boot 3.5.14, Spring Web, Spring Data JPA, Validation,
Actuator, MySQL 8.4, H2 for tests and the Choreo cloud demo, Maven, Docker,
Docker Compose, GitHub Actions, Terraform, AWS EC2, and Bash.

## Architecture

```text
HTTP Client
    |
Controller -> Service -> Repository -> MySQL
    |           |
 Validation   Business rules and logging
    |
Global exception handler
```

The code uses DTOs at the API boundary and keeps persistence entities inside
the application. Local and Docker Compose deployments use MySQL, automated
tests use H2, and the WSO2 Choreo cloud demo uses an isolated H2 profile.

## Folder Structure

```text
.
|-- .github/workflows/ci-cd.yml
|-- deployment/
|   |-- deploy.sh
|   `-- ec2-setup.sh
|-- infra/terraform/
|   |-- main.tf
|   |-- outputs.tf
|   |-- variables.tf
|   `-- README.md
|-- scripts/log-summary-prompt.md
|-- src/
|   |-- main/java/com/savithu/deployflow/
|   |   |-- controller/
|   |   |-- dto/
|   |   |-- entity/
|   |   |-- exception/
|   |   |-- repository/
|   |   `-- service/
|   |-- main/resources/application.yml
|   |-- test/java/com/savithu/deployflow/
|   `-- test/resources/application.yml
|-- .env.example
|-- Dockerfile
|-- docker-compose.yml
`-- pom.xml
```

## API Endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/health` | Custom application health |
| GET | `/actuator/health` | Actuator health details |
| GET | `/actuator/info` | Application information |
| POST | `/api/incidents` | Create an incident |
| GET | `/api/incidents` | List all incidents |
| GET | `/api/incidents/{id}` | Get an incident |
| PUT | `/api/incidents/{id}` | Fully update an incident |
| DELETE | `/api/incidents/{id}` | Delete an incident |
| GET | `/api/incidents/status/{status}` | Filter by status |
| GET | `/api/incidents/priority/{priority}` | Filter by priority |
| GET | `/api/incidents/type/{incidentType}` | Filter by type |
| PATCH | `/api/incidents/{id}/resolve` | Resolve an incident |
| GET | `/api/incidents/summary` | Get operational counts |

Valid incident types are `DELIVERY_DELAY`, `TEMPERATURE_ALERT`,
`VEHICLE_ISSUE`, `WAREHOUSE_LOADING_ISSUE`, `MISSING_ITEMS`,
`CUSTOMER_COMPLAINT`, `ROUTE_BLOCKED`, and `OTHER`.

Priorities are `LOW`, `MEDIUM`, `HIGH`, and `CRITICAL`. Statuses are `OPEN`,
`IN_PROGRESS`, `RESOLVED`, and `CANCELLED`.

## Sample Requests

```json
{
  "title": "Truck delayed due to road closure",
  "description": "Delivery truck was delayed due to an unexpected road closure.",
  "incidentType": "DELIVERY_DELAY",
  "priority": "HIGH",
  "status": "OPEN",
  "location": "Colombo Distribution Route 04",
  "vehicleId": "TRUCK-102",
  "reportedBy": "Operations Team"
}
```

```json
{
  "title": "Cold storage temperature alert",
  "description": "Temperature exceeded safe range during loading.",
  "incidentType": "TEMPERATURE_ALERT",
  "priority": "CRITICAL",
  "status": "OPEN",
  "location": "Warehouse Cold Storage Zone A",
  "vehicleId": "TRUCK-218",
  "reportedBy": "Warehouse Supervisor"
}
```

Example summary:

```json
{
  "totalIncidents": 12,
  "openIncidents": 5,
  "inProgressIncidents": 2,
  "criticalIncidents": 2,
  "resolvedIncidents": 4,
  "cancelledIncidents": 1
}
```

Errors include a timestamp, HTTP status, error, message, request path, and field
validation errors when applicable.

## Run Locally Without Docker

Requirements: Java 21, Maven 3.6.3 or newer, and MySQL.

Create the database and user, then set environment variables. PowerShell:

```powershell
$env:DB_HOST="localhost"
$env:DB_PORT="3306"
$env:DB_NAME="deployflow"
$env:DB_USERNAME="deployflow"
$env:DB_PASSWORD="your-password"
mvn clean test
mvn spring-boot:run
```

The application runs at `http://localhost:8080`.

## Run With Docker

No `.env` file is required for a quick local demonstration because Compose uses
isolated development credentials. Change these placeholder values before any
remote deployment and keep real values outside version control.

```bash
docker compose up --build
docker compose ps
docker compose logs -f app
```

Stop containers with `docker compose down`. Add `--volumes` only when you
intentionally want to delete local MySQL data.

## Cloud Demo Deployment with WSO2 Choreo

WSO2 Choreo runs the application with the `choreo` Spring profile. This profile
uses an in-memory H2 database so the portfolio API can be demonstrated without
provisioning an external cloud database.

Configure this environment variable in the Choreo component:

```text
SPRING_PROFILES_ACTIVE=choreo
```

Choreo may provide a `PORT` environment variable. The application uses it when
present and otherwise listens on port `8080`. The same Docker image can also be
tested manually with:

```bash
docker run --rm -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=choreo \
  deployflow-api:local
```

After deployment, verify:

- Health check: `/actuator/health`
- API test endpoint: `/api/incidents/summary`

The H2 database is intentionally temporary: cloud demo data is lost when the
Choreo container restarts or is redeployed. This is suitable for a portfolio
demonstration, not durable production storage.

Deployment modes are intentionally separated:

- Local deployment: Docker Compose and MySQL
- Cloud demo deployment: WSO2 Choreo and H2
- AWS deployment readiness: EC2 scripts and Terraform templates are included

## Test With Postman

1. Create a collection with base URL `http://localhost:8080`.
2. Send `GET /api/health` and confirm `status` is `UP`.
3. Send `POST /api/incidents` with `Content-Type: application/json` and one of
   the sample bodies.
4. Copy the returned `id` and test GET, PUT, and
   `PATCH /api/incidents/{id}/resolve`.
5. Test the filter and summary endpoints.
6. Send an invalid enum such as `"priority": "URGENT"` to capture the
   structured `400 Bad Request` response.

## GitHub Actions

The workflow runs for pushes and pull requests targeting `main`. It configures
Java 21, caches Maven dependencies, runs tests, packages the application, and
builds a Docker image.

Later configuration:

- Enable the commented EC2 deployment job only after testing it.
- Add `EC2_HOST`, `EC2_USER`, and `EC2_SSH_KEY` as repository secrets.
- Add `DOCKER_USERNAME` and `DOCKER_PASSWORD` if publishing images.
- Add registry login, image push, environment protection, and approval rules.
- Never place credentials directly in the workflow file.

## AWS EC2 Preparation

1. Provision Ubuntu EC2 and restrict SSH to your public IP.
2. Connect using your private key.
3. Clone the repository and run `deployment/ec2-setup.sh`.
4. Log out and back in so Docker group membership takes effect.
5. Configure a private `.env` on the server.
6. Run `deployment/deploy.sh` from the repository root.
7. Confirm `http://EC2_PUBLIC_IP:8080/actuator/health`.

For a stronger production deployment, use HTTPS, a reverse proxy or load
balancer, restricted networking, RDS, managed secrets, backups, and monitoring.

## Terraform

The template in `infra/terraform` creates an EC2 instance and security group.
Before use, configure AWS CLI credentials outside the repository, choose a
region-specific Ubuntu AMI, provide an existing key pair, and restrict
`ssh_cidr` to your public IP. See its dedicated README for commands.

## Monitoring

```bash
curl http://localhost:8080/api/health
curl http://localhost:8080/actuator/health
curl http://localhost:8080/actuator/info
docker compose ps
docker compose logs --tail=100 app
docker compose logs --tail=100 database
```

## Troubleshooting

| Problem | Checks and fixes |
|---|---|
| MySQL connection refused | Confirm MySQL is running, `DB_HOST` is correct, credentials match, and the database health check passes. Use `database` as host inside Compose, not `localhost`. |
| Port 8080 already in use | Stop the conflicting process/container or change the host-side Compose port. |
| Port 3306 already in use | Stop local MySQL or map the Compose database to another host port. |
| Docker build failed | Check network access, Docker disk space, Java/Maven build errors, and retry `docker compose build --no-cache`. |
| Maven test failure | Run `mvn clean test`, inspect the first failure, and confirm Java 21 with `java -version`. Tests use H2 and do not require MySQL. |
| GitHub Actions secret missing | Add the named repository secret or keep the optional deployment job disabled. |
| EC2 permission denied | Confirm the SSH user, key permissions, instance key pair, and security-group source IP. |
| App starts before database | Inspect the database health check. Compose waits for `service_healthy` before starting the app. |
| Environment variable missing | Compare the environment with `.env.example`; do not add spaces around `=`. |
| Actuator endpoint unavailable | Confirm the app is running and `health,info` remain exposed in `application.yml`. |
| Invalid enum value | Use uppercase values exactly as listed in the API section. |

## Future Improvements

- Authentication and role-based authorization
- Pagination, sorting, and date-range search
- OpenAPI/Swagger documentation
- Flyway database migrations
- RDS, HTTPS, DNS, and load-balancer integration
- Metrics dashboards and alerting
- Audit history and incident comments
- Image registry publishing and automated rollback

## Suggested Screenshots

- Successful `mvn test` output
- Running Docker Compose services and healthy status
- Postman create, resolve, filter, summary, and validation responses
- Actuator health and info responses
- GitHub Actions workflow success
- Terraform plan summary
- EC2 terminal with healthy containers
- Application health response through the EC2 public address

## Portfolio Text

**CV description:** Built a Java 21 Spring Boot incident tracking REST API for
foodservice delivery operations, with MySQL persistence, automated tests,
Docker Compose, GitHub Actions CI/CD preparation, AWS EC2 deployment scripts,
Terraform infrastructure templates, Actuator health checks, and troubleshooting
documentation.

**GitHub description:** DevOps-ready Spring Boot API for tracking delivery and
warehouse incidents, containerized with Docker and prepared for CI/CD, AWS EC2,
Terraform, and monitoring.

**Topics:** `java`, `spring-boot`, `rest-api`, `mysql`, `docker`,
`docker-compose`, `github-actions`, `aws`, `ec2`, `terraform`, `devops`,
`monitoring`, `portfolio-project`

## Screenshots

Add final screenshots to a future `docs/screenshots` directory and replace this
section with a compact project walkthrough.

## Author

Savithu Pemachandra
