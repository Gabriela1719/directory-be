directory-be

docker run --name postgres -e POSTGRES_USER=pguser -e  POSTGRES_PASSWORD=pgpwd -e POSTGRES_DB=postgres -d -p 5432:5432 postgres

To make Docker persistent data storage:

1. Create a Docker volume: docker volume create <volume-name>
2. Modify your Docker run command: docker run -v <volume-name>:/var/lib/postgresql/data -p 5432:5432 -e POSTGRES_PASSWORD=<password> -d postgres
