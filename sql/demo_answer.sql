create table answer
(
    id             bigint auto_increment
        primary key,
    topic_id       int           null,
    answer_id      int           null,
    question_id    int           null,
    question       varchar(300)  null,
    voteup_count   int           null,
    excerpt        varchar(3000) null,
    author_name    varchar(50)   null,
    create_date    date          null,
    answer_url     varchar(255)  null,
    content        longtext      null,
    is_god_replies int default 0 null
);

