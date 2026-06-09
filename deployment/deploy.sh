#!/usr/bin/env bash
set -euo pipefail

# Run this script from the repository root on the EC2 instance.
git pull --ff-only origin main

# Stop the currently running application without deleting MySQL data.
docker compose down

# Rebuild the application image and start all services in the background.
docker compose up --build --detach

# Show container state and recent application logs.
docker compose ps
docker compose logs --tail=100 app
