FROM nginx:1.15-alpine
COPY ./default.conf /etc/nginx/conf.d/default.conf
COPY ./https/fullchain.pem /etc/letsencrypt/live/foodchain.ddns.net/fullchain.pem
COPY ./https/privkey.pem /etc/letsencrypt/live/foodchain.ddns.net/privkey.pem
COPY ./https/options-ssl-nginx.conf /etc/letsencrypt/options-ssl-nginx.conf
COPY ./https/ssl-dhparams.pem /etc/letsencrypt/ssl-dhparams.pem