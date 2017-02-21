package com.ac.lab5;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * @author AC010168
 *
 */
@WebServlet(name = "webhook", urlPatterns = "/webhook")
public class Lab5Rest extends HttpServlet {

  /** Adding because it like it */
  private static final long serialVersionUID = -4463403013455550469L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setHeader("Access-Control-Allow-Origin", "*");
    resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
    resp.setHeader("Access-Control-Max-Age", "3600");
    resp.setHeader("Access-Control-Allow-Headers", "x-requested-with, X-Auth-Token, Content-Type");
    resp.setContentType("application/json");

    System.out.print("I've got a request, let's fill it: ");
    JSONObject object = new JSONObject();
    
    object.put("speech", "This video features the following elements: people, adult, man, portrait, one, indoors, urban, boy, wear, business");
    object.put("displayText", "This video features the following elements: people, adult, man, portrait, one, indoors, urban, boy, wear, business");
    object.put("source", "Lab5 Rest Service");
    
    resp.getWriter().write(object.toString());
  }
  
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setHeader("Access-Control-Allow-Origin", "*");
    resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
    resp.setHeader("Access-Control-Max-Age", "3600");
    resp.setHeader("Access-Control-Allow-Headers", "x-requested-with, X-Auth-Token, Content-Type");
    resp.setContentType("application/json");

    System.out.print("I've got a request, let's fill it: ");
    JSONObject object = new JSONObject();
    
    object.put("speech", "This video features the following elements: people, adult, man, portrait, one, indoors, urban, boy, wear, business");
    object.put("displayText", "This video features the following elements: people, adult, man, portrait, one, indoors, urban, boy, wear, business");
    object.put("source", "Lab5 Rest Service");
    
    resp.getWriter().write(object.toString());
  }

}
