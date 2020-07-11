CREATE TABLE type_fuel(
    fuel_id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    price INTEGER
);

CREATE TABLE transports (
    transport_id INTEGER PRIMARY KEY,
    brand TEXT NOT NULL,
    model TEXT NOT NULL,
    number TEXT NOT NULL UNIQUE,
    type_transport INTEGER NOT NULL,
    type_fuel INTEGER NOT NULL,
    static_use_fuel DOUBLE  NOT NULL,
    FOREIGN KEY (type_fuel) REFERENCES type_fuel(fuel_id)
);

CREATE TABLE drivers (
    driver_id INTEGER PRIMARY KEY,
    last_name TEXT NOT NULL,
    first_name TEXT NOT NULL,
    father_name TEXT NOT NULL,
    transport TEXT NOT NULL,
    FOREIGN KEY (transport) REFERENCES tansports(transport_id)
);

CREATE TABLE trailer (
    trailer_id INTEGER PRIMARY KEY,
    brand TEXT NOT NULL,
    number TEXT NOT NULL
);

CREATE TABLE time_sheet (
    id INTEGER PRIMARY KEY,
    day_date DATE NOT NULL,
    driver INTEGER NOT NULL REFERENCES drivers(driver_id),
    transport INTEGER NOT NULL  REFERENCES tansports(transport_id),
    use_fuel DOUBLE,
    result_work DOUBLE,
    trailer INTEGER REFERENCES trailer(trailer_id)
);




ALTER TABLE time_sheet
ADD trailer INTEGER REFERENCES trailer(trailer_id);

select * from time_sheet;
select * from drivers;
select * from transports;
select * from type_fuel;
select * from trailer;


drop table transports;


insert into type_fuel (name, price)
values("A-95", "0");

insert into type_fuel (name, price)
values("A-92", "0");

insert into type_fuel (name, price)
values("A-80", "0");

insert into type_fuel (name, price)
values("ДП", "0");

insert into type_fuel (name, price)
values("ГАЗ", "0");

insert into transports (brand, model, number, type_transport, type_fuel, static_use_fuel)
values("maz", "model1", "1234", "1", "1", "1");

insert into drivers (full_name, transport)
values("Andriy","1");



insert into time_sheet (day_date, driver, transport, use_fuel, result_work)
values("2020-04-04", "1", "1", "12", "320");


insert into drivers (full_name, transport)
values("Andriy", "1");

DELETE FROM drivers WHERE driver_id=1;
DELETE FROM time_sheet WHERE id=3;