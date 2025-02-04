CREATE TABLE IF NOT EXISTS Bid
(
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        TEXT        NOT NULL,
    description TEXT        NOT NULL,
    tender_id   UUID REFERENCES Tender (id),
    cost        MONEY       NOT NULL,
    region      region,
    author_type author_type NOT NULL,
    author_id   UUID        NOT NULL,
    version     BIGINT           DEFAULT 1,
    bid_status  bid_status  NOT NULL,
    created_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP,
    expired_at  TIMESTAMP
    );