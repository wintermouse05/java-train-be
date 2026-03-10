FROM mysql:8

# Environment variables used by MySQL entrypoint for first-time initialization.
ENV MYSQL_ROOT_PASSWORD=Student@123.
ENV MYSQL_DATABASE=student_db
ENV MYSQL_USER=student
ENV MYSQL_PASSWORD=Student@123
ENV TZ=Asia/Ho_Chi_Minh

# Initialization scripts are executed in filename order on first startup.
COPY init/ /docker-entrypoint-initdb.d/

EXPOSE 3306
