output "instance_id" {
  description = "ID of the DeployFlow EC2 instance"
  value       = aws_instance.deployflow.id
}

output "public_ip" {
  description = "Public IP address of the DeployFlow EC2 instance"
  value       = aws_instance.deployflow.public_ip
}
