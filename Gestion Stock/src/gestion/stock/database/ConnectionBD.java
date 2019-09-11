package gestion.stock.database;

import gestion.stock.entity.Charge;
import gestion.stock.entity.Client;
import gestion.stock.entity.Order;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;


public class ConnectionBD {        
    private static final String db = "gestion_stock";
    private static final String user = "root";
    private static final String pass= "";
    private static Connection con = null;
    private static Statement stmt;
    
    public static Connection getConnection(){
        if (con != null) return con;
        return getConnection(db, user, pass);
    }

    private static Connection getConnection(String db_name,String username,String password){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/"+db,user,pass);
        }catch(ClassNotFoundException | SQLException e){
            System.out.println("error: " + e.getMessage());
        }
        
        return con;        
    }
    
    //********************* Part of Login *****************************************************
    public static Boolean checkUser(String email) throws SQLException{
        stmt = ConnectionBD.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("select * from users where email = '" + email + "'");
        
        int count = 0;
        while (rs.next()) {
            ++count;
        }
        return count != 0;
    }
    
    public static Boolean checkPassword(String email, String password) throws SQLException{
        stmt = ConnectionBD.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("select * from users where email = '" + email + "' AND password = '" + password + "'");
        
        int count = 0;
        while (rs.next()) {
            ++count;
        }

        return count != 0;
    }
    
    public static String getUserType(String email) throws SQLException{
        stmt = ConnectionBD.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("select type from users where email = '" + email + "'");
        String type = "";
        
        while (rs.next()) {
            type = rs.getString("type");
            break;
        }

        return type;
    }
    
    //********************* Part of Client *****************************************************
    public static ArrayList<Client> getClients() throws SQLException{
        stmt = ConnectionBD.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("select * from clients order by id DESC");
        ArrayList<Client> user_ = new ArrayList<>();
        
        while (rs.next()){
            user_.add(new Client(rs.getInt("id"),rs.getString("fullname"),rs.getString("email"),rs.getString("telephone"),rs.getString("address"),rs.getString("profession"),rs.getString("ice")));
        }
        
        return user_;
    }
    
    public static void saveClient(Client client) throws SQLException {
        stmt = ConnectionBD.getConnection().createStatement();
        stmt.execute("insert into clients (email,fullname,telephone,profession,address,ice,inscription_date) value ("
                + "'" + client.getEmail() + "',"
                + "'" + client.getFullname() + "',"
                + "'" + client.getTelephone() + "',"
                + "'" + client.getProfession() + "',"
                + "'" + client.getAddress() + "',"
                + "'" + client.getIce()
                + "',now())");
    }

    public static void deleteClient(long id) throws SQLException {
        stmt = ConnectionBD.getConnection().createStatement();
        stmt.execute("delete from clients where id = " + id);
    }

    public static void updateClient(Client editedClient) throws SQLException {
        stmt = ConnectionBD.getConnection().createStatement();
        stmt.execute("update clients set email = '"+ editedClient.getEmail() +"',"
                        + "fullname = '"+ editedClient.getFullname() +"',"
                        + "telephone = '"+ editedClient.getTelephone() +"',"
                        + "address = '"+ editedClient.getAddress() +"',"
                        + "ice = '"+ editedClient.getIce() +"' where id = " + editedClient.getId());
    }
    
    //********************* Part of Order *****************************************************
    public static ArrayList<Order> getOrders(long id) throws SQLException{
        stmt = ConnectionBD.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("select * from orders where client_id = "+ id +" order by id DESC");
        ArrayList<Order> order = new ArrayList<>();
        
        while (rs.next()){
            order.add(new Order(rs.getInt("id"),rs.getString("title"),rs.getString("description"),rs.getDate("created_date")));
        }
        
        return order;
    }

    public static void saveOrder(Order order, long id) throws SQLException {
        stmt = ConnectionBD.getConnection().createStatement();
        stmt.execute("insert into orders (client_id,title,description,created_date) value ("
                + id + ","
                + "'" + order.getTitle() + "',"
                + "'" + order.getDescription()+ "',now())");
    }

    public static void updateOrder(Order editedOrder) throws SQLException {
        stmt = ConnectionBD.getConnection().createStatement();
        stmt.execute("update orders set title = '"+ editedOrder.getTitle()+"',"
                        + "description = '"+ editedOrder.getDescription()  +"' where id = " + editedOrder.getId());
    }

    public static void deleteOrder(Order order) throws SQLException {
        stmt = ConnectionBD.getConnection().createStatement();
        stmt.execute("delete from orders where id = " + order.getId());  
    }
    
    public static ArrayList<Charge> getCharges() throws SQLException{
        stmt = ConnectionBD.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("select * from charges order by id DESC");
        ArrayList<Charge> charge = new ArrayList<>();
        
        while (rs.next()){
            charge.add(new Charge(rs.getInt("id"),rs.getString("name"),rs.getDouble("price"),rs.getDate("created_date").toString(),rs.getString("description")));
        }
        
        return charge;
    }

    public static void deleteCharge(Charge selectedCharge) throws SQLException {
        stmt = ConnectionBD.getConnection().createStatement();
        stmt.execute("delete from charges where id = " + selectedCharge.getId());
    }

    public static void updateCharge(Charge editedCharge) throws SQLException {
        stmt = ConnectionBD.getConnection().createStatement();
        stmt.execute("update charges set name = '"+ editedCharge.getName()+"',"
                        + "price = '"+ editedCharge.getPrice() +"',"
                        + "description = '"+ editedCharge.getDescription()+"' where id = " + editedCharge.getId());
    }

    public static void saveCharge(Charge charge) throws SQLException {
        stmt = ConnectionBD.getConnection().createStatement();
        stmt.execute("insert into charges (name,price,description,created_date) value ("
                + "'" + charge.getName()+ "',"
                + "'" + charge.getPrice() + "',"
                + "'" + charge.getDescription() + "',"
                + "now())");
    }

    public static ArrayList<Charge> getCharges(LocalDate localDate1, LocalDate localDate2) throws SQLException {
        stmt = ConnectionBD.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("select * from charges where Date(created_date) between '"+ localDate1 +"' AND '"+ localDate2 +"'");
        ArrayList<Charge> charge = new ArrayList<>();
        
        while (rs.next()){
            charge.add(new Charge(rs.getInt("id"),rs.getString("name"),rs.getDouble("price"),rs.getDate("created_date").toString(),rs.getString("description")));
        }
        
        return charge;
    
    }
    
} 