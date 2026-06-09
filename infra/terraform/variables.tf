variable "aws_region" {
  description = "AWS region where resources will be created"
  type        = string
  default     = "us-east-1"
}

variable "instance_type" {
  description = "EC2 instance type"
  type        = string
  default     = "t3.micro"
}

variable "key_name" {
  description = "Name of an existing AWS EC2 key pair"
  type        = string
}

variable "ami_id" {
  description = "Ubuntu AMI ID valid in the selected region"
  type        = string
}

variable "ssh_cidr" {
  description = "CIDR allowed to connect over SSH; use your public IP/32"
  type        = string
}
