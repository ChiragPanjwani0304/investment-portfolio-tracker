# Investment Portfolio Tracker

A full-stack investment tracking application built with Spring Boot and React.

## Features
- JWT-based register/login
- Create and manage multiple portfolios
- Track stock holdings with average buy price
- Real-time stock prices via Alpha Vantage API
- Automatic price refresh via Spring @Scheduled job (NSE/BSE market hours)
- Unrealized gain/loss calculation per holding
- Buy/sell transaction history
- Watchlist and price alerts

## Tech Stack
**Backend:** Java, Spring Boot 3, Spring Security, JWT, Hibernate, JPA, MySQL  
**Frontend:** React, Recharts, Axios  
**External API:** Alpha Vantage (real-time stock prices)

## Setup

### Backend
1. Clone the repo
2. Copy `src/main/resources/application.properties.example` to `application.properties`
3. Fill in your MySQL credentials, JWT secret, and Alpha Vantage API key
4. Run the SQL schema from `schema.sql`
5. Run the Spring Boot app from IntelliJ

### Frontend
1. `cd frontend`
2. `npm install`
3. `npm start`

## Environment Variables
| Property | Description |
|---|---|
| `spring.datasource.password` | MySQL password |
| `app.jwt.secret` | 256-bit JWT secret |
| `alphavantage.api.key` | Alpha Vantage free API key |

## API Endpoints
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register |
| POST | `/api/auth/login` | Login |
| GET | `/api/portfolios` | Get portfolios |
| POST | `/api/portfolios/{id}/holdings` | Add holding |
| GET | `/api/portfolios/{id}/transactions/gain-loss` | Gain/loss |
| POST | `/api/prices/refresh` | Manual price refresh |
