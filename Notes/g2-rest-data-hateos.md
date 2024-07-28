- spring data rest follows hateos conventions and returns the response with a lot of links and information

```sh
    curl  http://localhost:8080/api/v1/beer?beerStyle=ALE&size=2
```

```json
{
  "_embedded": {
    "beers": [
      {
        "beerName": "Mango Bobs",
        "beerStyle": "ALE",
        "upc": "0631234200036",
        "quantityOnHand": 1192,
        "price": 33.98,
        "createdDate": "2024-07-28T04:44:27.909+00:00",
        "lastModifiedDate": "2024-07-28T04:44:27.909+00:00",
        "_links": {
          "self": {
            "href": "http://localhost:8080/api/v1/beer/2f178112-469c-4c02-8c77-b08b1a6f4fbc"
          },
          "beer": {
            "href": "http://localhost:8080/api/v1/beer/2f178112-469c-4c02-8c77-b08b1a6f4fbc"
          }
        }
      },
      {
        "beerName": "Galaxy Cat",
        "beerStyle": "PALE_ALE",
        "upc": "9122089364369",
        "quantityOnHand": 2601,
        "price": 39.32,
        "createdDate": "2024-07-28T04:44:27.921+00:00",
        "lastModifiedDate": "2024-07-28T04:44:27.921+00:00",
        "_links": {
          "self": {
            "href": "http://localhost:8080/api/v1/beer/d14ad641-0bfd-4a81-a894-eb30c6e4b182"
          },
          "beer": {
            "href": "http://localhost:8080/api/v1/beer/d14ad641-0bfd-4a81-a894-eb30c6e4b182"
          }
        }
      }
    ]
  },
  "_links": {
    "first": {
      "href": "http://localhost:8080/api/v1/beer?beerStyle=ALE&page=0&size=2"
    },
    "self": {
      "href": "http://localhost:8080/api/v1/beer?beerStyle=ALE&size=2"
    },
    "next": {
      "href": "http://localhost:8080/api/v1/beer?beerStyle=ALE&page=1&size=2"
    },
    "last": {
      "href": "http://localhost:8080/api/v1/beer?beerStyle=ALE&page=14&size=2"
    },
    "profile": {
      "href": "http://localhost:8080/api/v1/profile/beer"
    },
    "search": {
      "href": "http://localhost:8080/api/v1/beer/search"
    }
  },
  "page": {
    "size": 2,
    "totalElements": 30,
    "totalPages": 15,
    "number": 0
  }
}
```

```sh
curl http://localhost:8080/api/v1/profile/beer
```

```json
{
  "alps": {
    "version": "1.0",
    "descriptor": [
      {
        "id": "beer-representation",
        "href": "http://localhost:8080/api/v1/profile/beer",
        "descriptor": [
          {
            "name": "version",
            "type": "SEMANTIC"
          },
          {
            "name": "beerName",
            "type": "SEMANTIC"
          },
          {
            "name": "beerStyle",
            "type": "SEMANTIC",
            "doc": {
              "format": "TEXT",
              "value": "LAGER, PILSNER, STOUT, GOSE, PORTER, ALE, WHEAT, IPA, PALE_ALE, SAISON"
            }
          },
          {
            "name": "upc",
            "type": "SEMANTIC"
          },
          {
            "name": "quantityOnHand",
            "type": "SEMANTIC"
          },
          {
            "name": "price",
            "type": "SEMANTIC"
          },
          {
            "name": "createdDate",
            "type": "SEMANTIC"
          },
          {
            "name": "lastModifiedDate",
            "type": "SEMANTIC"
          }
        ]
      },
      {
        "id": "create-beers",
        "name": "beers",
        "type": "UNSAFE",
        "descriptor": [],
        "rt": "#beer-representation"
      },
      {
        "id": "get-beers",
        "name": "beers",
        "type": "SAFE",
        "descriptor": [
          {
            "name": "page",
            "type": "SEMANTIC",
            "doc": {
              "format": "TEXT",
              "value": "The page to return."
            }
          },
          {
            "name": "size",
            "type": "SEMANTIC",
            "doc": {
              "format": "TEXT",
              "value": "The size of the page to return."
            }
          },
          {
            "name": "sort",
            "type": "SEMANTIC",
            "doc": {
              "format": "TEXT",
              "value": "The sorting criteria to use to calculate the content of the page."
            }
          }
        ],
        "rt": "#beer-representation"
      },
      {
        "id": "delete-beer",
        "name": "beer",
        "type": "IDEMPOTENT",
        "descriptor": [],
        "rt": "#beer-representation"
      },
      {
        "id": "get-beer",
        "name": "beer",
        "type": "SAFE",
        "descriptor": [],
        "rt": "#beer-representation"
      },
      {
        "id": "update-beer",
        "name": "beer",
        "type": "IDEMPOTENT",
        "descriptor": [],
        "rt": "#beer-representation"
      },
      {
        "id": "patch-beer",
        "name": "beer",
        "type": "UNSAFE",
        "descriptor": [],
        "rt": "#beer-representation"
      },
      {
        "name": "findAllByBeerStyle",
        "type": "SAFE",
        "descriptor": [
          {
            "name": "beerStyle",
            "type": "SEMANTIC",
            "doc": {
              "format": "TEXT",
              "value": "LAGER, PILSNER, STOUT, GOSE, PORTER, ALE, WHEAT, IPA, PALE_ALE, SAISON"
            }
          }
        ]
      },
      {
        "name": "findAllByBeerName",
        "type": "SAFE",
        "descriptor": [
          {
            "name": "beerName",
            "type": "SEMANTIC"
          }
        ]
      },
      {
        "name": "findAllByBeerNameAndBeerStyle",
        "type": "SAFE",
        "descriptor": [
          {
            "name": "beerName",
            "type": "SEMANTIC"
          },
          {
            "name": "beerStyle",
            "type": "SEMANTIC",
            "doc": {
              "format": "TEXT",
              "value": "LAGER, PILSNER, STOUT, GOSE, PORTER, ALE, WHEAT, IPA, PALE_ALE, SAISON"
            }
          }
        ]
      },
      {
        "name": "findByUpc",
        "type": "SAFE",
        "descriptor": [
          {
            "name": "upc",
            "type": "SEMANTIC"
          }
        ]
      }
    ]
  }
}
```