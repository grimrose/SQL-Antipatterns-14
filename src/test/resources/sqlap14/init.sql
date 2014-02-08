-- tables

DROP TABLE IF EXISTS Accounts, BugsProducts, Bugs, Products CASCADE;

CREATE TABLE Accounts (
  account_id   SERIAL PRIMARY KEY,
  account_name VARCHAR(20)
);

CREATE TABLE Bugs (
  bug_id        SERIAL PRIMARY KEY,
  data_reported DATE   NOT NULL,
  reported_by   BIGINT NOT NULL,
  hours         NUMERIC(9, 2),
  FOREIGN KEY (reported_by) REFERENCES Accounts (account_id)
);

CREATE TABLE Products (
  product_id   SERIAL PRIMARY KEY,
  product_name VARCHAR(50)
);

CREATE TABLE BugsProducts (
  bug_id     BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  PRIMARY KEY (bug_id, product_id),
  FOREIGN KEY (bug_id) REFERENCES Bugs (bug_id),
  FOREIGN KEY (product_id) REFERENCES Products (product_id)
);


-- values

INSERT INTO Accounts (account_name) VALUES ('一郎'), ('二郎'), ('三郎');
INSERT INTO Products (product_name) VALUES ('P001'), ('P002'), ('P003');

INSERT INTO Bugs (data_reported, reported_by, hours) VALUES
    ('2013-12-19', 1, 10)
  , ('2014-06-01', 1, 20)
  , ('2014-02-16', 2, 30)
  , ('2014-02-10', 2, 40)
  , ('2014-02-16', 2, 50)
  , ('2014-01-01', 2, 60)
  , ('2013-11-09', 3, 70);

INSERT INTO BugsProducts (bug_id, product_id) VALUES
    (1, 1)
  , (2, 1)
  , (3, 2)
  , (4, 2)
  , (5, 2)
  , (6, 3)
  , (7, 3);
