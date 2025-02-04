
CREATE TABLE IF NOT EXISTS organisation_employee
(
    organisation_id UUID REFERENCES Organization (id) ON DELETE CASCADE,
    employee_id     UUID REFERENCES Employee (id) ON DELETE CASCADE,
    position        employee_position_in_organization NOT NULL,
    PRIMARY KEY (organisation_id, employee_id)
);