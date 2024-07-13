create table users
(
    user_id         bigint auto_increment
        primary key,
    about           text         null,
    created_at      datetime(6)  null,
    designation     varchar(255) null,
    image_file_name varchar(255) null,
    salary          double       not null,
    user_name       varchar(255) null
);


