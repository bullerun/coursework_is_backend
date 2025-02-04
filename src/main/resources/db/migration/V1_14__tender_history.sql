CREATE TABLE IF NOT EXISTS Tender_History
(
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tender_id       UUID          NOT NULL REFERENCES Tender (id),
    name            TEXT          NOT NULL,
    description     TEXT          NOT NULL,
    cost            MONEY         NOT NULL,
    region          region,
    organization_id UUID          NOT NULL REFERENCES Organization (id),
    version         BIGINT           DEFAULT 1,
    tender_status   tender_status NOT NULL,
    created_at      TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP,
    expired_at      TIMESTAMP
    );