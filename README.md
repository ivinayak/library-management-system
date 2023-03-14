# library-management-system
Library management system with Java springboot as backend and ReactJS as frontend
It supports 2 types of users : Staff and guest user.
Staff user can view/add/delete/update books and add/delete guest users.
Guest user can view/issue/renew/return books and see his borrowed books.

There are 4 microservices for the backend. 
1. Eureka server to enable client registry.
2. Authorization server that handles all user and authentication related services.
3. Library management server that handles all book related services.
4. Gateway service that acts as a firewall to redirect calls to the correct microservice

