<?php
$i = 1;
$cfg['Servers'][$i]['ssl'] = true;
$cfg['Servers'][$i]['ssl_key'] = '/etc/certs/tasks-myadmin-key.pem';
$cfg['Servers'][$i]['ssl_cert'] = '/etc/certs/tasks-myadmin-cert.pem';
$cfg['Servers'][$i]['ssl_ca'] = '/etc/certs/ca.pem';
$cfg['Servers'][$i]['ssl_ca_path'] = '/etc/certs/';
