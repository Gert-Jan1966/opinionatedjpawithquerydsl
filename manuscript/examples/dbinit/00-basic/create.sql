DROP ALL OBJECTS;

CREATE TABLE Breed (
  id INT IDENTITY PRIMARY KEY,
  name VARCHAR(100)
);

CREATE TABLE Dog (
  id INT IDENTITY PRIMARY KEY,
  name VARCHAR(100),
  breed_id INT,

  CONSTRAINT FK_Dog_breed_id FOREIGN KEY (breed_id) REFERENCES Breed (id)
);