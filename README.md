Step 1: Set up Keycloak with Custom SPI => clone project


Step 2: Build and Deploy the SPI
	- Build the SPI project: mvn clean install
	- Copy the JAR to Keycloak's providers directory
	- Build Keycloak with: kc.[sh|bat] build
	- Start Keycloak with: kc.[sh|bat] start-dev

 
Step 3: Configure Keycloak
	- Add a new realm called "realm-api"
	- Add new client called "client-app"
	- Client authentication :ON
	- Service accounts roles: ON
	- Verification Step: if we go to the Provider info page, available at Keycloak’s admin console, we’ll see our custom-protocol-mapper

 
Step 4: Configure Client
	- client-app-dedicated and go to its Add mapper By configuration to create a new mapping
	- Enter the Mapper type Custom Token Mapper (the value we used for the getDisplayType() method in our CustomProtocolMapper class)

 
Step 5: Testing
curl --location --request POST 'http://server-ip:server-port/auth/realms/realm-api/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=client-app' \
--data-urlencode 'client_secret=<client-secret>' \
--data-urlencode 'grant_type=client_credentials'
