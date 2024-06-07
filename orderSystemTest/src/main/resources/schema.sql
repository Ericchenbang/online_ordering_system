CREATE TABLE IF NOT EXISTS ORDERS(
    id SERIAL,
    order_time TIMESTAMP NOT NULL,
    customer_name VARCHAR(50) NOT NULL,
    customer_number VARCHAR(50) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,

    PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS MENU (
    id SERIAL,
    item_name VARCHAR(50) NOT NULL,

    price DECIMAL(10, 2) NOT NULL,
    category VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS ORDER_ITEMS (

    order_id INT NOT NULL,
    menu_item_id INT NOT NULL,
    quantity INT NOT NULL,

    FOREIGN KEY (order_id) REFERENCES ORDERS(id) ON DELETE CASCADE,
    FOREIGN KEY (menu_item_id) REFERENCES MENU(id)
);
