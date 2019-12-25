FROM node:alpine as builder
LABEL author="danijel.fon@gmail.com"
WORKDIR '/app'
COPY package.json .
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=builder /app/build /var/www
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 8050
ENTRYPOINT ["nginx","-g","daemon off;"]