# AWS deployment and operations runbook

The repository and application now target AWS only. This runbook covers AWS provisioning, migration of any retained
source data, deployment, and AWS-native rollback. It does not destroy cloud resources or manipulate legacy state.

## Before provisioning

- Confirm AWS account, region, quotas, budget, ownership, and an authorized bootstrap identity.
- Confirm the organization SCP permits creating the dedicated IAM deploy users, their inline policies, and access keys.
  SCP denials cannot be overridden by granting broader IAM permissions in the account.
- Verify the ACM certificate covers the chosen API hostname. Verify its DNS challenge and the SES sender identity
  separately; SES may also be restricted to sandbox recipients.
- Decide the DNS ownership, desired retention, recovery point objective, recovery time objective, and approved rollback
  window for each environment.
- Review the Terraform plan for public endpoints, data retention, NAT gateway charges, database sizing, IAM permissions,
  and security-group changes. Terraform state contains generated database, Redis, and JWT credentials; protect both
  remote and any temporary local state as sensitive data.
- The backend bootstrap initially uses a local state file. Keep that file out of Git and store a protected backup.

## Service compatibility

DocumentDB is currently the MongoDB-compatible implementation candidate. Do not set
`documentdb_compatibility_confirmed=true` for QA or production until representative integration tests pass against the
selected DocumentDB engine version. Check every query, index, aggregation, update, BSON type, TLS trust chain, and
history read/write path used by Motor Desk. If a required MongoDB feature is unsupported, select MongoDB Atlas or
another compatible service and update the Terraform module before production.

ElastiCache Redis is configured with TLS and an auth token. Verify Redis Streams commands, consumer group creation,
pending-entry handling, acknowledgements, retries, and worker behavior using the selected engine version. Redis remains
transport/cache; PostgreSQL remains the transactional source of truth.

## Data migration rehearsal

1. Identify any retained source data and record database versions, sizes, indexes, extensions, users, encoding, and
   backup/retention policies.
2. Take and verify recoverable source PostgreSQL and MongoDB backups. Perform a restore rehearsal before relying on them.
3. Provision an isolated AWS environment and restore copies of the data there. Validate row/document counts, constraints,
   sequences, indexes, application reads/writes, Service Order history, and the email queue state.
4. Prefer a native replication or managed migration path only after its compatibility and consistency guarantees are
   confirmed. Otherwise agree on a maintenance window and use a tested export/import procedure with writes paused.
5. Treat Redis Streams separately: identify queued and pending messages, pause producers and consumers, and drain or
   explicitly reconcile the stream. Do not assume a cache dump is a safe source of business data.
6. Record the exact restore commands, expected duration, checksums/counts, and responsible operators without placing
   credentials or sensitive dumps in the repository.

## AWS cutover

1. Announce a write freeze and confirm that backups and restore points are current.
2. Pause the source-side email workers/producers as appropriate; prevent both environments from processing the same queue.
3. Perform the final PostgreSQL and MongoDB synchronization and reconcile Redis pending messages using the rehearsed
   procedure.
4. Deploy the tested application image to ECS and validate `/health`, authentication, representative Service Order
   operations, PostgreSQL persistence, MongoDB history, Redis Streams, and SES delivery.
5. Switch DNS to the AWS ALB only after the new endpoint passes smoke checks.
6. Monitor API errors/latency, ECS task health, ALB target health, database connections/storage, Redis stream pending
   entries, CloudWatch logs, and SES delivery/bounce signals through the agreed rollback window.

## AWS rollback

- Roll back to the last known-good ECS task-definition revision while preserving the AWS database state.
- If AWS has accepted writes and a data restore is needed, freeze writes and follow the tested AWS restore/reconciliation
  procedure before resuming service.
- Avoid split-brain processing: at any point, only one environment should own business writes and each queued email
  should have one active consumer path.
- Record the failure, data reconciliation, and recovery decision.

## Completion and decommissioning

Remove any legacy cloud resources and credentials through their owning cloud account only after confirming that data has
been migrated, backups are restorable, traffic and workers have moved, and no consumers remain. The old provider's
Terraform configuration has been removed from this repository, so decommissioning those external resources requires a
separately reviewed, account-authorized procedure. The code change does not delete remote resources or state.
