-- CREATE THE DATABASE AND USE IT
CREATE DATABASE bookstudio_db;
USE bookstudio_db;

-- CREATE TABLES
-- Faculties table
CREATE TABLE Faculties (
    FacultyID INT AUTO_INCREMENT PRIMARY KEY,
    FacultyName VARCHAR(255) UNIQUE NOT NULL
);

-- LiteraryGenres table
CREATE TABLE LiteraryGenres (
    LiteraryGenreID INT AUTO_INCREMENT PRIMARY KEY,
    GenreName VARCHAR(255) UNIQUE NOT NULL
);

-- Genres table
CREATE TABLE Genres (
    GenreID INT AUTO_INCREMENT PRIMARY KEY,
    GenreName VARCHAR(255) UNIQUE NOT NULL
);

-- Authors table
CREATE TABLE Authors (
    AuthorID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    Nationality VARCHAR(100) NOT NULL,
    LiteraryGenreID INT NOT NULL,
    BirthDate DATE NOT NULL,
    Biography TEXT,
    Status ENUM('activo', 'inactivo') DEFAULT 'activo',
    Photo LONGBLOB,
    FOREIGN KEY (LiteraryGenreID) REFERENCES LiteraryGenres(LiteraryGenreID)
);

-- Publishers table
CREATE TABLE Publishers (
    PublisherID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    Nationality VARCHAR(100) NOT NULL,
    LiteraryGenreID INT NOT NULL,
    FoundationYear INT NOT NULL,
    Website VARCHAR(255),
    Address VARCHAR(255),
    Status ENUM('activo', 'inactivo') DEFAULT 'activo',
    Photo LONGBLOB,
    FOREIGN KEY (LiteraryGenreID) REFERENCES LiteraryGenres(LiteraryGenreID)
);

-- Courses table
CREATE TABLE Courses (
    CourseID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    Level VARCHAR(100) NOT NULL,
    Description TEXT,
    Status ENUM('activo', 'inactivo') DEFAULT 'activo'
);

-- Students table
CREATE TABLE Students (
    StudentID INT AUTO_INCREMENT PRIMARY KEY,
    DNI VARCHAR(8) UNIQUE NOT NULL,
    FirstName VARCHAR(255) NOT NULL,
    LastName VARCHAR(255) NOT NULL,
    Address VARCHAR(255) NOT NULL,
    Phone VARCHAR(9) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    BirthDate DATE NOT NULL,
    Gender VARCHAR(10) NOT NULL,
    FacultyID INT NOT NULL,
    Status ENUM('activo', 'inactivo') DEFAULT 'activo',
    FOREIGN KEY (FacultyID) REFERENCES Faculties(FacultyID)
);

-- Books table
CREATE TABLE Books (
    BookID INT AUTO_INCREMENT PRIMARY KEY,
    Title VARCHAR(255) NOT NULL,
    TotalCopies INT NOT NULL,
    LoanedCopies INT NOT NULL,
    AuthorID INT NOT NULL,
    PublisherID INT NOT NULL,
    CourseID INT NOT NULL,
    ReleaseDate DATE NOT NULL,
    GenreID INT NOT NULL,
    Status ENUM('activo', 'inactivo') DEFAULT 'activo',
    FOREIGN KEY (AuthorID) REFERENCES Authors(AuthorID),
    FOREIGN KEY (PublisherID) REFERENCES Publishers(PublisherID),
    FOREIGN KEY (CourseID) REFERENCES Courses(CourseID),
    FOREIGN KEY (GenreID) REFERENCES Genres(GenreID)
);

-- Loans table
CREATE TABLE Loans (
    LoanID INT AUTO_INCREMENT PRIMARY KEY,
    BookID INT NOT NULL,
    StudentID INT NOT NULL,
    LoanDate DATE NOT NULL,
    ReturnDate DATE NOT NULL,
    Quantity INT NOT NULL,
    Status ENUM('prestado', 'devuelto') DEFAULT 'prestado',
    Observation TEXT,
    FOREIGN KEY (BookID) REFERENCES Books(BookID),
    FOREIGN KEY (StudentID) REFERENCES Students(StudentID)
);

-- Users table
CREATE TABLE Users (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    FirstName VARCHAR(255) NOT NULL,
    LastName VARCHAR(255) NOT NULL,
    Password VARCHAR(255) NOT NULL,
    Role ENUM('administrador', 'bibliotecario') NOT NULL,
    ProfilePhoto LONGBLOB
);

-- INSERT DATA
-- Faculties
INSERT INTO Faculties (FacultyName) VALUES
('Ingeniería'), ('Ciencias Sociales'), ('Medicina'), ('Derecho'), ('Arquitectura'),
('Economía'), ('Psicología'), ('Ciencias de la Computación'), ('Biología'), ('Filosofía'),
('Educación'), ('Artes Visuales'), ('Música'), ('Física'), ('Química'), ('Comunicación'),
('Historia'), ('Geografía'), ('Matemáticas'), ('Idiomas Extranjeros'), ('Ciencias Políticas'),
('Enfermería'), ('Odontología'), ('Veterinaria'), ('Nutrición');

-- LiteraryGenres
INSERT INTO LiteraryGenres (GenreName) VALUES
('Realismo mágico'), ('Fantasía'), ('Ficción'), ('Poesía'), ('Drama'),
('Ensayo'), ('Ciencia Ficción'), ('Biografía'), ('Misterio'), ('Aventura'),
('Crónica'), ('Novela histórica'), ('Terror'), ('Romance'), ('Filosofía'),
('Autoayuda'), ('Periodismo'), ('Narrativa'), ('Comedia'), ('Tragedia'),
('Lírica'), ('Cuento'), ('Teatro'), ('Crítica literaria'), ('Literatura infantil');

-- Genres
INSERT INTO Genres (GenreName) VALUES
('Realismo mágico'), ('Fantasía'), ('Ficción'), ('Poesía'), ('Drama'),
('Ensayo'), ('Ciencia Ficción'), ('Biografía'), ('Misterio'), ('Aventura'),
('Crónica'), ('Matemáticas'), ('Novela histórica'), ('Terror'), ('Romance'),
('Filosofía'), ('Autoayuda'), ('Periodismo'), ('Narrativa'), ('Comedia'),
('Tragedia'), ('Lírica'), ('Física'), ('Química'), ('Biología');

-- Authors
INSERT INTO Authors (Name, Nationality, LiteraryGenreID, BirthDate, Status) VALUES
('Gabriel García Márquez', 'Colombiana', 1, '1927-03-06', 'activo'),
('J.K. Rowling', 'Británica', 2, '1965-07-31', 'activo'),
('Mario Vargas Llosa', 'Peruana', 3, '1936-03-28', 'activo'),
('Isabel Allende', 'Chilena', 4, '1942-08-02', 'activo'),
('Jorge Luis Borges', 'Argentina', 3, '1899-08-24', 'activo'),
('Pablo Neruda', 'Chilena', 4, '1904-07-12', 'activo'),
('Julio Cortázar', 'Argentina', 3, '1914-08-26', 'activo'),
('Miguel de Cervantes', 'Española', 3, '1547-09-29', 'activo'),
('Federico García Lorca', 'Española', 4, '1898-06-05', 'activo'),
('Octavio Paz', 'Mexicana', 4, '1914-03-31', 'activo'),
('Ernest Hemingway', 'Estadounidense', 3, '1899-07-21', 'activo'),
('Virginia Woolf', 'Británica', 3, '1882-01-25', 'activo'),
('Franz Kafka', 'Checoslovaca', 3, '1883-07-03', 'activo'),
('Charles Dickens', 'Británica', 3, '1812-02-07', 'activo'),
('Stephen King', 'Estadounidense', 2, '1947-09-21', 'activo'),
('Emily Dickinson', 'Estadounidense', 4, '1830-12-10', 'activo'),
('Albert Camus', 'Francesa', 6, '1913-11-07', 'activo'),
('Gabriela Mistral', 'Chilena', 4, '1889-04-07', 'activo'),
('Oscar Wilde', 'Irlandesa', 5, '1854-10-16', 'activo'),
('Edgar Allan Poe', 'Estadounidense', 9, '1809-01-19', 'activo'),
('Juan Rulfo', 'Mexicana', 3, '1917-05-16', 'activo'),
('George Orwell', 'Británica', 3, '1903-06-25', 'activo'),
('Mary Shelley', 'Británica', 7, '1797-08-30', 'activo'),
('Miguel Ángel Asturias', 'Guatemalteca', 1, '1899-10-19', 'activo'),
('Agatha Christie', 'Británica', 9, '1890-09-15', 'activo');

-- Publishers
INSERT INTO Publishers (Name, Nationality, LiteraryGenreID, FoundationYear, Website, Address, Status) VALUES
('Penguin Random House', 'Estados Unidos', 3, 1925, 'https://www.penguinrandomhouse.com', '555 Editorial St', 'activo'),
('HarperCollins', 'Estados Unidos', 3, 1817, 'https://www.harpercollins.com', '789 Harper Ave', 'activo'),
('Alfaguara', 'España', 3, 1964, 'https://www.alfaguara.com', 'Calle Editores 123', 'activo'),
('Planeta', 'España', 3, 1949, 'https://www.planeta.es', 'Av. Diagonal 662', 'activo'),
('Anagrama', 'España', 3, 1969, 'https://www.anagrama-ed.es', 'Calle Anagrama 45', 'activo'),
('Tusquets', 'España', 3, 1969, 'https://www.tusquetseditores.com', 'Av. Diagonal 928', 'activo'),
('Seix Barral', 'España', 3, 1911, 'https://www.seix-barral.es', 'Av. Editorial 78', 'activo'),
('Salamandra', 'España', 2, 1989, 'https://www.salamandra.info', 'Calle Salamandra 12', 'activo'),
('Ediciones B', 'España', 3, 1986, 'https://www.edicionesb.com', 'Plaza Editores 34', 'activo'),
('Siruela', 'España', 2, 1982, 'https://www.siruela.com', 'Calle del Libro 56', 'activo'),
('Gallimard', 'Francia', 3, 1911, 'https://www.gallimard.fr', 'Rue de Grenelle 5', 'activo'),
('Oxford University Press', 'Reino Unido', 6, 1586, 'https://www.oup.com', 'Great Clarendon Street', 'activo'),
('Pearson', 'Reino Unido', 6, 1844, 'https://www.pearson.com', 'London Bridge St', 'activo'),
('McGraw-Hill', 'Estados Unidos', 6, 1917, 'https://www.mheducation.com', 'Pennsylvania Ave', 'activo'),
('Fondo de Cultura Económica', 'México', 6, 1934, 'https://www.fondodeculturaeconomica.com', 'Av. del Libro 78', 'activo'),
('Hachette', 'Francia', 3, 1826, 'https://www.hachette.com', 'Rue de Fleurus 15', 'activo'),
('Simon & Schuster', 'Estados Unidos', 3, 1924, 'https://www.simonandschuster.com', 'Rockefeller Plaza', 'activo'),
('Macmillan', 'Reino Unido', 3, 1843, 'https://www.macmillan.com', 'Hampshire Road 12', 'activo'),
('Springer', 'Alemania', 6, 1842, 'https://www.springer.com', 'Tiergartenstrasse 17', 'activo'),
('Wiley', 'Estados Unidos', 6, 1807, 'https://www.wiley.com', 'River Street 111', 'activo'),
('Elsevier', 'Países Bajos', 6, 1880, 'https://www.elsevier.com', 'Radarweg 29', 'activo'),
('Paidós', 'Argentina', 6, 1945, 'https://www.paidos.com', 'Av. Independencia 1668', 'activo'),
('Grijalbo', 'México', 3, 1939, 'https://www.grijalbo.com', 'Av. Insurgentes 1822', 'activo'),
('Sudamericana', 'Argentina', 3, 1939, 'https://www.sudamericana.com', 'Humberto I 555', 'activo'),
('Cambridge University Press', 'Reino Unido', 6, 1534, 'https://www.cambridge.org', 'Cambridge Street 23', 'activo');

-- Courses
INSERT INTO Courses (Name, Level, Description, Status) VALUES
('Matemáticas Avanzadas', 'Avanzado', 'Curso sobre álgebra, geometría y cálculo', 'activo'),
('Literatura Contemporánea', 'Intermedio', 'Estudio de la literatura moderna y sus géneros', 'activo'),
('Física Cuántica', 'Avanzado', 'Introducción a los principios de la física cuántica', 'activo'),
('Historia del Arte', 'Intermedio', 'Recorrido por las principales corrientes artísticas', 'activo'),
('Programación en Python', 'Básico', 'Fundamentos de programación con Python', 'activo'),
('Economía Internacional', 'Avanzado', 'Estudio de mercados y políticas económicas globales', 'activo'),
('Biología Molecular', 'Avanzado', 'Estudio de las moléculas que componen las células', 'activo'),
('Derecho Constitucional', 'Intermedio', 'Análisis de la constitución y sus principios', 'activo'),
('Psicología Cognitiva', 'Avanzado', 'Estudio de los procesos mentales del aprendizaje', 'activo'),
('Inglés Técnico', 'Intermedio', 'Vocabulario especializado para profesionales', 'activo'),
('Química Orgánica', 'Avanzado', 'Estudio de compuestos orgánicos y sus reacciones', 'activo'),
('Arquitectura Sostenible', 'Avanzado', 'Diseño de edificios ecológicos y eficientes', 'activo'),
('Filosofía Contemporánea', 'Intermedio', 'Corrientes filosóficas de los siglos XX y XXI', 'activo'),
('Marketing Digital', 'Básico', 'Estrategias de promoción en medios digitales', 'activo'),
('Estadística Aplicada', 'Intermedio', 'Métodos estadísticos para análisis de datos', 'activo'),
('Diseño Gráfico', 'Básico', 'Fundamentos de composición y diseño', 'activo'),
('Literatura Latinoamericana', 'Intermedio', 'Obras destacadas de autores latinoamericanos', 'activo'),
('Inteligencia Artificial', 'Avanzado', 'Algoritmos y aplicaciones de IA', 'activo'),
('Historia Medieval', 'Intermedio', 'Europa durante la Edad Media', 'activo'),
('Anatomía Humana', 'Básico', 'Estudio de la estructura del cuerpo humano', 'activo'),
('Contabilidad Financiera', 'Intermedio', 'Principios de contabilidad para empresas', 'activo'),
('Energías Renovables', 'Avanzado', 'Tecnologías para la generación de energía sostenible', 'activo'),
('Sociología Urbana', 'Intermedio', 'Análisis de las dinámicas sociales en entornos urbanos', 'activo'),
('Teoría Musical', 'Básico', 'Fundamentos de notación y composición musical', 'activo'),
('Desarrollo Web', 'Intermedio', 'Creación de sitios web con HTML, CSS y JavaScript', 'activo');

-- Students
INSERT INTO Students (DNI, FirstName, LastName, Address, Phone, Email, BirthDate, Gender, FacultyID, Status) VALUES
('60916631', 'Juan', 'Pérez', 'Calle Ficticia 123', '987654321', 'juan.perez@correo.com', '2001-05-02', 'Masculino', 1, 'activo'),
('76179425', 'Ana', 'González', 'Calle Real 456', '123456789', 'ana.gonzalez@correo.com', '1998-08-10', 'Femenino', 2, 'activo'),
('76543210', 'Carlos', 'Martínez', 'Av. Principal 789', '987123456', 'carlos.martinez@correo.com', '2000-03-15', 'Masculino', 3, 'activo'),
('87654321', 'María', 'López', 'Jr. Los Álamos 456', '912345678', 'maria.lopez@correo.com', '1999-11-20', 'Femenino', 4, 'activo'),
('65432109', 'Pedro', 'Ramírez', 'Calle Las Flores 123', '945678912', 'pedro.ramirez@correo.com', '2001-07-22', 'Masculino', 5, 'activo'),
('54321098', 'Laura', 'Torres', 'Av. Los Pinos 456', '956789123', 'laura.torres@correo.com', '2002-01-10', 'Femenino', 6, 'activo'),
('43210987', 'Javier', 'García', 'Jr. Las Palmeras 789', '967891234', 'javier.garcia@correo.com', '2000-09-05', 'Masculino', 7, 'activo'),
('32109876', 'Sofía', 'Díaz', 'Calle Los Olivos 321', '978912345', 'sofia.diaz@correo.com', '1999-04-17', 'Femenino', 8, 'activo'),
('21098765', 'Miguel', 'Rodríguez', 'Av. Los Laureles 654', '989123456', 'miguel.rodriguez@correo.com', '2001-12-03', 'Masculino', 9, 'activo'),
('10987654', 'Valentina', 'Fernández', 'Jr. Los Cipreses 987', '991234567', 'valentina.fernandez@correo.com', '2002-06-28', 'Femenino', 10, 'activo'),
('09876543', 'Daniel', 'Castro', 'Calle Los Cedros 159', '912345678', 'daniel.castro@correo.com', '2000-05-14', 'Masculino', 11, 'activo'),
('98765432', 'Carolina', 'Vargas', 'Av. Los Ficus 753', '923456789', 'carolina.vargas@correo.com', '1999-08-29', 'Femenino', 1, 'activo'),
('87654329', 'Alejandro', 'Mendoza', 'Jr. Los Sauces 951', '934567891', 'alejandro.mendoza@correo.com', '2001-02-11', 'Masculino', 2, 'activo'),
('76543219', 'Camila', 'Herrera', 'Calle Los Pinos 357', '945678912', 'camila.herrera@correo.com', '2002-10-25', 'Femenino', 3, 'activo'),
('65432198', 'Gabriel', 'Rivera', 'Av. Los Robles 159', '956789123', 'gabriel.rivera@correo.com', '2000-07-18', 'Masculino', 4, 'activo'),
('54321987', 'Valeria', 'Morales', 'Jr. Los Manzanos 753', '967891234', 'valeria.morales@correo.com', '1999-01-31', 'Femenino', 5, 'activo'),
('43219876', 'Sebastián', 'Ortega', 'Calle Los Naranjos 951', '978912345', 'sebastian.ortega@correo.com', '2001-09-07', 'Masculino', 6, 'activo'),
('32198765', 'Lucía', 'Navarro', 'Av. Los Cerezos 357', '989123456', 'lucia.navarro@correo.com', '2002-03-22', 'Femenino', 7, 'activo'),
('21987654', 'Mateo', 'Campos', 'Jr. Los Almendros 159', '991234567', 'mateo.campos@correo.com', '2000-11-13', 'Masculino', 8, 'activo'),
('19876543', 'Isabella', 'Rojas', 'Calle Los Nogales 753', '912345678', 'isabella.rojas@correo.com', '1999-06-26', 'Femenino', 9, 'activo'),
('09876542', 'Santiago', 'Acosta', 'Av. Los Eucaliptos 951', '923456789', 'santiago.acosta@correo.com', '2001-04-09', 'Masculino', 10, 'activo'),
('98765431', 'Mariana', 'Flores', 'Jr. Los Pinos 357', '934567891', 'mariana.flores@correo.com', '2002-08-23', 'Femenino', 11, 'activo'),
('87654328', 'David', 'Gutiérrez', 'Calle Los Cedros 159', '945678912', 'david.gutierrez@correo.com', '2000-01-16', 'Masculino', 1, 'activo'),
('76543218', 'Renata', 'Chávez', 'Av. Los Ficus 753', '956789123', 'renata.chavez@correo.com', '1999-10-30', 'Femenino', 2, 'activo'),
('65432197', 'Lucas', 'Medina', 'Jr. Los Sauces 951', '967891234', 'lucas.medina@correo.com', '2001-05-12', 'Masculino', 3, 'activo');

-- Books
INSERT INTO Books (Title, TotalCopies, LoanedCopies, AuthorID, PublisherID, CourseID, ReleaseDate, GenreID, Status) VALUES
('Álgebra Superior', 50, 1, 1, 1, 1, '2020-01-15', 12, 'activo'),
('Harry Potter y la piedra filosofal', 55, 1, 2, 2, 2, '1997-06-26', 2, 'activo'),
('Cien años de soledad', 30, 2, 1, 3, 15, '1967-05-30', 1, 'activo'),
('1984', 25, 1, 18, 4, 11, '1949-06-08', 7, 'activo'),
('Don Quijote de la Mancha', 20, 0, 4, 5, 2, '1605-01-16', 3, 'activo'),
('Rayuela', 15, 1, 3, 6, 15, '1963-06-28', 3, 'activo'),
('El amor en los tiempos del cólera', 18, 0, 1, 7, 15, '1985-12-08', 3, 'activo'),
('La metamorfosis', 22, 1, 9, 8, 11, '1915-10-15', 3, 'activo'),
('Poema 20', 12, 0, 2, 9, 15, '1924-03-14', 4, 'activo'),
('Pedro Páramo', 17, 0, 17, 10, 15, '1955-03-27', 3, 'activo'),
('Orgullo y prejuicio', 24, 1, 8, 11, 15, '1813-01-28', 3, 'activo'),
('El principito', 35, 2, 6, 12, 15, '1943-04-06', 10, 'activo'),
('El resplandor', 20, 1, 11, 13, 10, '1977-01-28', 2, 'activo'),
('Frankenstein', 15, 0, 19, 14, 15, '1818-01-01', 7, 'activo'),
('Ficciones', 18, 1, 5, 15, 15, '1944-06-14', 3, 'activo'),
('Oliver Twist', 22, 0, 10, 16, 15, '1838-02-01', 3, 'activo'),
('La naranja mecánica', 17, 1, 18, 17, 11, '1962-05-22', 7, 'activo'),
('Física Universitaria', 40, 2, 8, 18, 3, '2018-03-15', 11, 'activo'),
('El extranjero', 25, 0, 13, 19, 11, '1942-05-19', 3, 'activo'),
('El retrato de Dorian Gray', 20, 1, 15, 20, 2, '1890-07-01', 3, 'activo'),
('Cuentos de la selva', 30, 0, 20, 21, 22, '1918-01-01', 11, 'activo'),
('Química General', 45, 3, 7, 22, 9, '2019-08-12', 12, 'activo'),
('Los miserables', 22, 1, 4, 23, 17, '1862-04-03', 3, 'activo'),
('Antología poética', 15, 0, 14, 24, 15, '1958-09-10', 4, 'activo'),
('El laberinto de la soledad', 18, 1, 6, 25, 11, '1950-02-18', 6, 'activo');

-- Loans
INSERT INTO Loans (BookID, StudentID, LoanDate, ReturnDate, Quantity, Status, Observation) VALUES
-- 2024
(1, 1, '2024-12-01', '2024-12-15', 1, 'devuelto', 'Ninguna observación'),
(2, 2, '2024-12-02', '2024-12-16', 1, 'devuelto', 'Libro en perfecto estado'),
(3, 3, '2024-01-05', '2024-01-17', 1, 'devuelto', 'Devuelto en buen estado'),
(5, 5, '2024-01-10', '2024-01-20', 1, 'devuelto', 'Pequeña mancha en la página 35'),
(7, 7, '2024-01-15', '2024-01-25', 1, 'devuelto', 'Devuelto en perfectas condiciones'),
(9, 9, '2024-01-20', '2024-01-30', 1, 'devuelto', 'Devuelto a tiempo'),
(11, 11, '2024-01-25', '2024-02-05', 1, 'devuelto', 'Sin observaciones'),
(13, 13, '2024-02-03', '2024-02-12', 1, 'devuelto', 'Devuelto en buen estado'),
(15, 15, '2024-02-08', '2024-02-19', 1, 'devuelto', 'Pequeño doblez en la esquina de la portada'),
(17, 17, '2024-02-13', '2024-02-22', 1, 'devuelto', 'Devuelto en perfectas condiciones'),
(19, 19, '2024-02-18', '2024-02-29', 1, 'devuelto', 'Sin observaciones'),
(21, 21, '2024-02-23', '2024-03-07', 1, 'devuelto', 'Pequeñas anotaciones a lápiz'),
-- 2025
(2, 2, '2025-01-05', '2025-01-15', 1, 'devuelto', 'Devuelto en buen estado'),
(4, 4, '2025-01-10', '2025-01-21', 1, 'devuelto', 'Sin observaciones'),
(6, 6, '2025-01-15', '2025-01-26', 1, 'devuelto', 'Devuelto en perfectas condiciones'),
(8, 8, '2025-01-20', '2025-02-01', 1, 'devuelto', 'Pequeña mancha en la contraportada'),
(10, 10, '2025-01-25', '2025-02-06', 1, 'devuelto', 'Devuelto en buen estado'),
(12, 12, '2025-02-03', '2025-02-14', 1, 'devuelto', 'Devuelto en buen estado'),
(14, 14, '2025-02-08', '2025-02-20', 1, 'devuelto', 'Sin observaciones'),
(16, 16, '2025-02-13', '2025-02-24', 1, 'prestado', 'Solicita prórroga de una semana'),
(18, 18, '2025-02-18', '2025-03-01', 1, 'prestado', 'Sin observaciones'),
(20, 20, '2025-02-23', '2025-03-07', 1, 'prestado', 'Préstamo para proyecto de fin de curso');

-- Users
INSERT INTO Users (Username, Email, FirstName, LastName, Password, Role, ProfilePhoto) VALUES
('Admin', 'admin@example.com', 'Admin', 'Admin', 'Admin123@', 'administrador', NULL);