CREATE TABLE IF NOT EXISTS Employee
(
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id    UUID REFERENCES Users (id),
    first_name Text,
    last_name  TEXT,
    created_at TIMESTAMP        DEFAULT current_timestamp,
    updated_at TIMESTAMP
);