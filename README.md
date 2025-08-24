# Introduction

Introducing the "Multiple-Choice Test Management System" - a cutting-edge project developed by talented students from Hanoi University of Science and Technology (HUST). This cross-platform solution combines a website and mobile app with the power of AI to provide efficient and accurate test scoring, revolutionizing the field of education and assessment. Available at: *"http://mpec.exam.io.vn"*.

# Teams
* DAO HUY CHIEN (chiendao1808)
* TA QUANG MINH (minhtq13)
* PHAM THI HUONG (huongclie)
# Technical Stack
## Backend
* Java
* Spring Framework
* PostgreSQL
* Redis
* Google FCM
## Frontend
* HTML/CSS/Typescript
* NextJS
* Antd/Bootstrap/TailwindCSS
## AI
* Python
* OpenCV
* YOLOv8
## Build and Deployment
* Docker
* AWS EC2 / Google GCE
* NGINX
* Jenkins CI/CD


## Setup and Run in Local Enviroment:
* Backend Java Spring Boot:
	* JDK 11: https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html
	* Database initialization (using pgql or pgAdmin):
		> create database elearning_support_db
	* Database connection (Recommend using PostgreSQL 14 or later):
		* Config file: ./src/main/resources/config/
			> application-local.yml
		* Connection configuration (apply for both master and slave datasource): 
			> url: jdbc:postgresql://localhost:5432/elearning_support_db \
			username: $POSTGRES_USER \
			password: $POSTGRES_PASSWORD \
	* Database migration:
		> flyway:
		    &nbsp; &nbsp; &nbsp; &nbsp; enabled: true \
    		&nbsp; &nbsp; &nbsp; &nbsp; url: jdbc:postgresql://localhost:5432/elearning_support_db \
			&nbsp; &nbsp; &nbsp; &nbsp; username: $POSTGRES_USER \
			&nbsp; &nbsp; &nbsp; &nbsp; password: $POSTGRES_PASSWORD \
* Frontend ReactJS(Old UI version) / NextJS 14(New UI version):
	* Node 18.20.2: https://nodejs.org/en/blog/release/v18.20.2
	* Command:
		* Install dependencies in package.json:
			* Using npm:
				> npm install
			* Using yarn:
				> npm install --global yarn && yarn install
		* Run Web application:
			* Using npm: 
				> npm run start
			* Using yarn:
				> yarn start
* Module Automatic Scoring Python:
	* Python >= 3.8
	* Install dependencies: 
		> pip / pip3 install opencv-python ultralytics flask
	* Run server:
		> python / python3 server.py
