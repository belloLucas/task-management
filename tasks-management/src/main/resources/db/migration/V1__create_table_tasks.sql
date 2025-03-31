create table tasks (
    id bigserial not null primary key,
    title varchar(255) not null,
    description text,
    priority varchar(50) not null,
    assignee bigint,
    deadline timestamp,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_set_updated_at
BEFORE UPDATE ON tasks
FOR EACH ROW
EXECUTE FUNCTION set_updated_at();