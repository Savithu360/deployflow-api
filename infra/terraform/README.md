# DeployFlow Terraform Template

This template creates one EC2 instance and a security group for SSH, HTTP, and
port 8080. It does not create credentials, a key pair, a database service, DNS,
TLS, or remote Terraform state.

## Usage

1. Install Terraform and configure the AWS CLI with your own account.
2. Find a current Ubuntu AMI for your selected region.
3. Create `terraform.tfvars` locally:

```hcl
aws_region   = "us-east-1"
instance_type = "t3.micro"
key_name     = "your-existing-key-pair"
ami_id       = "ami-replace-me"
ssh_cidr     = "YOUR.PUBLIC.IP/32"
```

4. Run:

```bash
terraform init
terraform fmt -check
terraform validate
terraform plan
terraform apply
```

Do not commit `terraform.tfvars`, state files, private keys, or AWS credentials.
For a production design, restrict port 8080, place the app behind a reverse
proxy with TLS, use a managed database, and configure remote encrypted state.
