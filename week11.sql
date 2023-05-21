USE projects;


DROP TABLE IF EXISTS project_category;
DROP TABLE IF EXISTS material;
DROP TABLE IF EXISTS step;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS project;

CREATE TABLE project (
  project_id INT AUTO_INCREMENT NOT NULL,
  project_name VARCHAR(128) NOT NULL,
  estimated_hours DECIMAL(7, 2),
  actual_hours DECIMAL(7, 2),
  difficulty INT,
  notes TEXT,
  PRIMARY KEY (project_id)
);

CREATE TABLE category (
  category_id INT AUTO_INCREMENT NOT NULL,
  category_name VARCHAR(128) NOT NULL,
  PRIMARY KEY (category_id)
);

CREATE TABLE step (
  step_id INT AUTO_INCREMENT NOT NULL,
  project_id INT NOT NULL,
  step_text TEXT NOT NULL,
  step_order INT NOT NULL,
  PRIMARY KEY (step_id),
  FOREIGN KEY (project_id) REFERENCES project (project_id) ON DELETE CASCADE
);

CREATE TABLE material (
  material_id INT AUTO_INCREMENT NOT NULL,
  project_id INT NOT NULL,
  material_name VARCHAR(128) NOT NULL,
  num_required INT,
  cost DECIMAL(7, 2),
  PRIMARY KEY (material_id),
  FOREIGN KEY (project_id) REFERENCES project (project_id) ON DELETE CASCADE
);

CREATE TABLE project_category (
  project_id INT NOT NULL,
  category_id INT NOT NULL,
  FOREIGN KEY (project_id) REFERENCES project (project_id) ON DELETE CASCADE,
  FOREIGN KEY (category_id) REFERENCES category (category_id) ON DELETE CASCADE,
  UNIQUE KEY (project_id, category_id)
);

INSERT INTO category (category_name) VALUES ('Doors and Windows');

SELECT * FROM project;

INSERT INTO material (project_id, material_name, num_required)
VALUES
(1, '2-inch screws', 20);



SELECT m.project_id
FROM material m
LEFT JOIN project p ON m.project_id = p.project_id
WHERE p.project_id IS NULL;

SET FOREIGN_KEY_CHECKS=1;

CREATE TABLE project_category (
project_id INT NOT NULL,
category_id INT NOT NULL,
FOREIGN KEY (project_id) REFERENCES projects (project_id) ON DELETE CASCADE,
FOREIGN KEY (category_id) REFERENCES category (category_id) ON DELETE CASCADE,
UNIQUE KEY (project_id, category_id)
);

-- Add some data

INSERT INTO project (project_name, estimated_hours, actual_hours, difficulty, notes) 
VALUES ('Project 1', 10.5, 8.75, 3, 'Example project 1 notes');

INSERT INTO project (project_name, estimated_hours, actual_hours, difficulty, notes) 
VALUES ('Project 2', 20.25, 15.5, 2, 'Example project 2 notes');

INSERT INTO category (category_name) 
VALUES ('Category 1');

INSERT INTO category (category_name) 
VALUES ('Category 2');

INSERT INTO step (project_id, step_text, step_order) 
VALUES (1, 'Step 1 for Project 1', 1);

INSERT INTO step (project_id, step_text, step_order) 
VALUES (1, 'Step 2 for Project 1', 2);

INSERT INTO material (project_id, material_name, num_required, cost) 
VALUES (1, 'Material 1 for Project 1', 5, 10.5);

INSERT INTO material (project_id, material_name, num_required, cost) 
VALUES (1, 'Material 2 for Project 1', 3, 7.25);

INSERT INTO project_category (project_id, category_id) 
VALUES (1, 1);

INSERT INTO project_category (project_id, category_id) 
VALUES (1, 2);

