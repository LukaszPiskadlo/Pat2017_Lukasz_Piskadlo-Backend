# Patronage 2017 - Backend

Simple REST API for movie database using Spring and Spring Boot.

## Requirements

* Java JDK 8 - http://www.oracle.com/technetwork/java/javase/downloads/index.html
* Maven 3.x - https://maven.apache.org/download.cgi

## Build and run

* Build
```
mvnw clean install
```

* Run
```
mvnw spring-boot:run
```

## API

### Get all actors

- Request:
`GET http://localhost:8080/actors`

- Response:

```
[
  {
    "id",
    "name",
    "lastName",
    "birthDate"
  }
]
```

### Get actor by id

- Request: 
`GET http://localhost:8080/actors/{id}`

- Response:
```
{
  "id",
  "name",
  "lastName",
  "birthDate"
}
```

### Add actor

- Request: 
`POST http://localhost:8080/actors`
  - Header: 
  `Content-Type:application/json`
  - Body:
  ```
  {
    "name",
    "lastName",
    "birthDate"
  }
  ```
- Response:
```
{
  "id",
  "name",
  "lastName",
  "birthDate"
}
```

### Delete actor

- Request: 
`DELETE http://localhost:8080/actors/{id}`

- Response:
```
{
  "id",
  "name",
  "lastName",
  "birthDate"
}
```

### Update actor

- Request:
`PUT http://localhost:8080/actors/{id}`
  - Header: 
  `Content-Type:application/json`
  - Body:
  ```
  {
    "name",
    "lastName",
    "birthDate"
  }
  ```
- Response:
```
{
  "id",
  "name",
  "lastName",
  "birthDate"
}
```

### Get all movies

- Request:
`GET http://localhost:8080/movies`

- Response:
```
[
  {
    "id",
    "title",
    "director",
    "cast": [
      {
        "id",
        "name",
        "lastName",
        "birthDate"
      }
    ],
    "releaseDate",
    "duration",
    "amountAvailable",
    "group"
  }
]
```
  
### Get movie by id
  
- Request:
`GET http://localhost:8080/movies/{id}`
  
- Response:
```
{
  "id",
  "title",
  "director",
  "cast": [
    {
      "id",
      "name",
      "lastName",
      "birthDate"
    }
  ],
  "releaseDate",
  "duration",
  "amountAvailable",
  "group"
}
```

### Add movie

- Request:
`POST http://localhost:8080/movies`
  - Header:
  `Content-Type:application/json`
  - Body:
  ```
  {
    "title",
    "director",
    "cast": [
      {
        "name",
        "lastName",
        "birthDate"
      }
    ],
    "releaseDate"
    "duration",
    "amountAvailable",
    "group"
  }
  ```
  
- Response:
```
{
  "id",
  "title",
  "director",
  "cast": [
    {
      "id",
      "name",
      "lastName",
      "birthDate"
    }
  ],
  "releaseDate",
  "duration",
  "amountAvailable",
  "group"
}
```

### Delete movie

- Request:
`DELETE http://localhost:8080/movies/{id}`

- Response:
```
{
  "id",
  "title",
  "director",
  "cast": [
    {
      "id",
      "name",
      "lastName",
      "birthDate"
    }
  ],
  "releaseDate",
  "duration",
  "amountAvailable",
  "group"
}
```

### Update movie

- Request:
`PUT http://localhost:8080/movies`
  - Header:
  `Content-Type:application/json`
  - Body:
  ```
  {
    "title",
    "director",
    "cast": [
      {
        "name",
        "lastName",
        "birthDate"
      }
    ],
    "releaseDate",
    "duration",
    "amountAvailable",
    "group"
  }
  ```
  
- Response:
```
{
  "id",
  "title",
  "director",
  "cast": [
    {
      "id",
      "name",
      "lastName",
      "birthDate"
    }
  ],
  "releaseDate",
  "duration",
  "amountAvailable",
  "group"
}
```

### Add actor to movie

- Request:
`POST http://localhost:8080/movies/{id}/actor`
  - Header:
  `Content-Type:application/json`
  - Body:
  ```
  {
    "name",
    "lastName",
    "birthDate"
  }
  ```
  
- Response:
```
{
  "id",
  "title",
  "director",
  "cast": [
    {
      "id",
      "name",
      "lastName",
      "birthDate"
    }
  ],
  "releaseDate",
  "duration",
  "amountAvailable",
  "group"
}
```

### Get available movies

- Request:
`GET http://localhost:8080/movies/available`

- Response:
```
[
  {
    "id",
    "title",
    "director",
    "cast": [
      {
        "id",
        "name",
        "lastName",
        "birthDate"
      }
    ],
    "releaseDate",
    "duration",
    "amountAvailable",
    "group"
  }
]
```

### Get movies by group

- Request:
`GET http://localhost:8080/movies/group/{groupName}`
    - `{groupName} : NEW, HIT, OTHER`

- Response:
```
[
  {
    "id",
    "title",
    "director",
    "cast": [
      {
        "id",
        "name",
        "lastName",
        "birthDate"
      }
    ],
    "releaseDate",
    "duration",
    "amountAvailable",
    "group"
  }
]
```

### Create user

- Request:
`POST http://localhost:8080/users`
    - Header:
        `Content-Type:application/json`
    - Body:
     ```
     {
         "name",
         "lastName",
         "email",
         "password"
     }
     ```

- Response:
```
{
    "id",
    "name",
    "lastName",
    "email",
    "amountOfRentedMovies"
}
```

### Get movies rented by user

- Request:
`GET http://localhost:8080/users/{userId}`

- Response:
```
[
  {
    "id",
    "title",
    "director",
    "cast": [
      {
        "id",
        "name",
        "lastName",
        "birthDate"
      }
    ],
    "releaseDate",
    "duration",
    "amountAvailable",
    "group"
  }
]
```

### Rent movie

- Request:
`POST http://localhost:8080/users/{userId}/rent`
    - Header:
        `Content-Type:application/json`
    - Body: List of movie ids
    ```
    [
    	{movieId},
    	{movieId}
    ]
    ```

- Response:
```
{
  "price",
  "user": {
    "id",
    "name",
    "lastName",
    "amountOfRentedMovies"
  },
  "rentedMovies": [
    {
      "id",
      "title",
      "director",
      "cast": [
        {
          "id",
          "name",
          "lastName"
        }
      ],
      "releaseDate",
      "duration",
      "amountAvailable",
      "group"
    }
  ]
}
```   

### Return movie

- Request:
    `POST http://localhost:8080/users/{userId/return`
    - Header:
        `Content-Type:application/json`
    - Body: List of movie ids
        ```
        [
            {movieId},
            {movieId}
        ]
        ```

- Response:
```
[
  {
    "id",
    "title",
    "director",
    "cast": [
      {
        "id",
        "name",
        "lastName",
        "birthDate"
      }
    ],
    "releaseDate",
    "duration",
    "amountAvailable",
    "group"
  }
]
```
