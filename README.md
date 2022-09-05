# Asset-Trading-Platform

A client-server based asset trading system for use with an organisation. Allows units within an organisation to trade assets they produce/need on a platform similar to a stock exchange. The client is a simple interface that allows users within an organisation to buy and sell assets the unit they work within own. Admin users can also create new units, assets and users. Uses threads to allow multiple clients to connect simultaneously. Also performs the matching of trades and handles the connection to the database.

![asset-trading](https://user-images.githubusercontent.com/47819009/128789851-2fc53571-48a9-472a-8e0e-f744fe92c798.PNG)


### Database Info

- Install MariaDB from [this location](https://mariadb.org/download/).
- During install, take note of the port MariaDB listens to. Also make sure you remember the password you use for the root user.
- Hopefully the dependencies should already be present for Java to talk to MariaDB. If not, go to `File -> Project Structure` click `Libraries` click the plus icon and search for `org.mariadb.jdbc:mariadb` and choose the most recent stable build.
- Add the required details to the dbserver.props file (port, username and password)

### Running the Server and Client

1. Ensure a MariaDB server is running and you have the correct port and login details recorded in the dbserver.props file.
2. Run the ```main``` method in the ```RunServer``` class. This should auto-create the schema and tables in the database, as well as a default unit (IT Admin) and default user (username: ```admin``` password: ```password```).
3. Run the ```main``` method in ```AssetTradingGUI```to use the client side of the project.  You can allow ```AssetTradingGUI``` to run multiple instances to simulate multiple clients being connected to the server at once.
