CREATE TABLE assessment
(
    request_id           uuid DEFAULT gen_random_uuid(),
    request_ip           text      NOT NULL,
    request_ip_provider  text,
    request_country_code text,
    request_uri          text      NOT NULL,
    request_start        timestamp NOT NULL,
    time_lapsed          decimal   NOT NULL,
    response_code        int       NOT NULL,
    PRIMARY KEY (request_id)
);

-- TODO create necessary indexes
