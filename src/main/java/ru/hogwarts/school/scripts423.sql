CREATE TABLE student_faculty
(
    student_id INTEGER NOT NULL,
    faculty_id INTEGER NOT NULL,

    CONSTRAINT student_cons
        FOREIGN KEY (student_id)
            REFERENCES student (id) ON DELETE CASCADE,

    CONSTRAINT faculty_cons
        FOREIGN KEY (faculty_id)
REFERENCES faculty(id) ON DELETE CASCADE ,

    CONSTRAINT pk_student_faculty
PRIMARY KEY (student_id,faculty_id)
);

SELECT
    s.name AS student_name,
    s.age AS student_age,
    f.name AS faculty_name
FROM student s
         INNER JOIN student_faculty sf ON s.id = sf.student_id
         INNER JOIN faculty f ON sf.faculty_id = f.id
ORDER BY s.name;

CREATE TABLE student_avatar
(
    student_id INTEGER NOT NULL,
    avatar_id INTEGER NOT NULL,

    CONSTRAINT student_const
        FOREIGN KEY (student_id)
            REFERENCES student (id) ON DELETE CASCADE,

    CONSTRAINT avatar_const
        FOREIGN KEY (avatar_id)
            REFERENCES avatar(id) ON DELETE CASCADE ,

    CONSTRAINT pk_student_avatar
        PRIMARY KEY (student_id,avatar_id)
);

SELECT
    s.name AS student_name,
    s.age AS student_age,
    f.name AS faculty_name
FROM student s
         INNER JOIN student_avatar sa ON s.id = sa.student_id
         INNER JOIN student_faculty sf ON s.id = sf.student_id
         INNER JOIN faculty f ON sf.faculty_id = f.id
WHERE
    sa.avatar_id >0
ORDER BY s.name;