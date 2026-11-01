terraform -chdir="infra\terraform\aws\envs\dev" init -input=false -backend-config="bucket=dev-test-bucket-backend" -backend-config="region=us-east-2"
terraform -chdir="infra\terraform\aws\envs\dev" plan 
