#!/bin/bash

# Tested with Ununtu 18 only
# Get the Minikube service URL
BASE_URL=$(minikube service cust-api --url)

# Check if the Minikube service URL is empty or invalid
if [ -z "$BASE_URL" ]; then
  echo
  echo
  echo "Failed to get the Minikube service URL. Please manually input the URL:"
  read -p "Enter API base URL (e.g., http://localhost:8080): " BASE_URL

  # Check if the input URL is still empty
  if [ -z "$BASE_URL" ]; then
    echo "Invalid input. Exiting script."
    exit 1
  fi
fi

echo "BASE URL: $BASE_URL"

BASE_URL="$BASE_URL/api/customer"
echo "API URL: $BASE_URL"
echo

check_status_code() {
  local STATUS_CODE=$1          # Actual status code
  local SUCCESS_CODES=$2        # Expected status codes (comma-separated)
  local ERROR_MESSAGE=$3

  IFS=',' read -ra CODES <<< "$SUCCESS_CODES"
  local MATCH_FOUND=false

  for CODE in "${CODES[@]}"; do
    if [[ "$STATUS_CODE" == "$CODE" ]]; then
      MATCH_FOUND=true
      break
    fi
  done

  if [[ "$MATCH_FOUND" == false ]]; then
    echo "$ERROR_MESSAGE"
    exit 1
  fi
}

# Create a new customer
echo "Creating a new customer..."
response=$(curl -s -w "%{http_code}" -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "middleName": "M",
    "lastName": "Doe",
    "emailAddress": "john.doe@john.com",
    "phoneNumber": "123-456-7890"
  }')
STATUS_CODE="${response: -3}"
CREATE_RESPONSE="${response::-3}"
echo "Create one customer, HTTP Status Code: $STATUS_CODE"
echo "Create, response Body: $CREATE_RESPONSE"
echo
check_status_code "$STATUS_CODE" "200,201" "Failed to create the customer. Test not completed."

# Extract the customer ID from the response
CUSTOMER_ID=$(echo $CREATE_RESPONSE | jq -r '.id')
echo "The created customer's ID: $CUSTOMER_ID"

# Get the created customer by ID
echo "Retrieving the created customer..."
response=$(curl -s -w "%{http_code}" -X GET "$BASE_URL/$CUSTOMER_ID")
STATUS_CODE="${response: -3}"
GET_RESPONSE="${response::-3}"
echo "Retrieve the customer, HTTP Status Code: $STATUS_CODE"
echo "Retrieve, response Body: $GET_RESPONSE"
echo
check_status_code "$STATUS_CODE" "200" "Failed to get the created customer. Test not completed."

# Update the customer
echo "Updating the customer..."
response=$(curl -s -w "%{http_code}" -X PUT "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
       "id": "'"$CUSTOMER_ID"'",
      "firstName": "Jane",
      "middleName": "G",
      "lastName": "Doe",
      "emailAddress": "jane@jane.com",
      "phoneNumber": "987-654-3210"
    }')
STATUS_CODE="${response: -3}"
UPDATE_RESPONSE="${response::-3}"
echo "Update the customer, HTTP Status Code: $STATUS_CODE"
echo "Update, response Body: $UPDATE_RESPONSE"
echo
check_status_code "$STATUS_CODE" "200" "Failed to update the created customer. Test not completed."

# Get the updated customer by ID
echo "Retrieving the updated customer..."
GET_UPDATED_RESPONSE=$(curl -s -X GET "$BASE_URL/$CUSTOMER_ID")
response=$(curl -s -w "%{http_code}" -X GET "$BASE_URL/$CUSTOMER_ID")
STATUS_CODE="${response: -3}"
GET_RESPONSE="${response::-3}"
echo "Retrieve the updated customer, HTTP Status Code: $STATUS_CODE"
echo "response Body: $GET_RESPONSE"
echo
check_status_code "$STATUS_CODE" "200" "Failed to get the updated customer. Test not completed."

# Delete the customer
echo "Deleting the customer..."
response=$(curl -s -w "%{http_code}" -X DELETE "$BASE_URL/$CUSTOMER_ID")
STATUS_CODE="${response: -3}"
DELETE_RESPONSE="${response::-3}"
echo "Delete the customer, HTTP Status Code: $STATUS_CODE"
echo "Delete, response Body: $DELETE_RESPONSE"
echo
check_status_code "$STATUS_CODE" "200" "Failed to delete the created customer. Test not completed."
echo "Integration test passed!"
echo