#!/bin/bash

rsync -avP --delete target/classes/static/* root@10.146.58.31:/srv/www/
