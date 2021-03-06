#!/bin/bash

SUBJ=$1
KEY_STORE_PEM_FILE="./certs/${2}-key.pem"
REQ_FILE="./certs/${2}-req.pem"
CERT_FILE="./certs/${2}-cert.pem"
KEY_STORE_P12_FILE="./certs/${2}-keystore.p12"
KEY_STORE_JKS_FILE="./certs/${2}-keystore.jks"

# Create the certificates
if [ ! -f $KEY_STORE_PEM_FILE ]; then
	openssl req -newkey rsa:2048 -days 3600 -nodes -subj "${SUBJ}" -keyout $KEY_STORE_PEM_FILE -out $REQ_FILE
	openssl rsa -in $KEY_STORE_PEM_FILE -out $KEY_STORE_PEM_FILE
fi	

if [ ! -f $CERT_FILE ]; then
	openssl x509 -req -in $REQ_FILE -days 3600 -CA ./certs/ca.pem -CAkey ./certs/ca-key.pem -set_serial $3 -out $CERT_FILE
fi

# Verify the certificate are correct
openssl verify -CAfile ./certs/ca.pem $CERT_FILE

chmod a=r $KEY_STORE_PEM_FILE

if [ ! -f $KEY_STORE_P12_FILE ]; then
	openssl pkcs12 -export -in $CERT_FILE -inkey $KEY_STORE_PEM_FILE -passout pass:mypass -out $KEY_STORE_P12_FILE
fi

keytool -importcert -trustcacerts -file $CERT_FILE -keystore ./certs/truststore.ts -storepass mypass -alias $2 -noprompt

if [ ! -f $KEY_STORE_JKS_FILE ]; then
	keytool -importkeystore -srckeystore $KEY_STORE_P12_FILE -srcstoretype pkcs12 -srcstorepass mypass -destkeystore $KEY_STORE_JKS_FILE -deststoretype JKS -deststorepass mypass
	keytool -import -alias ca -keystore $KEY_STORE_JKS_FILE -file ./certs/ca.pem -storepass mypass -noprompt
fi
