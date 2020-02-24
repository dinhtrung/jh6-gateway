#!/bin/bash
# Usage: ./convert-logo.sh logo.png

/usr/bin/convert $1 -resize     x42 ./src/main/webapp/content/images/logo.png
/usr/bin/convert $1 -resize 512x512  ./src/main/webapp/content/images/logo-512.png
/usr/bin/convert $1 -resize 192x192  ./src/main/webapp/content/images/logo-192.png
/usr/bin/convert $1 -resize 256x256  ./src/main/webapp/content/images/logo-256.png
/usr/bin/convert $1 -resize 384x384  ./src/main/webapp/content/images/logo-384.png
