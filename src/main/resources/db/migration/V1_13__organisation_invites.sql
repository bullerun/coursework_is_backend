CREATE TABLE organisation_invites
(
    id              UUID PRIMARY KEY                    DEFAULT uuid_generate_v4(),
    sender_id       UUID REFERENCES users (id) NOT NULL,
    receiver_id     UUID REFERENCES users (id) NOT NULL,
    organization_id UUID REFERENCES organization (id),
    status          VARCHAR(50)                NOT NULL DEFAULT 'PENDING',
    created_at      TIMESTAMP                           DEFAULT CURRENT_TIMESTAMP
);
