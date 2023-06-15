CREATE TABLE IF NOT EXISTS item_request (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description VARCHAR(255)                   NOT NULL,
    user_id BIGINT                             NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE        NOT NUll,
    CONSTRAINT pk_item_request PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255)                          NOT NULL,
    email VARCHAR(512)                         NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(20)                           NOT NULL,
    description VARCHAR(50)                    NOT NULL,
    available BOOLEAN                          NOT NULL,
    owner BIGINT                               NOT NULL,
    request BIGINT,
    CONSTRAINT pk_items PRIMARY KEY (id),
    CONSTRAINT fk_items_users FOREIGN KEY (owner) REFERENCES users(id),
    CONSTRAINT fk_item_item_request FOREIGN KEY (request) REFERENCES item_request(id)
);
