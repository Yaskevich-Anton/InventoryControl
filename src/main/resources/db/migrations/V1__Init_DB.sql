
create table users
(
    id                uuid not null
        primary key,
    address           varchar(255),
    bank_account      varchar(255),
    bank_name         varchar(255),
    country           varchar(255),
    director_name     varchar(255),
    email             varchar(255),
    form_of_ownership varchar(255),
    manager_name      varchar(255),
    phone_number      varchar(255),
    username          varchar(255)
);
create table product
(
    measure_unit smallint
        constraint product_measure_unit_check
            check ((measure_unit >= 0) AND (measure_unit <= 2)),
    price        integer,
    id           uuid not null
        primary key,
    country      varchar(255),
    description  varchar(255),
    name         varchar(255),
    type         varchar(255)
);
create table orders
(
    status  smallint
        constraint orders_status_check
            check ((status >= 0) AND (status <= 2)),
    date    timestamp(6),
    id      uuid not null
        primary key,
    user_id uuid not null
        constraint fk32ql8ubntj5uh44ph9659tiih
            references users
);
create table employee
(
    role smallint
        constraint employee_role_check
            check ((role >= 0) AND (role <= 4)),
    id   uuid not null
        primary key
);

