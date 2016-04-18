package com.epam.app;

import com.epam.app.dao.NewsRepository;
import com.epam.app.dao.impl.AuthorRepositoryImpl;
import com.epam.app.dao.impl.NewsRepositoryImpl;
import com.epam.app.exception.DaoException;
import com.epam.app.model.News;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dmitry on 4/17/16.
 */
public class Ololo {
    public static void main(String[] args) throws Exception {
        String s            = new String();
        List<String> inst = new LinkedList<>();

        try
        {
            FileReader fr = new FileReader(new File("src/main/resources/script/sql/data.sql"));
            // be sure to not have line starting with "--" or "/*" or any other non aplhabetical character

            BufferedReader br = new BufferedReader(fr);

            while((s = br.readLine()) != null)
            {
                inst.add(s);
            }
            br.close();

            // here is our splitter ! We use ";" as a

            Connection c = DriverManager.getConnection("jdbc:oracle:thin:@localhost", "dmitry", "pass");
            Statement st = c.createStatement();

            for(int i = 0; i<inst.size(); i++)
            {
                // we ensure that there is no spaces before or after the request string
                // in order to not execute empty statements
                if(!inst.get(i).trim().equals(""))
                {
                    if (i != 0)
                        continue;
                    System.out.println(inst.get(1));
                    System.out.println(inst.get(1).substring(0, inst.get(1).length() - 1));
                    st.executeUpdate(inst.get(1));
                    System.out.println(">>"+inst.get(1));
                    if (i == 1)
                        break;
                }
            }

        }
        catch(Exception e)
        {
            System.out.println("*** Error : "+e.toString());
            System.out.println("*** ");
            System.out.println("*** Error : ");
            e.printStackTrace();
            System.out.println("################################################");
            System.out.println(inst.get(1));
        }
    }
}
