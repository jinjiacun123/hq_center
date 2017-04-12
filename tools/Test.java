import java.lang.*; 
import java.util.*; 
import java.sql.*; 


public class Test
{
    public static void main( String args[] )
    {
Test t = new Test();
t.test();
    }


    void test()
    {
        try
        {   
            Class.forName("com.mysql.jdbc.Driver") ;   
        }catch(ClassNotFoundException e)
        {   
            System.out.println("not find mysql driver, load failed");   
            e.printStackTrace() ;   
        }


        //String url = "jdbc:Hive://localhost:10000/default"; 
        String url = "jdbc:mysql://192.168.1.233:3306/mysql";
        String username = "root"; 
        String password = "root";


        try
        {   
            Connection con = DriverManager.getConnection(url , username , password ) ;
            System.out.println( "connect success" );
        }catch(SQLException se)
        {   
            System.out.println("connect failed");   
            se.printStackTrace() ;   
        }
    }
}
