-- Create customer table
CREATE TABLE customer (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL
);

-- Create order table
CREATE TABLE "order" (
                         id BIGSERIAL PRIMARY KEY,
                         description VARCHAR(255) NOT NULL,
                         customer_id BIGINT NOT NULL,
                         CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer (id),
						 CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE product (
                          id BIGSERIAL PRIMARY KEY,
                          description VARCHAR(255) NOT NULL,
						  CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES order (id)
);
