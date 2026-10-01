-- =====================================================
-- SPEEDFAST - BASE DE DATOS
-- Semana 8
-- =====================================================

CREATE DATABASE IF NOT EXISTS speedfast_db;

-- =====================================================
-- USUARIO DE LA APLICACIÓN
-- =====================================================

CREATE USER IF NOT EXISTS 'speedfast_user'@'localhost'
IDENTIFIED BY 'SpeedFast2026';

GRANT ALL PRIVILEGES ON speedfast_db.*
TO 'speedfast_user'@'localhost';

FLUSH PRIVILEGES;

USE speedfast_db;

-- =====================================================
-- TABLA REPARTIDORES
-- =====================================================

CREATE TABLE IF NOT EXISTS repartidores (
                                            id INT AUTO_INCREMENT PRIMARY KEY,
                                            nombre VARCHAR(100) NOT NULL
    );

-- =====================================================
-- TABLA PEDIDOS
-- =====================================================

CREATE TABLE IF NOT EXISTS pedidos (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       direccion VARCHAR(100) NOT NULL,
    tipo ENUM('COMIDA', 'ENCOMIENDA', 'EXPRESS'),
    estado ENUM('PENDIENTE', 'EN_REPARTO', 'ENTREGADO')
    );

-- =====================================================
-- TABLA ENTREGAS
-- =====================================================

CREATE TABLE IF NOT EXISTS entregas (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        id_pedido INT,
                                        id_repartidor INT,
                                        fecha DATE,
                                        hora TIME,

                                        CONSTRAINT fk_entregas_pedido
                                        FOREIGN KEY (id_pedido)
    REFERENCES pedidos(id),

    CONSTRAINT fk_entregas_repartidor
    FOREIGN KEY (id_repartidor)
    REFERENCES repartidores(id)
    );

-- =====================================================
-- CONSULTAS DE COMPROBACIÓN
-- =====================================================

SELECT * FROM repartidores;
SELECT * FROM pedidos;
SELECT * FROM entregas;