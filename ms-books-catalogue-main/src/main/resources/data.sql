CREATE TABLE generos_libros(
	id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50),
    descripcion VARCHAR(250),
    PRIMARY KEY (id)
);
INSERT INTO generos_libros (nombre, descripcion) VALUES
('Ficción', 'Libros de narrativa imaginativa, incluyendo novelas y cuentos.'),
('No Ficción', 'Libros basados en hechos reales y conocimientos verificables.'),
('Ciencia Ficción', 'Historias sobre avances científicos y tecnológicos, a menudo en el futuro.'),
('Fantasía', 'Relatos ambientados en mundos mágicos con criaturas y poderes sobrenaturales.'),
('Misterio', 'Novelas centradas en resolver crímenes o enigmas.'),
('Terror', 'Libros diseñados para provocar miedo o tensión en el lector.'),
('Historia', 'Libros que exploran eventos y personajes históricos.'),
('Biografías', 'Historias sobre la vida de personas reales.'),
('Autoayuda', 'Libros con consejos prácticos para mejorar la vida personal y profesional.'),
('Negocios', 'Libros sobre estrategias empresariales, finanzas y liderazgo.'),
('Ciencia', 'Obras que explican conceptos científicos y descubrimientos.'),
('Poesía', 'Colecciones de poemas y obras líricas.'),
('Infantil', 'Libros diseñados para niños, con ilustraciones y lenguaje simple.'),
('Juvenil', 'Libros dirigidos a adolescentes y jóvenes adultos.'),
('Romance', 'Historias centradas en relaciones amorosas y sentimientos.'),
('Aventura', 'Relatos de viajes, exploraciones y desafíos emocionantes.');
CREATE TABLE editoriales_libros(
	id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50),
    descripcion VARCHAR(250),
    PRIMARY KEY (id)
);
INSERT INTO editoriales_libros (nombre, descripcion) VALUES
('Penguin Random House', 'Una de las editoriales más grandes del mundo, con una amplia variedad de géneros y autores.'),
('HarperCollins', 'Editorial internacional con gran presencia en el mercado literario.'),
('Planeta', 'Editorial líder en el mundo hispanohablante, con un extenso catálogo de libros en diversos géneros.'),
('Santillana', 'Especializada en libros educativos y de literatura infantil y juvenil.'),
('Alfaguara', 'Conocida por su enfoque en la literatura de calidad y su apoyo a autores contemporáneos.'),
('Anagrama', 'Editorial independiente que ha publicado a algunos de los escritores más importantes en lengua española.'),
('Ediciones B', 'Parte del Grupo Planeta, publica libros de ficción, no ficción y literatura juvenil.'),
('Siglo XXI Editores', 'Especializada en ciencias sociales y humanidades, con un enfoque en pensamiento crítico.'),
('Roca Editorial', 'Editorial que abarca una amplia gama de géneros, desde novela histórica hasta thriller y ciencia ficción.'),
('Ediciones SM', 'Famosa por sus libros educativos y literatura infantil y juvenil.');

CREATE TABLE libros(
	id INT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(200) NOT NULL,
    autor VARCHAR(200) NOT NULL,
    editorial VARCHAR(200) NOT NULL,
    anio INT,
    isbn VARCHAR(30),
    resumen VARCHAR(500),
    genero VARCHAR(50) NOT NULL,
    precio FLOAT NOT NULL,    
    imagen VARCHAR(500),
    stock INT NOT NULL DEFAULT 0,
    visible BOOLEAN NOT NULL DEFAULT false,
    valoracion INT DEFAULT 0,
    PRIMARY KEY (id)
);
INSERT INTO libros (titulo, autor, editorial, anio, isbn, resumen, genero, precio, imagen) VALUES
('To Kill a Mockingbird', 'Harper Lee', 'Planeta', 1995, '978-0-06-112008-4','A novel set in the Great Depression, focusing on the Finch family and the moral growth of Scout and Jem as their father, Atticus, defends a black man falsely accused of raping a white woman.','Fantasía', 19.90,'https://covers.openlibrary.org/b/id/8225261-L.jpg'),
('1984', 'George Orwell', 'Roca Editorial', 1984, '978-0-452-28423-4','A dystopian novel exploring themes of totalitarianism, surveillance, and individuality through the experiences of Winston Smith in a tightly controlled society.','Juvenil', 16.50,'https://covers.openlibrary.org/b/id/7222246-L.jpg'),
('Pride and Prejudice', 'Jane Austen', 'Siglo XXI Editores', 2022, '978-0-19-953556-9','A romantic novel that delves into issues of class, family, and societal expectations through the evolving relationship between Elizabeth Bennet and Mr. Darcy.','Aventura',15.00,'https://covers.openlibrary.org/b/id/8231856-L.jpg'),
('The Great Gatsby', 'F. Scott Fitzgerald', 'Santillana', 1999, '978-0-7432-7356-5','A story about the mysterious Jay Gatsby, his unrequited love for Daisy Buchanan, and the critique of the American Dream in the 1920s.','Ciencia',10.20,'https://covers.openlibrary.org/b/id/7222161-L.jpg'),
('Moby Dick', 'Herman Melville', 'Anagrama', 2004, '978-0-14-243724-7','A thrilling adventure of Captain Ahabs obsessive quest to kill the white whale, Moby Dick, which explores themes of revenge, fate, and the sea.','Ciencia Ficción',25.50,'https://covers.openlibrary.org/b/id/7222247-L.jpg'),
('The Catcher in the Rye', 'J.D. Salinger', 'Alfaguara', 2024, '978-0-316-76948-0','The experiences of Holden Caulfield, a disillusioned teenager navigating the challenges of adolescence in a world he sees as phony.','Biografías',16.50,'https://covers.openlibrary.org/b/id/8226191-L.jpg'),
('The Hobbit', 'J.R.R. Tolkien', 'HarperCollins', 2016, '978-0-618-26030-0','The journey of Bilbo Baggins, a hobbit who is thrust into an adventure involving dwarves, a dragon, and a quest for treasure.','Terror',8.00,'https://covers.openlibrary.org/b/id/6979861-L.jpg');