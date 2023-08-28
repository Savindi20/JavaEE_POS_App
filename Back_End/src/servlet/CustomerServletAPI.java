package servlet;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = "/Customer")
public class CustomerServletAPI extends HttpServlet {

//    query string
//    form Data (x-www-form-urlencoded)
//    JSON
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posdb", "root", "1234");
            String option = req.getParameter("option");

            switch (option) {
                case "GetAll":
                    PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Customer");
                    ResultSet rst = pstm.executeQuery();
                    JsonArrayBuilder allCustomers = Json.createArrayBuilder();

                    while (rst.next()) {
                        JsonObjectBuilder customer = Json.createObjectBuilder();
                        customer.add("id", rst.getString("id"));
                        customer.add("name", rst.getString("name"));
                        customer.add("address", rst.getString("address"));
                        customer.add("salary", rst.getDouble("salary"));
                        allCustomers.add(customer.build());
                    }
                    resp.addHeader("Content-Type", "application/json");
                    resp.addHeader("Access-Control-Allow-Origin", "*");

                    JsonObjectBuilder job = Json.createObjectBuilder();
                    job.add("state", "OK");
                    job.add("message", "Successfully Loaded..!");
                    job.add("data", allCustomers.build());
                    resp.getWriter().print(job.build());

                    break;
                case "search":
                    PreparedStatement pstm3 = connection.prepareStatement("select * from Customer where id=?");
                    pstm3.setObject(1, req.getParameter("id"));
                    ResultSet rst3 = pstm3.executeQuery();
                    resp.addHeader("Access-Control-Allow-Origin", "*");

                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    if (rst3.next()) {
                        String id = rst3.getString(1);
                        String name = rst3.getString(2);
                        String salary = rst3.getString(3);
                        String address = rst3.getString(4);

                        objectBuilder.add("id", id);
                        objectBuilder.add("name", name);
                        objectBuilder.add("salary", salary);
                        objectBuilder.add("address", address);

                    }
                    resp.setContentType("application/json");
                    resp.getWriter().print(objectBuilder.build());
                    break;

            }

        }catch (ClassNotFoundException | SQLException e){
            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state","Error");
            rjo.add("message",e.getLocalizedMessage());
            rjo.add("data","");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(rjo.build());
        }

    }

//    query string
//    JSON
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String salary = req.getParameter("salary");
        resp.addHeader("Access-Control-Allow-Origin","*");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posdb", "root", "1234");

            PreparedStatement pstm = connection.prepareStatement("insert into Customer values(?,?,?,?)");
            pstm.setObject(1,id);
            pstm.setObject(2,name);
            pstm.setObject(3,address);
            pstm.setObject(4,salary);
            boolean b = pstm.executeUpdate() > 0;
            if (b){
                JsonObjectBuilder responseObject = Json.createObjectBuilder();
                responseObject.add("state","Ok");
                responseObject.add("message","Successfully added..!");
                responseObject.add("data","");
                resp.getWriter().print(responseObject.build());
            }
        } catch (ClassNotFoundException e) {
            JsonObjectBuilder error = Json.createObjectBuilder();
            error.add("state","Ok");
            error.add("message",e.getLocalizedMessage());
            error.add("data","");
//            resp.setStatus(500);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(error.build());
        }catch (SQLException e) {
            JsonObjectBuilder error = Json.createObjectBuilder();
            error.add("state","Error");
            error.add("message",e.getLocalizedMessage());
            error.add("data","");
//            resp.setStatus(400);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(error.build());
        }
    }

//    query string
//    JSON
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        resp.setContentType("application/json");
        resp.addHeader("Access-Control-Allow-Origin","*");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posdb", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("delete from Customer where id=?");
            pstm.setObject(1,id);
            boolean b = pstm.executeUpdate() > 0;
            if (b) {
                JsonObjectBuilder rjo = Json.createObjectBuilder();
                rjo.add("state","Ok");
                rjo.add("message","Successfully Deleted..!");
                rjo.add("data","");
                resp.getWriter().print(rjo.build());
            }else {
                throw new RuntimeException("There is no Customer for that ID..!");
            }
        } catch (RuntimeException e) {
            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state","Error");
            rjo.add("message",e.getLocalizedMessage());
            rjo.add("data","");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(rjo.build());
        }catch (ClassNotFoundException | SQLException e){
            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state","Error");
            rjo.add("message",e.getLocalizedMessage());
            rjo.add("data","");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(rjo.build());
        }
    }

//    query string
//    JSON
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject customer = reader.readObject();
        String id = customer.getString("id");
        String name = customer.getString("name");
        String address = customer.getString("address");
        String salary = customer.getString("salary");
        resp.addHeader("Access-Control-Allow-Origin","*");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posdb", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("update Customer set name=?,address=?,salary=? where id=?");
            pstm.setObject(4,id);
            pstm.setObject(1,name);
            pstm.setObject(2,address);
            pstm.setObject(3,salary);
            boolean b = pstm.executeUpdate() > 0;
            if (b){
                JsonObjectBuilder responseObject = Json.createObjectBuilder();
                responseObject.add("state","Ok");
                responseObject.add("message","Successfully Updated..!");
                responseObject.add("data","");
                resp.getWriter().print(responseObject.build());
            }else{
                throw new RuntimeException("Wrong ID, Please check the ID..!");
            }

        } catch (RuntimeException e) {
            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state","Error");
            rjo.add("message",e.getLocalizedMessage());
            rjo.add("data","");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(rjo.build());
        }catch (ClassNotFoundException | SQLException e){
            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state","Error");
            rjo.add("message",e.getLocalizedMessage());
            rjo.add("data","");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(rjo.build());
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin","*");
        resp.addHeader("Access-Control-Allow-Methods","DELETE,PUT");
        resp.addHeader("Access-Control-Allow-Headers","content-type");
    }
}
