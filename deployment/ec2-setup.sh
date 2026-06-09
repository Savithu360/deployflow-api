#!/usr/bin/env bash
set -euo pipefail

# Run this on a fresh Ubuntu EC2 instance.
sudo apt-get update
sudo apt-get upgrade -y

# Install Docker using Ubuntu's packages.
sudo apt-get install -y docker.io docker-compose-v2 git

# Start Docker now and automatically after reboot.
sudo systemctl enable --now docker

# Allow the current user to run Docker without sudo.
# Log out and back in after this command for the group change to apply.
sudo usermod -aG docker "$USER"

# Verify the installed tools.
docker --version
docker compose version
sudo systemctl status docker --no-pager

echo "EC2 setup complete. Log out and back in before running Docker as your user."
