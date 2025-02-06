CREATE TABLE IF NOT EXISTS organisation_employee
(
    organisation_id UUID REFERENCES Organization (id) ON DELETE CASCADE,
    employee_id     UUID REFERENCES users (id) ON DELETE CASCADE,
    position        VARCHAR(50) NOT NULL,
    PRIMARY KEY (organisation_id, employee_id)
);