CREATE TABLE IF NOT EXISTS feedback
(
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    bid_id          uuid REFERENCES Bid (id),
    description     TEXT NOT NULL,
    organisation_id UUID REFERENCES Organization (id),
    feedback_status TEXT NOT NULL,
    created_at      TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);