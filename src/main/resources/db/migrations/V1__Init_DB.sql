
CREATE TABLE users (
                             id UUID PRIMARY KEY,
                             username VARCHAR(255) NOT NULL,
                             password VARCHAR(255) NOT NULL,
                             country VARCHAR(100),
                             phone_number VARCHAR(20),
                             email VARCHAR(255),
                             role VARCHAR(50) NOT NULL
);
CREATE TABLE product (
                            id UUID PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            measure_unit VARCHAR(50),
                            description TEXT,
                            price DOUBLE PRECISION,
                            country VARCHAR(100),
                            type VARCHAR(50),
                            quantity INTEGER,
                            photo_url VARCHAR(255)
);
CREATE TABLE orders (
                              id UUID PRIMARY KEY,
                              user_id UUID NOT NULL,
                              date DATE,
                              status VARCHAR(50),
                              FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE order_product (
                              order_id UUID,
                              product_id UUID,
                              quantity INTEGER,
                              PRIMARY KEY (order_id, product_id),
                              FOREIGN KEY (order_id) REFERENCES orders(id),
                              FOREIGN KEY (product_id) REFERENCES product(id)
);
create table employee
(
    role smallint
        constraint employee_role_check
            check ((role >= 0) AND (role <= 4)),
    id   uuid not null
        primary key
);

