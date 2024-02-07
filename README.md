# TradeBit
This mobile crypto trading bot application is designed to automate cryptocurrency trading on the Binance platform. 
Leveraging advanced algorithms and real-time market analysis, it enables users to execute trades automatically, optimizing for profitability and risk management.
From executing trades manually to deploying sophisticated trading bots, our application empowers users to make informed decisions and automate their trading strategies.
## Table of Contents
- [Features](#features)
- [Technolody Stack](#TechnologyStack)
- [Getting Started](#GettingStarted)
- [Usage](#Usage)

## Features
- Binance Integration:
  - Directly connect your application account to Binance, providing seamless access to real-time market data and the ability to execute trades on the platform.
- Manual Trading Support:
  - Offers users the flexibility to manually execute trades, giving them complete control over their trading decisions.
- Automated Trading Bots:
  - Users can create and customize trading bots with various parameters, allowing for automated trading based on predefined strategies. This feature is designed to help users capitalize on market opportunities 24/7.
- Advanced Market Analysis Tools:
  - Equipped with powerful analysis tools, our platform enables users to conduct in-depth market research and analysis, aiding in more informed trading decisions.
- AI-Powered Predictions:
  - Leverage AI-driven insights for cryptocurrency market predictions, helping users to make smarter trading choices based on data-driven forecasts.
- Secure Authentication and Authorization:
  - With Keycloak integration, our platform ensures robust security measures for user authentication and authorization, safeguarding users' information and assets.
- Cross-Platform Accessibility:
  - Thanks to the client application developed in Flutter, users can enjoy a consistent and responsive experience across all devices, ensuring they can trade anytime, anywhere.
- Real-Time Notifications:
  - Stay updated with real-time alerts and notifications on market movements and bot trading activities, ensuring users never miss a critical trading opportunity.

## Technology Stack
- Spring Boot:
  - For creating a standalone, production-grade application.
- REST API:
  - Facilitates communication between the frontend application and the backend services.
- Spring Security & Keycloak:
  - For secure authentication and authorization.
- PostgreSQL:
  - Database for storing user and application data.
- RabbitMQ:
  - Messaging queue for handling asynchronous tasks and communication.
- Docker:
  - For containerizing the application and ensuring consistent environments.
- Binance API:
  - For executing trades and retrieving market data as well as user account data.

## Setup Instructions
### Backend
1. Clone the Repository
```
git clone https://github.com/bodyakyryliuk/TradeBit.git
```
2. Navigate to the server directory.
```
cd server
```
3. Use Docker to build and run the application:
```
docker-compose up --build
```

### Flutter Client
1. Navigate to the client directory
```
cd client
```
2. Install dependencies
```
flutter pub get
```
3. Run the flutter application
```
flutter run
```

## Team Contributions
Our project's success is attributed to the dedicated efforts of our talented team, each bringing specialized skills and expertise to the table. Here’s a breakdown of the roles and responsibilities:

### Bohdan Kyryliuk: 
- Spearheaded the development of the backend infrastructure, utilizing Spring Boot to create a robust and scalable system within microservices architecture.

### Sergiy Vergun: 
- Led the development of the client-side application, crafting a seamless and intuitive user interface with Flutter using clean architecture.

### Ilgin Sogut: 
- Focused on the integration and development of AI-powered prediction models for providing user

This collaborative effort has resulted in a comprehensive trading platform that is powerful, user-friendly, and innovative. We extend our deepest gratitude to each team member for their remarkable contributions and unwavering commitment to the project's success.


