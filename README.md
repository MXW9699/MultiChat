# MultiChat
Distributed Message Chat

# Set up Containerized database 
run docker compose up
-exec it to docker container
-mongosh
-rs.initiate()  to initiate the replica set
-db.createUser({user:"admin",pwd:"admin",roles:[{role:"root",db:"admin"}]}) create the root admin user

# Run the GetUserService
- operates on port 8080

