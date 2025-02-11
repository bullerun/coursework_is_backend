create table if not exists role_request
(
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id       UUID REFERENCES Users (id) ON DELETE CASCADE,
    requested_role TEXT NOT NULL,
    status        TEXT NOT NULL,
    created_at    TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);