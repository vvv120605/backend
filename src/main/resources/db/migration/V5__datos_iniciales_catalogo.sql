-- Catálogo inicial de la tienda (los mismos productos que mostraba el frontend)
INSERT INTO categorias (nombre) VALUES
    ('Guitarras Acústicas'),
    ('Guitarras Eléctricas'),
    ('Baterías'),
    ('Teclados y Pianos'),
    ('Amplificadores');

INSERT INTO productos (codigo, nombre, descripcion, precio, stock, stock_critico, imagen, categoria_id) VALUES
    ('GT001', 'Guitarra Acústica Yamaha',
     'Guitarra acústica ideal para principiantes y estudiantes, con un sonido cálido y cuerpo cómodo para largas sesiones de práctica.',
     89990, 12, 3, '/img/gt001.jpg', (SELECT id FROM categorias WHERE nombre = 'Guitarras Acústicas')),
    ('GT002', 'Guitarra Eléctrica Fender',
     'Guitarra eléctrica de cuerpo sólido, perfecta para rock y blues, con pastillas de alta fidelidad.',
     249990, 5, 2, '/img/gt002.jpg', (SELECT id FROM categorias WHERE nombre = 'Guitarras Eléctricas')),
    ('BT001', 'Batería Electrónica Roland',
     'Batería electrónica compacta con múltiples sonidos preconfigurados, ideal para práctica silenciosa.',
     399990, 3, 1, '/img/bt001.jpg', (SELECT id FROM categorias WHERE nombre = 'Baterías')),
    ('TC001', 'Teclado Casio 61 teclas',
     'Teclado de 61 teclas con múltiples voces y ritmos, perfecto para aprender y componer.',
     129990, 8, 2, '/img/tc001.jpg', (SELECT id FROM categorias WHERE nombre = 'Teclados y Pianos')),
    ('AM001', 'Amplificador Marshall 20W',
     'Amplificador compacto de 20W, ideal para práctica en casa con un sonido característico Marshall.',
     79990, 10, 2, '/img/am001.jpg', (SELECT id FROM categorias WHERE nombre = 'Amplificadores'));