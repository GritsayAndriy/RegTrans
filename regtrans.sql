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
select * from time_sheet;