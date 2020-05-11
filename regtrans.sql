CREATE TABLE transports (
    transport_id INTEGER PRIMARY KEY,
    brand TEXT NOT NULL,
    model TEXT NOT NULL,
    number TEXT NOT NULL UNIQUE,
    type_transport INTEGER NOT NULL,
    type_fuel INTEGER NOT NULL,
    static_use_fuel INTEGER  NOT NULL
);

CREATE TABLE drivers (
    driver_id INTEGER PRIMARY KEY,
    full_name TEXT NOT NULL,
    transport TEXT NOT NULL,
    FOREIGN KEY (transport) REFERENCES tansports(transport_id)
);

CREATE TABLE time_sheet (
    id INTEGER PRIMARY KEY,
    day_date DATE NOT NULL,
    driver INTEGER NOT NULL REFERENCES drivers(driver_id),
    transport INTEGER NOT NULL  REFERENCES tansports(transport_id),
    use_fuel INTEGER,
    result_work INTEGER
);

select * from time_sheet;
select * from drivers;
select * from transports;
drop table transport;

insert into transports (brand, model, number, type_transport, type_fuel, static_use_fuel)
values("maz", "model1", "1234", "Gruz", "gaz", "1");


insert into time_sheet (day_date, driver, transport, use_fuel, result_work)
values("2020-04-04", "4", "3", "12", "320");


insert into drivers (full_name, transport)
values("Andriy", "1");