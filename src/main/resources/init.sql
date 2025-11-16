CREATE TABLE IF NOT EXISTS users
(
    id
    SERIAL
    PRIMARY
    KEY,
    login
    VARCHAR
(
    50
) NOT NULL UNIQUE,
    password VARCHAR
(
    255
) NOT NULL,
    salt VARCHAR
(
    255
) NOT NULL,
    email VARCHAR
(
    255
) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS categories
(
    id
    SERIAL
    PRIMARY
    KEY,
    name
    VARCHAR
(
    100
) NOT NULL,
    type VARCHAR
(
    20
) NOT NULL CHECK
(
    type
    IN
(
    'INCOME',
    'EXPENSE'
)),
    description VARCHAR
(
    255
),
    UNIQUE
(
    name,
    type
)
    );

CREATE TABLE IF NOT EXISTS transactions
(
    id
    SERIAL
    PRIMARY
    KEY,
    user_id
    INTEGER
    NOT
    NULL
    REFERENCES
    users
(
    id
) ON DELETE CASCADE,
    amount NUMERIC
(
    12,
    2
) NOT NULL CHECK
(
    amount
    >=
    0
),
    category VARCHAR
(
    100
) NOT NULL,
    description VARCHAR
(
    500
),
    type VARCHAR
(
    20
) NOT NULL CHECK
(
    type
    IN
(
    'INCOME',
    'EXPENSE'
)),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );


CREATE TABLE IF NOT EXISTS tags
(
    id
    SERIAL
    PRIMARY
    KEY,
    name
    VARCHAR
(
    50
) NOT NULL,
    user_id INTEGER NOT NULL REFERENCES users
(
    id
) ON DELETE CASCADE,
    color VARCHAR
(
    7
) DEFAULT '#3498db',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE
(
    name,
    user_id
)
    );

CREATE TABLE IF NOT EXISTS transaction_tags
(
    id
    SERIAL
    PRIMARY
    KEY,
    transaction_id
    INTEGER
    NOT
    NULL
    REFERENCES
    transactions
(
    id
) ON DELETE CASCADE,
    tag_id INTEGER NOT NULL REFERENCES tags
(
    id
)
  ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE
(
    transaction_id,
    tag_id
)
    );


INSERT INTO categories (name, type)
VALUES ('Зарплата', 'INCOME'),
       ('Фриланс', 'INCOME'),
       ('Инвестиции', 'INCOME'),
       ('Подарки', 'INCOME'),
       ('Прочее', 'INCOME'),

       ('Еда', 'EXPENSE'),
       ('Транспорт', 'EXPENSE'),
       ('Жилье', 'EXPENSE'),
       ('Развлечения', 'EXPENSE'),
       ('Здоровье', 'EXPENSE'),
       ('Одежда', 'EXPENSE'),
       ('Образование', 'EXPENSE'),
       ('Другое', 'EXPENSE') ON CONFLICT (name, type) DO NOTHING;