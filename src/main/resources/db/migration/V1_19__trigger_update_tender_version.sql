CREATE OR REPLACE FUNCTION update_tender_version()
    RETURNS TRIGGER AS
$$
BEGIN
    IF OLD.name = NEW.name AND
       OLD.description = NEW.description AND
       OLD.tender_status = NEW.tender_status AND
       OLD.version = NEW.version THEN
        RETURN NEW;
END IF;

INSERT INTO tender_history (tender_id, name, description, cost, region, organization_id, version, tender_status,
                             created_at, updated_at, expired_at)
VALUES (OLD.id, OLD.name, OLD.description, OLD.cost, OLD.region, OLD.organization_id, OLD.version,
        OLD.tender_status, NOW(), OLD.expired_at);

NEW.version := OLD.version + 1;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_tender_version
    BEFORE UPDATE
    ON tender
    FOR EACH ROW
    EXECUTE FUNCTION update_tender_version();