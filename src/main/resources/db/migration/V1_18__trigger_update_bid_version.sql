CREATE OR REPLACE FUNCTION update_bid_version()
    RETURNS TRIGGER AS
$$
BEGIN

    IF OLD.name = NEW.name AND
       OLD.description = NEW.description AND
       OLD.bid_status = NEW.bid_status AND
       OLD.version = NEW.version THEN
        RETURN NEW;
    END IF;

    INSERT INTO bid_history (bid_id, name, description, tender_id, cost, region, author_type, author_id, bid_status,
                             version, updated_at, expired_at, owner_id)
    VALUES (OLD.id, OLD.name, OLD.description, OLD.tender_id, OLD.cost, OLD.region, OLD.author_type, OLD.author_id,
            OLD.bid_status, OLD.version,NOW(), OLD.expired_at, OLD.owner_id);

    NEW.version := OLD.version + 1;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_bid_version
    BEFORE UPDATE
    ON bid
    FOR EACH ROW
EXECUTE FUNCTION update_bid_version();