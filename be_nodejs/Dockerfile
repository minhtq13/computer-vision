FROM node:20.18.3-alpine AS base
# Create app directory
WORKDIR /usr/app
COPY package*.json ./

# Install dependencies
FROM base AS dependencies
RUN yarn install

# Bundle app source
FROM base AS build
COPY --from=dependencies /usr/app/node_modules ./node_modules
COPY . .
RUN yarn build:production

# Production image
FROM base AS release
# Set NODE_ENV
ENV NODE_ENV production
# Copy production dependencies
COPY --from=dependencies /usr/app/node_modules ./node_modules
# Copy built app (if you have a build step)
COPY --from=build /usr/app/dist ./dist
# Expose the port your app runs on
EXPOSE 8098
# Start the application
CMD ["node", "dist/index.js"]