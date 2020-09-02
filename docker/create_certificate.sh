#!/bin/bash

SUBJ=$1
KEY_STORE_FILE="./certs/${2}-key.pem"
REQ_FILE="./certs/${2}-req.pem"
CERT_FILE="./certs/${2}-cert.pem"

# Create the certificates
if [ ! -f $KEY_STORE_FILE ]; then
	openssl req -newkey rsa:2048 -days 3600 -nodes -subj "${SUBJ}" -keyout $KEY_STORE_FILE -out $REQ_FILE
	openssl rsa -in $KEY_STORE_FILE -out $KEY_STORE_FILE
fi	

if [ ! -f $CERT_FILE ]; then
	openssl x509 -req -in $REQ_FILE -days 3600 -CA ./certs/ca.pem -CAkey ./certs/ca-key.pem -set_serial $3 -out $CERT_FILE
fi

# Verify the certificate are correct
openssl verify -CAfile ./certs/ca.pem $CERT_FILE

keytool -importcert -file $CERT_FILE -keystore ./certs/truststore.ts -storepass mypass -alias $2 -noprompt

chmod a=r $KEY_STORE_FILE
