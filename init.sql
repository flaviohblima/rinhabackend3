CREATE UNLOGGED TABLE payments (
  correlation_id UUID PRIMARY KEY,
  amount DECIMAL NOT NULL,
  processor_name VARCHAR(16) NOT NULL,
  is_processed BOOLEAN NOT NULL,
  requested_at TIMESTAMP NOT NULL
);

CREATE INDEX payments_requested_at ON payments (requested_at);