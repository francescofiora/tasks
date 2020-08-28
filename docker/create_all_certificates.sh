#!/bin/bash
OPENSSL_SUBJ="/C=IT/ST=Ireland/L=Cork"
OPENSSL_CA="${OPENSSL_SUBJ}/CN=ca.francescofiora.it"

# Generate new CA certificate ca.pem file.
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

./create_java_certificate.sh "${OPENSSL_SUBJ}/CN=tasks-activemq" activemq
./create_certificate.sh "${OPENSSL_SUBJ}/CN=tasks-mysql" mysql
./create_certificate.sh "${OPENSSL_SUBJ}/CN=tasks-myadmin" myadmin
./create_certificate.sh "${OPENSSL_SUBJ}/CN=tasks-mongodb" mongodb
./create_java_certificate.sh "${OPENSSL_SUBJ}/CN=tasks-api" tasks
./create_java_certificate.sh "${OPENSSL_SUBJ}/CN=tasks-executor" executor
./create_java_certificate.sh "${OPENSSL_SUBJ}/CN=localhost" localhost

cp ./certs/activemq-keystore.jks ./artemis-instance/etc
cp ./certs/activemq-truststore.ts ./artemis-instance/etc
