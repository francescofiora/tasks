#!/bin/bash
OPENSSL_SUBJ="/C=IE/ST=Ireland/L=Cork"
OPENSSL_CA="${OPENSSL_SUBJ}/CN=ca.francescofiora.it"

if [ -d ./certs ]; then
	rm certs/*
fi
if [ ! -d ./certs ]; then
	mkdir certs
fi

# Generate new CA certificate ca.pem file.
if [ ! -f ./certs/ca-key.pem ]; then
	openssl genrsa -out ./certs/ca-key.pem 2048
fi

if [ ! -f ./certs/ca.pem ]; then
	openssl req -new -x509 -nodes -days 3600 -subj "${OPENSSL_CA}" -key ./certs/ca-key.pem -out ./certs/ca.pem
fi

chmod a=r ./certs/ca.pem

keytool -importcert -file ./certs/ca.pem -keystore ./certs/truststore.ts -storepass mypass -alias "ca.francescofiora.it" -noprompt

./create_java_certificate.sh "${OPENSSL_SUBJ}/CN=tasks-activemq" "tasks-activemq" "01"
./create_certificate.sh "${OPENSSL_SUBJ}/CN=tasks-mysql" "tasks-mysql" "02"
./create_certificate.sh "${OPENSSL_SUBJ}/CN=tasks-myadmin" "tasks-myadmin" "03"
./create_certificate.sh "${OPENSSL_SUBJ}/CN=tasks-mongodb" "tasks-mongodb" "04"
./create_java_certificate.sh "${OPENSSL_SUBJ}/CN=tasks-api" "tasks-api" "05"
./create_java_certificate.sh "${OPENSSL_SUBJ}/CN=tasks-executor" "tasks-executor" "06"

cp ./certs/tasks-activemq-keystore.jks ./artemis-instance/etc
cp ./certs/truststore.ts ./artemis-instance/etc

cp ./certs/tasks-api-keystore.jks ./tasks-api/config
cp ./certs/truststore.ts ./tasks-api/config

cp ./certs/tasks-executor-keystore.jks ./tasks-executor/config
cp ./certs/truststore.ts ./tasks-executor/config

cat ./certs/tasks-mongodb-key.pem ./certs/tasks-mongodb-cert.pem > ./certs/mongodb.pem

