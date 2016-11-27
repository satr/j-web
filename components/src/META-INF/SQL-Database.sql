CREATE TABLE Account
(
    ID INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    FirstName VARCHAR(255) NOT NULL,
    LastName VARCHAR(255),
    MiddleName VARCHAR(2000),
    PasswordHash VARCHAR(255) NOT NULL,
    PasswordSalt VARCHAR(255) NOT NULL,
    Email VARCHAR(255),
    CreatedOn DATETIME NOT NULL,
    UpdatedOn DATETIME
);

CREATE TABLE Stock
(
    Amount INT(11) NOT NULL,
    ID INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT
);

CREATE TABLE Product
(
    ID INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    Name VARCHAR(500),
    Price FLOAT,
    StockID INT(11) NOT NULL,
    CONSTRAINT product_ibfk_1 FOREIGN KEY (StockID) REFERENCES Stock (ID)
);
CREATE INDEX FK_Product_Stock ON Product (StockID);


CREATE TABLE CartItemStatus (
  ID INT NOT NULL PRIMARY KEY,
  Name NVARCHAR(255) NOT NULL,
  ItemKey VARCHAR(50) NOT NULL
);

INSERT INTO CartItemStatus (ID, Name, ItemKey) VALUES (1, 'Not Payed', 'NotPayed');
INSERT INTO CartItemStatus (ID, Name, ItemKey) VALUES (2, 'Payed', 'Payed');
INSERT INTO CartItemStatus (ID, Name, ItemKey) VALUES (3, 'Postponed', 'Postponed');
INSERT INTO CartItemStatus (ID, Name, ItemKey) VALUES (4, 'Removed', 'Removed');

CREATE TABLE CartItem (
  ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  ProductID INT NOT NULL,
  AccountID INT NOT NULL,
  Price FLOAT NOT NULL,
  Amount INT NOT NULL,
  Discount FLOAT NOT NULL,
  Total FLOAT NOT NULL,
  Comment NVARCHAR(2000) NULL,
  StatusID INT NOT NULL,
  CreatedOn DATETIME NOT NULL,
  UpdatedOn DATETIME NOT NULL,
  CONSTRAINT FK_CartItem_Product FOREIGN KEY (ProductID) REFERENCES Product (ID),
  CONSTRAINT FK_CartItem_Account FOREIGN KEY (AccountID) REFERENCES Account (ID),
  CONSTRAINT FK_CartItem_CartItemStatus FOREIGN KEY (StatusID) REFERENCES CartItemStatus (ID)
);
