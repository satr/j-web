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

CREATE TABLE Product
(
    ID INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    Name VARCHAR(500),
    Price FLOAT
);

CREATE TABLE Stock
(
    ProductID INT(11) NOT NULL,
    Amount INT(11) NOT NULL,
    CONSTRAINT FK_Stock_Product FOREIGN KEY (ProductID) REFERENCES Product (ID)
);
CREATE INDEX FK_Stock_Product ON Stock (ProductID);

CREATE TRIGGER Product_Stock_Insert
    AFTER INSERT ON Product
    FOR EACH ROW
BEGIN
    INSERT INTO Stock (ProductID, Amount)
    VALUES (NEW.ID, 0);
END;

CREATE TABLE CartItemStatus (
  ID INT NOT NULL PRIMARY KEY,
  Name NVARCHAR(255) NOT NULL,
  ItemKey VARCHAR(50) NOT NULL
);

INSERT INTO dev1.CartItemStatus (ID, Name, ItemKey) VALUES (1, 'Not Payed', 'NotPayed');
INSERT INTO dev1.CartItemStatus (ID, Name, ItemKey) VALUES (2, 'Payed', 'Payed');
INSERT INTO dev1.CartItemStatus (ID, Name, ItemKey) VALUES (3, 'Postponed', 'Postponed');
INSERT INTO dev1.CartItemStatus (ID, Name, ItemKey) VALUES (4, 'Removed', 'Removed');

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


INSERT INTO CartItemStatus (Name) VALUES ('Not Payed');
INSERT INTO CartItemStatus (Name) VALUES ('Payed');
INSERT INTO CartItemStatus (Name) VALUES ('Postponed');
INSERT INTO CartItemStatus (Name) VALUES ('Removed');