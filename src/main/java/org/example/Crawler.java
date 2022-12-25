package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;

public class Crawler {
    private HashSet<String> urlLink;
    private int MAX_DEPTH = 2;
    public Connection connection;

    public Crawler() {

        //used to write queries from to SQL Database
        connection = databaseConnection.getConnection();
        urlLink = new HashSet<>();
    }

    public void getPageTextAndLinks(String url, int depth){
        if(!urlLink.contains(url)){
            if(urlLink.add(url)){
                System.out.println(url);
            }
            try{
                //Parsing HTML object to java Document object
                Document document = Jsoup.connect(url).timeout(5000).get();
                //get text from dcument object
                String text = document.text().length()<500?document.text():document.text().substring(0 , 499);

                System.out.println(text);
                //insert data into page table
                PreparedStatement preparedStatement = connection.prepareStatement("Insert into pages values(?,?,?)");
                //insert into the page table available in sql
                //1.Document title
                preparedStatement.setString( 1 , document.title());
                //2.url link
                preparedStatement.setString(2 , url);
                //3.Text present in the page
                preparedStatement.setString(3 , text);
                preparedStatement.executeUpdate();

                //increase depth
                depth++;
                //if depth is greater than max the return
                if(depth > MAX_DEPTH){
                    return;
                }

                Elements availableLinksOnPage = document.select("a[href]");

                for(Element currentLink :availableLinksOnPage){
                    getPageTextAndLinks(currentLink.attr("abs:href"),depth);
                }

            } catch (IOException ioException){
                ioException.printStackTrace();
            }
            catch (SQLException sqlException){
                sqlException.printStackTrace();
            }
        }
    }



    public static void main(String[] args) {

        Crawler crawler = new Crawler();
        crawler.getPageTextAndLinks("https://www.javatpoint.com/" , 0);
     }

 }

