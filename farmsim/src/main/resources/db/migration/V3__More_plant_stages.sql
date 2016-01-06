DROP TABLE PLANTS;

CREATE TABLE PLANTS( 
        Pid INTEGER PRIMARY KEY,
        Pname VARCHAR(20) NOT NULL,
        Clevel INTEGER,
        Gtime1 INTEGER,
        Gtime2 INTEGER,
        Gtime3 INTEGER,
        Gtime4 INTEGER,
	Gtime5 INTEGER,
        Hquantity INTEGER,
        StNames1 VARCHAR(20),
        StNames2 VARCHAR(20),
        StNames3 VARCHAR(20),
        StNames4 VARCHAR(20),
        DeadStage1 VARCHAR(20),
	DeadStage2 VARCHAR(20),
	DeadStage3 VARCHAR(20),
        Penvironment VARCHAR(20),
        Nenvironment VARCHAR(20)
);

INSERT INTO PLANTS VALUES( 1001, 'Lettuce', 1, 0, 80000, 160000, 240000, 360000, 2, 'seed', 'Lettuce1', 'Lettuce2', 'Lettuce3', 'Lettuce1dead', 'Lettuce2dead', 'Lettuce3dead', 'GRASSLAND', 'ARID');
INSERT INTO PLANTS VALUES( 1002, 'Strawberry', 1, 0, 160000, 320000, 480000, 720000, 4, 'seed', 'Strawberry1', 'Strawberry2', 'Strawberry3', 'Strawberry1dead', 'Strawberry2dead', 'Strawberry3dead', 'GRASSLAND', 'ARID');
INSERT INTO PLANTS VALUES( 1003, 'Cotton', 1, 0, 200000, 400000, 600000, 900000, 4, 'seed', 'Cotton1', 'Cotton2', 'Cotton3', 'Cotton1dead', 'Cotton2dead', 'Cotton3dead', 'ARID', 'MARSH');
INSERT INTO PLANTS VALUES( 1004, 'Sugarcane', 1, 0, 240000, 480000, 720000, 1080000, 6, 'seed', 'Sugarcane1', 'Sugarcane2', 'Sugarcane3', 'Sugarcane1dead', 'Sugarcane2dead', 'Sugarcane3dead', 'MARSH', 'ROCKY');
INSERT INTO PLANTS VALUES( 1005, 'Corn', 1, 0, 280000, 560000, 840000, 1260000, 6, 'seed', 'Corn1', 'Corn2', 'Corn3', 'Corn1dead', 'Corn2dead', 'Corn3dead', 'GRASSLAND', 'ARID');
INSERT INTO PLANTS VALUES( 1006, 'Apple', 1, 0, 320000, 620000, 960000, 1440000, 8, 'seed', 'Apple1', 'Apple2', 'Apple3', 'Apple1dead', 'Apple2dead', 'Apple3dead', 'FOREST', 'ROCKY');
INSERT INTO PLANTS VALUES( 1007, 'Pear', 1, 0, 360000, 720000, 1080000, 1620000, 10, 'seed', 'Pear1', 'Pear2', 'Pear3', 'Pear1dead', 'Pear2dead', 'Pear3dead', 'FOREST', 'ROCKY');
INSERT INTO PLANTS VALUES( 1008, 'Mango', 1, 0, 400000, 800000, 1200000, 1800000, 10, 'seed', 'Mango1', 'Mango2', 'Mango3', 'Mango1dead', 'Mango2dead', 'Mango3dead', 'FOREST', 'ROCKY');
INSERT INTO PLANTS VALUES( 1009, 'Banana', 1, 0, 480000, 960000, 1440000, 2160000, 12, 'seed', 'Banana1', 'Banana2', 'Banana3', 'Banana1dead', 'Banana2dead', 'Banana3dead', 'FOREST', 'ROCKY');
INSERT INTO PLANTS VALUES( 1010, 'Lemon', 1, 0, 560000, 1200000, 1680000, 2520000, 14, 'seed', 'Lemon1', 'Lemon2', 'Lemon3', 'Lemon1dead', 'Lemon2dead', 'Lemon3dead', 'FOREST', 'ROCKY');