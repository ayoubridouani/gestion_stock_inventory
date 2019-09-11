package gestion.stock.model;

import gestion.stock.database.ConnectionBD;
import java.sql.SQLException;

public class UserModel {
    
    public Boolean checkUser(String email) throws SQLException{
        return ConnectionBD.checkUser(email);
    }
    
    public Boolean checkPassword(String email, String password) throws SQLException{
        
        return ConnectionBD.checkPassword(email, password);
    }
    
    public String getUserType(String email) throws SQLException{
        
        return ConnectionBD.getUserType(email);
    }
    
}
