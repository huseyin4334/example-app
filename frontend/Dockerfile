# Frontend Dockerfile - Multi-stage build for Angular
FROM node:18-alpine AS build

# Set working directory
WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci --only=production

# Copy source code
COPY . .

# Build the Angular application for production
RUN npm run build

# Production stage - Nginx to serve static files
FROM nginx:alpine

# Copy built application from build stage
COPY --from=build /app/dist/location-transport-app /usr/share/nginx/html

# Copy custom nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]
