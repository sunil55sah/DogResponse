# DogResponse - Full Stack Java Web Project

DogResponse is a simple, mini full-stack Java web application that allows users to **register**, **log in**, **save dog names**, **search**, and **delete** dog names from a personal list.

> **Note:** Edit functionality is **not working yet**. Planned for a future update.

---

##  Tech Stack

###  Frontend:
- HTML, CSS, Bootstrap
- JSP (Java Server Pages)

###  Backend:
- Java Servlets (`jakarta.servlet`)
- JDBC (Java Database Connectivity)

###  Database:
- MySQL

---

##  Project Structure (Maven)
DogResponse/
├── src/main/java/com/dogresponse/servlet/ # Servlet classes
├── src/main/webapp/ # JSP pages and static assets
│ ├── css/
│ ├── *.jsp
│ └── WEB-INF/web.xml
├── pom.xml # Maven dependencies
└── .gitignore

##  Features

-  User Signup & Login
-  Save favorite dog names
-  Search dog names
-  Delete names from the list
-  **Edit feature coming soon!**

HTTP Dog Image Source
This project uses images from the https://http.dog API, which provides a fun and visual way to represent HTTP status codes using dog illustrations.

##  Screenshots

###  Signup Page
![Signup dashboard](https://raw.githubusercontent.com/sunil55sah/DogResponse/main/images/signup%20page.png)

###  Login Page
![Login dashboard](https://raw.githubusercontent.com/sunil55sah/DogResponse/main/images/loginpage.png)

###  Search & Save
![search doghttpCode](https://raw.githubusercontent.com/sunil55sah/DogResponse/main/images/search%20doghttpCode%20and%20save%20.png)

###  Delete &  Edit (Edit not working yet)
![Delete and edit](https://raw.githubusercontent.com/sunil55sah/DogResponse/main/images/we%20can%20delelete%20and%20edit%20.png)
