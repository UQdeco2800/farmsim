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
        DeadStage VARCHAR(20),
        Penvironment VARCHAR(20),
        Nenvironment VARCHAR(20)
);

INSERT INTO PLANTS VALUES( 1001, 'Lettuce', 1, 0, 500, 1000, 1500, 11500, 2, 'seed', 'lettuce1', 'lettuce2', 'lettuce3','lettucedead', 'GRASSLAND', 'ARID');
INSERT INTO PLANTS VALUES( 1002, 'Strawberry', 1, 0, 800, 1600, 2400, 12400, 4, 'seed', 'strawberry1', 'strawberry2', 'strawberry3','strawberrydead', 'GRASSLAND', 'ARID');
INSERT INTO PLANTS VALUES( 1003, 'Cotton', 1, 0, 1000, 2000, 3000, 13000, 4, 'seed', 'cotton1', 'cotton2', 'cotton3','cottondead', 'ARID', 'MARSH');
INSERT INTO PLANTS VALUES( 1004, 'Sugarcane', 1, 0, 1300, 2600, 3900, 13900, 6, 'seed', 'sugarcane1', 'sugarcane2', 'sugarcane3','sugarcanedead', 'MARSH', 'ROCKY');
INSERT INTO PLANTS VALUES( 1005, 'Corn', 1, 0, 1500, 3000, 3500, 13500, 6, 'seed', 'corn1', 'corn2', 'corn3','corndead', 'GRASSLAND', 'ARID');
INSERT INTO PLANTS VALUES( 1006, 'Apple', 1, 0, 1800,3600, 5400, 15400, 8, 'seed', 'apple1', 'apple2', 'apple3','appledead', 'FOREST', 'ROCKY');
INSERT INTO PLANTS VALUES( 1007, 'Pear', 1, 0, 2000, 4000, 6000, 16000, 10, 'seed', 'pear1', 'pear2', 'pear3','peardead', 'FOREST', 'ROCKY');
INSERT INTO PLANTS VALUES( 1008, 'Mango', 1, 0, 2200, 4400, 6600, 16600, 10, 'seed', 'mango1', 'mango2', 'mango3','mangodead', 'FOREST', 'ROCKY');
INSERT INTO PLANTS VALUES( 1009, 'Banana', 1, 0, 2500, 5000, 7500, 17500, 12, 'seed', 'banana1', 'banana2', 'banana3','bananadead', 'FOREST', 'ROCKY');
INSERT INTO PLANTS VALUES( 1010, 'Lemon', 1, 0, 2700, 5400, 8100, 18100, 14, 'seed', 'lemon1', 'lemon2', 'lemon3','lemondead', 'FOREST', 'ROCKY');
