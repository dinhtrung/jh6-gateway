#!/bin/bash

rsync -avP target/*.jar root@10.146.58.31:/srv/gateway/
ssh root@10.146.58.31 systemctl restart gateway
