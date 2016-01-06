CREATE TABLE PLANTS( 
        Pid INTEGER PRIMARY KEY,
        Pname VARCHAR(20) NOT NULL,
        Clevel INTEGER,
        Gtime1 INTEGER,
        Gtime2 INTEGER,
	Gtime3 INTEGER,
        Gtime4 INTEGER,
	Hquantity DOUBLE,
        StNames1 VARCHAR(20),
        StNames2 VARCHAR(20),
	StNames3 VARCHAR(20),
        StNames4 VARCHAR(20),
	DeadStage VARCHAR(20),
        Penvironment VARCHAR(20),
        Nenvironment VARCHAR(20)
);
INSERT INTO PLANTS VALUES( 1001, 'Lettuce', 1, 20, 0, 1500, 3000, 3500, 'seed', 'Lettuce', 'seed', 'Lettuce','Lettuce', 'GRASSLAND', 'ARID');
INSERT INTO PLANTS VALUES( 1002, 'Strawberry', 1, 20, 0, 1500, 3000, 3500, 'seed', 'Lettuce', 'seed', 'Lettuce','Lettuce', 'GRASSLAND', 'ARID');
INSERT INTO PLANTS VALUES( 1003, 'Cotton', 1, 20, 0, 1500, 3000, 3500, 'seed', 'Lettuce', 'seed', 'Lettuce','Lettuce', 'ARID', 'MARSH');
INSERT INTO PLANTS VALUES( 1004, 'Sugarcane', 1, 20, 0, 1500, 3000, 3500, 'seed', 'Lettuce', 'seed', 'Lettuce','Lettuce', 'MARSH', 'ROCKY');
INSERT INTO PLANTS VALUES( 1005, 'Corn', 1, 20, 0, 1500, 3000, 3500, 'seed', 'Lettuce', 'seed', 'Lettuce','Lettuce', 'GRASSLAND', 'ARID');
INSERT INTO PLANTS VALUES( 1006, 'Apple', 1, 20, 0, 1500, 3000, 3500, 'seed', 'Lettuce', 'seed', 'Lettuce','Lettuce', 'FOREST', 'ROCKY');
INSERT INTO PLANTS VALUES( 1007, 'Pear', 1, 20, 0, 1500, 3000, 3500, 'seed', 'Lettuce', 'seed', 'Lettuce','Lettuce', 'FOREST', 'ROCKY');
INSERT INTO PLANTS VALUES( 1008, 'Mango', 1, 20, 0, 1500, 3000, 3500, 'seed', 'Lettuce', 'seed', 'Lettuce','Lettuce', 'FOREST', 'ROCKY');
INSERT INTO PLANTS VALUES( 1009, 'Banana', 1, 20, 0, 1500, 3000, 3500, 'seed', 'Lettuce', 'seed', 'Lettuce','Lettuce', 'FOREST', 'ROCKY');
INSERT INTO PLANTS VALUES( 1010, 'Lemon', 1, 20, 0, 1500, 3000, 3500, 'seed', 'Lettuce', 'seed', 'Lettuce','Lettuce', 'FOREST', 'ROCKY');


 