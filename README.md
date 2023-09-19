Starting a local database: 
docker run --name postgres -e POSTGRES_USER=pguser -e  POSTGRES_PASSWORD=pgpwd -e POSTGRES_DB=postgres -d -p 5432:5432 postgres
