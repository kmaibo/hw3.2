CREATE TABLE person (
    id_person SERIAL PRIMARY KEY ,
    name CHAR NOT NULL ,
    age INTEGER NOT NULL ,
    driver_license BOOLEAN NOT NULL
);
CREATE TABLE car (
    id_car SERIAL PRIMARY KEY ,
    brand CHAR NOT NULL ,
    model CHAR NOT NULL ,
    price NUMERIC(10,2)
);
CREATE TABLE person_car (
    person_id INTEGER NOT NULL ,
    car_id INTEGER NOT NULL ,
    PRIMARY KEY (person_id, car_id),
    FOREIGN KEY (person_id) REFERENCES person(id_person) ON DELETE CASCADE ,
    FOREIGN KEY (car_id) REFERENCES car (id_car) ON DELETE CASCADE
)
