# Vaultier
A simple plugin to make vault more *vaultier*.

## Developer reference

### API reference

### Acquiring an API key

Log into the minecraft server you wish and run the `/getapikey` command

### Using the API

To access the endpoint you should ask the server owner for where they have hosted it.

### Request Funds

- **Endpoint:** `/transfer`
- **Method:** `POST`
- **Description:** Call this endpoint to request funds from a player.
- **Body Parameters:**
  - `Key` (required): Your API key.
  - `Player` (required): The player you wish to transfer funds from
  - `Value` (required): The amount you wish to transfer
- **Example Request:**
  ```bash
  # Transfer $5 from Example Player to yourself
  curl -X POST 'https://example.com/transfer' \
  -H 'Content-Type: application/json' \
  -d '{
        "Key": "[YOUR API KEY]",
        "Player": "Example Player",
        "Value": 5
      }'
  ```
- **Example Response:**
  ```json
  {
    "error": null,
    "id": "Example Transaction ID"
  }
  ```

### Checking Request status

- **Endpoint:** `/stat/<transactionID>`
- **Method:** `GET`
- **Description:** Call this endpoint check on the status of a request
- **URL Parameters:**
  - `transactionID` (required): This can be acquired by creating a transaction.
- **Example Request:**
  ```bash
  curl 'https://example.com/stat/ExampleTransactionId'
  ```
- **Example Response:**
  ```json
  {
    "error": null,
    "status": "SUCCESS"
  }
  ```
  *Note: the `status` of a transaction could be either `PENDING`, `SUCCESS` or `REJECTED`. If it could not be found then it timed out.
## Owner reference

### Configuring

There is only 1 configurable thing about this plugin, you can access it in the `config.yml` file in the plugins data directory.

### Use a reverse proxy

You should use a naming scheme for the URLs similar to
```
example.com/vaultier/<servername>/
```

## To Do

- Rate limit using ~~IP/~~ Key
- Allow Blocking users
- Better error handling
- Optimisations